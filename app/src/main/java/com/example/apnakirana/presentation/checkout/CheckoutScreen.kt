package com.example.apnakirana.presentation.checkout

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.example.apnakirana.data.local.entity.Address
import com.example.apnakirana.data.local.entity.PaymentMethod
import com.example.apnakirana.domain.model.CartItemWithProduct

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CheckoutScreen(
    onBackPressed: () -> Unit,
    onOrderPlaced: (String) -> Unit,
    viewModel: CheckoutViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val orderCreated by viewModel.orderCreated.collectAsState()

    var showAddressSelection by remember { mutableStateOf(false) }
    var showPaymentMethodSelection by remember { mutableStateOf(false) }

    // Navigate to order confirmation when order is created
    LaunchedEffect(orderCreated) {
        orderCreated?.let { orderId ->
            onOrderPlaced(orderId)
            viewModel.resetOrderCreated()
        }
    }

    // Handle error messages
    uiState.errorMessage?.let { error ->
        LaunchedEffect(error) {
            // You can show a snackbar here
            viewModel.clearError()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        // Top App Bar
        TopAppBar(
            title = { Text("Checkout", style = MaterialTheme.typography.titleLarge) },
            navigationIcon = {
                IconButton(onClick = onBackPressed) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = MaterialTheme.colorScheme.surface
            )
        )

        if (uiState.isLoading) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else {
            LazyColumn(
                modifier = Modifier.weight(1f),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Cart Items Section
                item {
                    CheckoutSection(title = "Order Items (${uiState.cartItems.size})") {
                        CartItemsList(cartItems = uiState.cartItems)
                    }
                }

                // Delivery Address Section
                item {
                    CheckoutSection(title = "Delivery Address") {
                        AddressSelectionCard(
                            selectedAddress = uiState.selectedAddress,
                            onAddressClick = { showAddressSelection = true }
                        )
                    }
                }

                // Payment Method Section
                item {
                    CheckoutSection(title = "Payment Method") {
                        PaymentMethodCard(
                            selectedPaymentMethod = uiState.selectedPaymentMethod,
                            onPaymentMethodClick = { showPaymentMethodSelection = true }
                        )
                    }
                }

                // Order Summary Section
                item {
                    CheckoutSection(title = "Bill Details") {
                        OrderSummaryCard(uiState = uiState)
                    }
                }
            }

            // Place Order Button
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = "₹${String.format("%.0f", uiState.totalAmount)}",
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.primary
                            )
                            Text(
                                text = "Total Amount",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                        Button(
                            onClick = {
                                println("🔘 DEBUG: Place Order button clicked in UI!")
                                viewModel.placeOrder()
                            },
                            enabled = uiState.canPlaceOrder,
                            modifier = Modifier.height(56.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.primary
                            )
                        ) {
                            if (uiState.isPlacingOrder) {
                                CircularProgressIndicator(
                                    modifier = Modifier.size(20.dp),
                                    color = MaterialTheme.colorScheme.onPrimary
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                            }
                            Text(
                                text = if (uiState.isPlacingOrder) "Placing..." else "Place Order",
                                style = MaterialTheme.typography.titleMedium
                            )
                        }
                    }
                }
            }
        }
    }

    // Address Selection Bottom Sheet
    if (showAddressSelection) {
        AddressSelectionBottomSheet(
            addresses = uiState.addresses,
            selectedAddress = uiState.selectedAddress,
            onAddressSelected = { address ->
                viewModel.selectAddress(address)
                showAddressSelection = false
            },
            onDismiss = { showAddressSelection = false }
        )
    }

    // Payment Method Selection Bottom Sheet
    if (showPaymentMethodSelection) {
        PaymentMethodSelectionBottomSheet(
            paymentMethods = uiState.paymentMethods,
            selectedPaymentMethod = uiState.selectedPaymentMethod,
            onPaymentMethodSelected = { paymentMethod ->
                viewModel.selectPaymentMethod(paymentMethod)
                showPaymentMethodSelection = false
            },
            onDismiss = { showPaymentMethodSelection = false }
        )
    }
}

@Composable
private fun CheckoutSection(
    title: String,
    content: @Composable () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 12.dp)
            )
            content()
        }
    }
}

@Composable
private fun CartItemsList(cartItems: List<CartItemWithProduct>) {
    Column(
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        cartItems.forEach { cartItem ->
            CheckoutCartItem(cartItem = cartItem)
        }
    }
}

@Composable
private fun CheckoutCartItem(cartItem: CartItemWithProduct) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            modifier = Modifier.weight(1f)
        ) {
            AsyncImage(
                model = cartItem.product.imageUrl,
                contentDescription = cartItem.product.name,
                modifier = Modifier
                    .size(50.dp)
                    .clip(RoundedCornerShape(8.dp)),
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.width(12.dp))

            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = cartItem.product.name,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Medium,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )

                Text(
                    text = "Qty: ${cartItem.quantity}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        Column(
            horizontalAlignment = Alignment.End
        ) {
            Text(
                text = "₹${String.format("%.0f", cartItem.totalPrice)}",
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )

            cartItem.product.originalPrice?.let { originalPrice ->
                if (originalPrice > cartItem.product.price) {
                    Text(
                        text = "₹${String.format("%.0f", originalPrice * cartItem.quantity)}",
                        style = MaterialTheme.typography.bodySmall,
                        textDecoration = TextDecoration.LineThrough,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }
}

@Composable
private fun AddressSelectionCard(
    selectedAddress: Address?,
    onAddressClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onAddressClick() },
        colors = CardDefaults.cardColors(
            containerColor = if (selectedAddress != null)
                MaterialTheme.colorScheme.primaryContainer
            else MaterialTheme.colorScheme.surface
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (selectedAddress != null) {
                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = selectedAddress.name,
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Surface(
                            color = MaterialTheme.colorScheme.primary,
                            shape = RoundedCornerShape(4.dp)
                        ) {
                            Text(
                                text = selectedAddress.addressType,
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.onPrimary,
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = "${selectedAddress.addressLine1}, ${selectedAddress.city}",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )

                    Text(
                        text = "📱 ${selectedAddress.phone}",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            } else {
                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = "Select Delivery Address",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Medium
                    )
                    Text(
                        text = "Choose where to deliver your order",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            Icon(
                imageVector = Icons.Default.ChevronRight,
                contentDescription = "Select Address",
                tint = MaterialTheme.colorScheme.primary
            )
        }
    }
}

@Composable
private fun PaymentMethodCard(
    selectedPaymentMethod: PaymentMethod?,
    onPaymentMethodClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onPaymentMethodClick() },
        colors = CardDefaults.cardColors(
            containerColor = if (selectedPaymentMethod != null)
                MaterialTheme.colorScheme.primaryContainer
            else MaterialTheme.colorScheme.surface
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (selectedPaymentMethod != null) {
                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = selectedPaymentMethod.name,
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = selectedPaymentMethod.description,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            } else {
                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = "Select Payment Method",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Medium
                    )
                    Text(
                        text = "Choose how to pay for your order",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            Icon(
                imageVector = Icons.Default.ChevronRight,
                contentDescription = "Select Payment Method",
                tint = MaterialTheme.colorScheme.primary
            )
        }
    }
}

@Composable
private fun OrderSummaryCard(uiState: com.example.apnakirana.domain.model.CheckoutUiState) {
    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        OrderSummaryRow(
            label = "Item Total",
            value = "₹${String.format("%.0f", uiState.subtotal)}"
        )

        if (uiState.savings > 0) {
            OrderSummaryRow(
                label = "You Saved",
                value = "-₹${String.format("%.0f", uiState.savings)}",
                valueColor = Color(0xFF4CAF50)
            )
        }

        OrderSummaryRow(
            label = "Delivery Fee",
            value = if (uiState.deliveryFee > 0) "₹${String.format("%.0f", uiState.deliveryFee)}" else "FREE",
            valueColor = if (uiState.deliveryFee == 0.0) Color(0xFF4CAF50) else null
        )

        OrderSummaryRow(
            label = "Platform Fee",
            value = "₹${String.format("%.0f", uiState.platformFee)}"
        )

        if (uiState.discount > 0) {
            OrderSummaryRow(
                label = "Discount",
                value = "-₹${String.format("%.0f", uiState.discount)}",
                valueColor = Color(0xFF4CAF50)
            )
        }

        if (uiState.subtotal < 499 && uiState.deliveryFee > 0) {
            Text(
                text = "💡 Add ₹${String.format("%.0f", 499 - uiState.subtotal)} more for FREE delivery",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        MaterialTheme.colorScheme.primaryContainer,
                        RoundedCornerShape(8.dp)
                    )
                    .padding(12.dp)
            )
        }

        Divider(modifier = Modifier.padding(vertical = 8.dp))

        OrderSummaryRow(
            label = "Total Amount",
            value = "₹${String.format("%.0f", uiState.totalAmount)}",
            isTotal = true
        )
    }
}

@Composable
private fun OrderSummaryRow(
    label: String,
    value: String,
    valueColor: Color? = null,
    isTotal: Boolean = false
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            style = if (isTotal) MaterialTheme.typography.titleMedium else MaterialTheme.typography.bodyMedium,
            fontWeight = if (isTotal) FontWeight.Bold else FontWeight.Normal
        )
        Text(
            text = value,
            style = if (isTotal) MaterialTheme.typography.titleMedium else MaterialTheme.typography.bodyMedium,
            fontWeight = if (isTotal) FontWeight.Bold else FontWeight.Medium,
            color = valueColor ?: if (isTotal) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
        )
    }
}