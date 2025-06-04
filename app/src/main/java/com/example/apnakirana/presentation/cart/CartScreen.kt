package com.example.apnakirana.presentation.cart

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.example.apnakirana.R
import com.example.apnakirana.domain.model.CartItemWithProduct

@Composable
fun CartScreen(
    onProductClick: (String) -> Unit = {},
    viewModel: CartViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    if (uiState.isLoading) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
    } else if (uiState.isEmpty) {
        EmptyCartState()
    } else {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFF5F5F5))
        ) {
            // Cart Header
            CartHeader(
                itemCount = uiState.totalQuantity,
                onClearCart = viewModel::clearCart
            )

            // Scrollable Content
            LazyColumn(
                modifier = Modifier.weight(1f),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Cart Items
                items(uiState.cartItems) { cartItem ->
                    CartItemCard(
                        cartItem = cartItem,
                        onQuantityChange = { quantity ->
                            viewModel.updateQuantity(cartItem.product.id, quantity)
                        },
                        onRemoveClick = {
                            viewModel.removeItem(cartItem.product.id)
                        },
                        onProductClick = {
                            onProductClick(cartItem.product.id)
                        }
                    )
                }

                // Order Summary
                item {
                    CartSummaryCard(uiState = uiState)
                }

                // Extra spacing for checkout button
                item {
                    Spacer(modifier = Modifier.height(80.dp))
                }
            }

            // Fixed Checkout Button at Bottom
            Surface(
                modifier = Modifier.fillMaxWidth(),
                color = Color.White,
                shadowElevation = 8.dp
            ) {
                Button(
                    onClick = { viewModel.proceedToCheckout() },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                        .height(56.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF4CAF50)
                    )
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.ShoppingCart,
                            contentDescription = null,
                            tint = Color.White
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Proceed to Checkout • ₹${uiState.finalTotal.toInt()}",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CartHeader(
    itemCount: Int,
    onClearCart: () -> Unit
) {
    TopAppBar(
        title = {
            Text(
                text = "My Cart ($itemCount items)",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
        },
        actions = {
            if (itemCount > 0) {
                TextButton(onClick = onClearCart) {
                    Text(
                        text = "Clear All",
                        color = Color.Red,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Color.White
        )
    )
}

@Composable
fun CartItemCard(
    cartItem: CartItemWithProduct,
    onQuantityChange: (Int) -> Unit,
    onRemoveClick: () -> Unit,
    onProductClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onProductClick() },
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Product Image
            AsyncImage(
                model = cartItem.product.imageUrl,
                contentDescription = cartItem.product.name,
                modifier = Modifier
                    .size(80.dp)
                    .clip(RoundedCornerShape(8.dp)),
                contentScale = ContentScale.Crop,
                placeholder = painterResource(android.R.drawable.ic_menu_gallery),
                error = painterResource(android.R.drawable.ic_menu_gallery)
            )

            Spacer(modifier = Modifier.width(12.dp))

            // Product Details
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = cartItem.product.name,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    color = Color.Black
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = cartItem.product.unit,
                    fontSize = 12.sp,
                    color = Color.Gray
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Price
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "₹${cartItem.product.price.toInt()}",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF4CAF50)
                    )

                    cartItem.product.originalPrice?.let { originalPrice ->
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "₹${originalPrice.toInt()}",
                            fontSize = 12.sp,
                            color = Color.Gray,
                            style = androidx.compose.ui.text.TextStyle(
                                textDecoration = TextDecoration.LineThrough
                            )
                        )
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Total for this item
                Text(
                    text = "Total: ₹${cartItem.totalPrice.toInt()}",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
            }

            Spacer(modifier = Modifier.width(8.dp))

            // Quantity Controls and Remove
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Remove Button
                IconButton(
                    onClick = onRemoveClick,
                    modifier = Modifier.size(32.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Remove",
                        tint = Color.Red,
                        modifier = Modifier.size(20.dp)
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Quantity Selector
                Surface(
                    shape = RoundedCornerShape(6.dp),
                    color = Color(0xFFF5F5F5)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(2.dp)
                    ) {
                        IconButton(
                            onClick = {
                                if (cartItem.quantity > 1) {
                                    onQuantityChange(cartItem.quantity - 1)
                                }
                            },
                            modifier = Modifier.size(28.dp),
                            enabled = cartItem.quantity > 1
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.outline_remove_24),
                                contentDescription = "Decrease",
                                tint = if (cartItem.quantity > 1) Color(0xFF4CAF50) else Color.Gray,
                                modifier = Modifier.size(16.dp)
                            )
                        }

                        Text(
                            text = cartItem.quantity.toString(),
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 8.dp)
                        )

                        IconButton(
                            onClick = { onQuantityChange(cartItem.quantity + 1) },
                            modifier = Modifier.size(28.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Add,
                                contentDescription = "Increase",
                                tint = Color(0xFF4CAF50),
                                modifier = Modifier.size(16.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun CartSummaryCard(
    uiState: CartUiState
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(
            modifier = Modifier.padding(20.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Bill Details",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )

                Surface(
                    color = Color(0xFF4CAF50).copy(alpha = 0.1f),
                    shape = RoundedCornerShape(6.dp)
                ) {
                    Text(
                        text = "${uiState.cartItems.size} items",
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color(0xFF4CAF50)
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Subtotal
            SummaryRow(
                label = "Item total",
                value = "₹${uiState.subtotal.toInt()}",
                textColor = Color.Gray
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Delivery Fee
            SummaryRow(
                label = "Delivery fee",
                value = if (uiState.deliveryFee > 0) "₹${uiState.deliveryFee.toInt()}" else "FREE",
                textColor = Color.Gray
            )

            // Savings
            if (uiState.totalSavings > 0) {
                Spacer(modifier = Modifier.height(8.dp))
                SummaryRow(
                    label = "Total savings",
                    value = "-₹${uiState.totalSavings.toInt()}",
                    textColor = Color(0xFF4CAF50),
                    valueColor = Color(0xFF4CAF50)
                )
            }

            // Platform fee
            Spacer(modifier = Modifier.height(8.dp))
            SummaryRow(
                label = "Platform fee",
                value = "₹5",
                textColor = Color.Gray
            )

            Spacer(modifier = Modifier.height(12.dp))

            Divider(
                color = Color.Gray.copy(alpha = 0.3f),
                thickness = 1.dp
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Total
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "To pay",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
                Text(
                    text = "₹${(uiState.finalTotal + 5).toInt()}", // Adding platform fee
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF4CAF50)
                )
            }

            if (uiState.totalSavings > 0) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "You saved ₹${uiState.totalSavings.toInt()} on this order! 🎉",
                    fontSize = 12.sp,
                    color = Color(0xFF4CAF50),
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}

@Composable
private fun SummaryRow(
    label: String,
    value: String,
    textColor: Color = Color.Gray,
    valueColor: Color = Color.Black
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = label,
            fontSize = 14.sp,
            color = textColor
        )
        Text(
            text = value,
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium,
            color = valueColor
        )
    }
}

@Composable
fun CheckoutButton(
    totalAmount: Double,
    onCheckoutClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier.fillMaxWidth(),
        color = Color.White,
        shadowElevation = 8.dp
    ) {
        Button(
            onClick = onCheckoutClick,
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .height(50.dp),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF4CAF50)
            )
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Icon(
                    imageVector = Icons.Default.ShoppingCart,
                    contentDescription = null,
                    tint = Color.White
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Checkout - ₹${totalAmount.toInt()}",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }
        }
    }
}

@Composable
fun EmptyCartState() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "🛒",
            fontSize = 80.sp
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Your cart is empty",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Add some fresh groceries to get started!",
            fontSize = 16.sp,
            color = Color.Gray
        )
    }
}