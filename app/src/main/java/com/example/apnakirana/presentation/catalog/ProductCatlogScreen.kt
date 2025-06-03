package com.example.apnakirana.presentation.catalog

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.example.apnakirana.data.local.entity.Product

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductCatalogScreen(
    onBackPress: () -> Unit,
    onProductClick: (Product) -> Unit,
    viewModel: ProductCatalogViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    var showSortDialog by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF5F5F5))
    ) {
        // Top Bar
        CatalogTopBar(
            categoryName = uiState.categoryName,
            onBackPress = onBackPress,
            onSortClick = { showSortDialog = true }
        )

        // Search Bar
        SearchSection(
            searchQuery = uiState.searchQuery,
            onSearchQueryChange = viewModel::onSearchQueryChange
        )

        // Products Content
        if (uiState.isLoading) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else if (uiState.filteredProducts.isEmpty()) {
            EmptyProductsState(
                searchQuery = uiState.searchQuery,
                categoryName = uiState.categoryName
            )
        } else {
            ProductGrid(
                products = uiState.filteredProducts,
                onProductClick = { product ->
                    onProductClick(product)
                    viewModel.onProductClick(product)
                },
                onAddToCartClick = viewModel::onAddToCartClick
            )
        }
    }

    // Sort Dialog
    if (showSortDialog) {
        SortDialog(
            selectedOption = uiState.selectedSortOption,
            onSortOptionSelected = { option ->
                viewModel.onSortOptionChange(option)
                showSortDialog = false
            },
            onDismiss = { showSortDialog = false }
        )
    }

    // Error handling
    uiState.errorMessage?.let { error ->
        LaunchedEffect(error) {
            viewModel.clearError()
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CatalogTopBar(
    categoryName: String,
    onBackPress: () -> Unit,
    onSortClick: () -> Unit
) {
    TopAppBar(
        title = {
            Text(
                text = categoryName,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
        },
        navigationIcon = {
            IconButton(onClick = onBackPress) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Back"
                )
            }
        },
        actions = {
            IconButton(onClick = onSortClick) {
                Icon(
                    imageVector = Icons.Default.Refresh,
                    contentDescription = "Sort"
                )
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Color.White
        )
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchSection(
    searchQuery: String,
    onSearchQueryChange: (String) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = "Search",
                tint = Color.Gray,
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.width(12.dp))

            OutlinedTextField(
                value = searchQuery,
                onValueChange = onSearchQueryChange,
                placeholder = { Text("Search products...") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color.Transparent,
                    unfocusedBorderColor = Color.Transparent
                )
            )
        }
    }
}

@Composable
fun ProductGrid(
    products: List<Product>,
    onProductClick: (Product) -> Unit,
    onAddToCartClick: (Product) -> Unit
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        contentPadding = PaddingValues(bottom = 16.dp)
    ) {
        items(products) { product ->
            ProductGridItem(
                product = product,
                onClick = { onProductClick(product) },
                onAddToCartClick = { onAddToCartClick(product) }
            )
        }
    }
}

@Composable
fun ProductGridItem(
    product: Product,
    onClick: () -> Unit,
    onAddToCartClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(
            modifier = Modifier.padding(12.dp)
        ) {
            // Product Image with Discount Badge
            Box {
                AsyncImage(
                    model = product.imageUrl,
                    contentDescription = product.name,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(120.dp)
                        .clip(RoundedCornerShape(8.dp)),
                    contentScale = ContentScale.Crop,
                    placeholder = painterResource(android.R.drawable.ic_menu_gallery),
                    error = painterResource(android.R.drawable.ic_menu_gallery)
                )

                if (product.discount > 0) {
                    Surface(
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .padding(4.dp),
                        color = Color.Red,
                        shape = RoundedCornerShape(6.dp)
                    ) {
                        Text(
                            text = "${product.discount}% OFF",
                            fontSize = 10.sp,
                            color = Color.White,
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Product Name
            Text(
                text = product.name,
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                color = Color.Black
            )

            Spacer(modifier = Modifier.height(4.dp))

            // Unit and Rating
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = product.unit,
                    fontSize = 12.sp,
                    color = Color.Gray
                )

                if (product.rating > 0) {
                    Text(
                        text = "⭐ ${product.rating}",
                        fontSize = 12.sp,
                        color = Color.Gray
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Price and Add Button
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "₹${product.price.toInt()}",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF4CAF50)
                    )

                    product.originalPrice?.let { originalPrice ->
                        Text(
                            text = "₹${originalPrice.toInt()}",
                            fontSize = 12.sp,
                            color = Color.Gray,
                            style = androidx.compose.ui.text.TextStyle(
                                textDecoration = androidx.compose.ui.text.style.TextDecoration.LineThrough
                            )
                        )
                    }
                }

                Button(
                    onClick = onAddToCartClick,
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF4CAF50)
                    ),
                    modifier = Modifier.height(32.dp)
                ) {
                    Text(
                        text = "ADD",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
            }
        }
    }
}

@Composable
fun EmptyProductsState(
    searchQuery: String,
    categoryName: String
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "🔍",
            fontSize = 64.sp
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = if (searchQuery.isNotEmpty()) {
                "No products found for \"$searchQuery\""
            } else {
                "No products available in $categoryName"
            },
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = if (searchQuery.isNotEmpty()) {
                "Try different keywords or browse other categories"
            } else {
                "Check back later for new products"
            },
            fontSize = 14.sp,
            color = Color.Gray
        )
    }
}

@Composable
fun SortDialog(
    selectedOption: SortOption,
    onSortOptionSelected: (SortOption) -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = "Sort Products",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
        },
        text = {
            LazyColumn {
                items(SortOption.values()) { option ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onSortOptionSelected(option) }
                            .padding(vertical = 12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(
                            selected = selectedOption == option,
                            onClick = { onSortOptionSelected(option) }
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = option.displayName,
                            fontSize = 16.sp
                        )
                    }
                }
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text("Done")
            }
        }
    )
}