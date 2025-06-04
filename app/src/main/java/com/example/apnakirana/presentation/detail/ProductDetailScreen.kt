package com.example.apnakirana.presentation.detail

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Star
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
import com.example.apnakirana.data.local.entity.Product
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.layout.navigationBars
import com.example.apnakirana.domain.repository.CartRepository


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductDetailScreen(
    onBackPress: () -> Unit,
    onRelatedProductClick: (Product) -> Unit,
    viewModel: ProductDetailViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    var isFavorite by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .windowInsetsPadding(WindowInsets.systemBars)
    ) {
        if (uiState.isLoading) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else if (uiState.product != null) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.White),
                contentPadding = PaddingValues(bottom = 160.dp) // Space for bottom buttons
            ) {
                item {
                    ProductImageSection(
                        product = uiState.product!!,
                        onBackPress = onBackPress,
                        isFavorite = isFavorite,
                        onFavoriteToggle = { isFavorite = !isFavorite }
                    )
                }

                item {
                    ProductInfoSection(
                        product = uiState.product!!,
                        quantity = uiState.quantity,
                        onQuantityChange = viewModel::onQuantityChange
                    )
                }

                item {
                    ProductDescriptionSection(
                        description = uiState.product!!.description
                    )
                }

                item {
                    RelatedProductsSection(
                        products = uiState.relatedProducts,
                        onProductClick = { product ->
                            onRelatedProductClick(product)
                            viewModel.onRelatedProductClick(product)
                        }
                    )
                }
            }

            // Bottom Action Buttons
            BottomActionButtons(
                product = uiState.product!!,
                quantity = uiState.quantity,
                onAddToCart = viewModel::onAddToCart,
                onBuyNow = viewModel::onBuyNow,
                modifier = Modifier.align(Alignment.BottomCenter)
            )
        } else {
            // Error State - same as before
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(32.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(text = "😞", fontSize = 64.sp)
                Spacer(modifier = Modifier.height(16.dp))
                Text(text = "Product not found", fontSize = 20.sp, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(16.dp))
                Button(
                    onClick = onBackPress,
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4CAF50))
                ) {
                    Text("Go Back")
                }
            }
        }
    }

    // Show add to cart message and error handling remain the same
    if (uiState.showAddToCartMessage) {
        LaunchedEffect(Unit) {
            kotlinx.coroutines.delay(2000)
            viewModel.clearAddToCartMessage()
        }
    }

    uiState.errorMessage?.let { error ->
        LaunchedEffect(error) {
            viewModel.clearError()
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductImageSection(
    product: Product,
    onBackPress: () -> Unit,
    isFavorite: Boolean,
    onFavoriteToggle: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(300.dp)
    ) {
        AsyncImage(
            model = product.imageUrl,
            contentDescription = product.name,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop,
            placeholder = painterResource(android.R.drawable.ic_menu_gallery),
            error = painterResource(android.R.drawable.ic_menu_gallery)
        )

        // Top Bar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Surface(
                onClick = onBackPress,
                shape = CircleShape,
                color = Color.Black.copy(alpha = 0.3f),
                modifier = Modifier.size(40.dp)
            ) {
                Box(
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Back",
                        tint = Color.White
                    )
                }
            }

            Surface(
                onClick = onFavoriteToggle,
                shape = CircleShape,
                color = Color.Black.copy(alpha = 0.3f),
                modifier = Modifier.size(40.dp)
            ) {
                Box(
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = if (isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                        contentDescription = "Favorite",
                        tint = if (isFavorite) Color.Red else Color.White
                    )
                }
            }
        }

        // Discount Badge
        if (product.discount > 0) {
            Surface(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(16.dp)
                    .offset(y = 50.dp),
                color = Color.Red,
                shape = RoundedCornerShape(8.dp)
            ) {
                Text(
                    text = "${product.discount}% OFF",
                    fontSize = 12.sp,
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                )
            }
        }
    }
}

@Composable
fun ProductInfoSection(
    product: Product,
    quantity: Int,
    onQuantityChange: (Int) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        // Product Name
        Text(
            text = product.name,
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Unit and Rating
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = product.unit,
                fontSize = 16.sp,
                color = Color.Gray
            )

            if (product.rating > 0) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Star,
                        contentDescription = "Rating",
                        tint = Color(0xFFFFC107),
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "${product.rating}",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color.Black
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Price Section
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = "₹${product.price.toInt()}",
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF4CAF50)
                )

                product.originalPrice?.let { originalPrice ->
                    Text(
                        text = "₹${originalPrice.toInt()}",
                        fontSize = 16.sp,
                        color = Color.Gray,
                        style = androidx.compose.ui.text.TextStyle(
                            textDecoration = TextDecoration.LineThrough
                        )
                    )
                }
            }

            // Quantity Selector
            QuantitySelector(
                quantity = quantity,
                onQuantityChange = onQuantityChange
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Stock Status
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Surface(
                color = if (product.isInStock) Color(0xFF4CAF50) else Color.Red,
                shape = CircleShape,
                modifier = Modifier.size(8.dp)
            ) {}

            Spacer(modifier = Modifier.width(8.dp))

            Text(
                text = if (product.isInStock) "In Stock" else "Out of Stock",
                fontSize = 14.sp,
                color = if (product.isInStock) Color(0xFF4CAF50) else Color.Red,
                fontWeight = FontWeight.Medium
            )
        }
    }
}

@Composable
fun QuantitySelector(
    quantity: Int,
    onQuantityChange: (Int) -> Unit
) {
    Surface(
        shape = RoundedCornerShape(8.dp),
        color = Color(0xFFF5F5F5),
        modifier = Modifier.wrapContentSize()
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(4.dp)
        ) {
            IconButton(
                onClick = {
                    if (quantity > 1) onQuantityChange(quantity - 1)
                },
                enabled = quantity > 1
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.outline_remove_24),
                    contentDescription = "Decrease",
                    tint = if (quantity > 1) Color(0xFF4CAF50) else Color.Gray
                )
            }

            Text(
                text = quantity.toString(),
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black,
                modifier = Modifier.padding(horizontal = 16.dp)
            )

            IconButton(
                onClick = { onQuantityChange(quantity + 1) }
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Increase",
                    tint = Color(0xFF4CAF50)
                )
            }
        }
    }
}

@Composable
fun ProductDescriptionSection(
    description: String
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Text(
            text = "Description",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = description,
            fontSize = 16.sp,
            color = Color.Gray,
            lineHeight = 24.sp
        )
    }
}

@Composable
fun RelatedProductsSection(
    products: List<Product>,
    onProductClick: (Product) -> Unit
) {
    if (products.isNotEmpty()) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                text = "Related Products",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )

            Spacer(modifier = Modifier.height(16.dp))

            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(products) { product ->
                    RelatedProductCard(
                        product = product,
                        onClick = { onProductClick(product) }
                    )
                }
            }
        }
    }
}

@Composable
fun RelatedProductCard(
    product: Product,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .width(140.dp)
            .clickable { onClick() },
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(
            modifier = Modifier.padding(8.dp)
        ) {
            AsyncImage(
                model = product.imageUrl,
                contentDescription = product.name,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(80.dp)
                    .clip(RoundedCornerShape(8.dp)),
                contentScale = ContentScale.Crop,
                placeholder = painterResource(android.R.drawable.ic_menu_gallery),
                error = painterResource(android.R.drawable.ic_menu_gallery)
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = product.name,
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                color = Color.Black
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = "₹${product.price.toInt()}",
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF4CAF50)
            )
        }
    }
}

@Composable
fun BottomActionButtons(
    product: Product,
    quantity: Int,
    onAddToCart: () -> Unit,
    onBuyNow: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .padding(bottom = 80.dp), // Space above the app's bottom nav bar
        color = Color.White,
        shadowElevation = 8.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            OutlinedButton(
                onClick = onAddToCart,
                modifier = Modifier.weight(1f),
                shape = RoundedCornerShape(8.dp),
                border = ButtonDefaults.outlinedButtonBorder.copy(
                    width = 2.dp,
                    brush = androidx.compose.ui.graphics.SolidColor(Color(0xFF4CAF50))
                )
            ) {
                Text(
                    text = "Add to Cart",
                    color = Color(0xFF4CAF50),
                    fontWeight = FontWeight.Bold
                )
            }

            Button(
                onClick = onBuyNow,
                modifier = Modifier.weight(1f),
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF4CAF50)
                )
            ) {
                Text(
                    text = "Buy Now",
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}