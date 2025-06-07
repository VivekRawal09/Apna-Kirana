package com.example.apnakirana.presentation.search

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.res.painterResource
import coil.compose.AsyncImage
import com.example.apnakirana.data.local.entity.Product
import com.example.apnakirana.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(
    onProductClick: (Product) -> Unit,
    viewModel: SearchViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val searchQuery by viewModel.searchQuery.collectAsState()

    val focusRequester = remember { FocusRequester() }
    val keyboardController = LocalSoftwareKeyboardController.current
    var showSortBottomSheet by remember { mutableStateOf(false) }
    var showFilters by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        // Search Bar
        SearchBar(
            query = searchQuery,
            onQueryChange = viewModel::updateSearchQuery,
            onClearClick = viewModel::clearSearch,
            focusRequester = focusRequester,
            keyboardController = keyboardController
        )

        // Search History (shown when search is empty)
        if (searchQuery.isBlank() && uiState.searchHistory.isNotEmpty()) {
            SearchHistorySection(
                searchHistory = uiState.searchHistory,
                onHistoryClick = viewModel::selectFromHistory,
                onClearHistory = viewModel::clearSearchHistory
            )
        }

        // Filters and Sort Row
        if (searchQuery.isNotBlank() || uiState.selectedCategory != null) {
            FilterSortRow(
                selectedCategory = uiState.selectedCategory,
                sortOption = uiState.sortOption,
                onFiltersClick = { showFilters = !showFilters },
                onSortClick = { showSortBottomSheet = true }
            )
        }

        // Category Filters (expandable)
        if (showFilters) {
            CategoryFilters(
                categories = uiState.categories,
                selectedCategory = uiState.selectedCategory,
                onCategorySelect = viewModel::selectCategory
            )
        }

        // Search Results
        when {
            uiState.isLoading -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }

            uiState.showEmptyState -> {
                EmptySearchState(
                    query = searchQuery,
                    onClearSearch = viewModel::clearSearch
                )
            }

            else -> {
                SearchResults(
                    products = uiState.searchResults,
                    onProductClick = onProductClick,
                    onAddToCart = viewModel::onAddToCartClick
                )
            }
        }
    }

    // Sort Bottom Sheet
    if (showSortBottomSheet) {
        SortBottomSheet(
            currentSortOption = uiState.sortOption,
            onSortOptionSelect = { option ->
                viewModel.updateSortOption(option)
                showSortBottomSheet = false
            },
            onDismiss = { showSortBottomSheet = false }
        )
    }

    // Error handling
    uiState.errorMessage?.let { error ->
        LaunchedEffect(error) {
            // Show snackbar or handle error
            viewModel.clearError()
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SearchBar(
    query: String,
    onQueryChange: (String) -> Unit,
    onClearClick: () -> Unit,
    focusRequester: FocusRequester,
    keyboardController: androidx.compose.ui.platform.SoftwareKeyboardController?
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        OutlinedTextField(
            value = query,
            onValueChange = onQueryChange,
            modifier = Modifier
                .fillMaxWidth()
                .focusRequester(focusRequester),
            placeholder = { Text("Search for products...") },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "Search"
                )
            },
            trailingIcon = {
                if (query.isNotBlank()) {
                    IconButton(onClick = onClearClick) {
                        Icon(
                            imageVector = Icons.Default.Clear,
                            contentDescription = "Clear search"
                        )
                    }
                }
            },
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Search
            ),
            keyboardActions = KeyboardActions(
                onSearch = { keyboardController?.hide() }
            ),
            singleLine = true
        )
    }
}

@Composable
private fun SearchHistorySection(
    searchHistory: List<String>,
    onHistoryClick: (String) -> Unit,
    onClearHistory: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Recent Searches",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            TextButton(onClick = onClearHistory) {
                Text("Clear All")
            }
        }

        LazyColumn {
            items(searchHistory) { query ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onHistoryClick(query) }
                        .padding(vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.baseline_history_24),
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(
                        text = query,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
        }
    }
}

@Composable
private fun FilterSortRow(
    selectedCategory: String?,
    sortOption: SortOption,
    onFiltersClick: () -> Unit,
    onSortClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        // Filter Button
        FilterChip(
            onClick = onFiltersClick,
            label = {
                Text(
                    text = selectedCategory ?: "All Categories",
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            },
            selected = selectedCategory != null,
            leadingIcon = {
                Icon(
                    painter = painterResource(id = R.drawable.outline_filter_list_24),
                    contentDescription = "Filter",
                    modifier = Modifier.size(18.dp)
                )
            }
        )

        // Sort Button
        FilterChip(
            onClick = onSortClick,
            label = { Text("Sort: ${sortOption.displayName}") },
            selected = false,
            leadingIcon = {
                Icon(
                    painter = painterResource(id = R.drawable.outline_sort_24),
                    contentDescription = "Sort",
                    modifier = Modifier.size(18.dp)
                )
            }
        )
    }
}

@Composable
private fun CategoryFilters(
    categories: List<com.example.apnakirana.data.local.entity.Category>,
    selectedCategory: String?,
    onCategorySelect: (String?) -> Unit
) {
    LazyRow(
        modifier = Modifier.fillMaxWidth(),
        contentPadding = PaddingValues(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        // All Categories option
        item {
            FilterChip(
                onClick = { onCategorySelect(null) },
                label = { Text("All") },
                selected = selectedCategory == null
            )
        }

        items(categories) { category ->
            FilterChip(
                onClick = { onCategorySelect(category.name) },
                label = { Text(category.name) },
                selected = selectedCategory == category.name
            )
        }
    }
}

@Composable
private fun SearchResults(
    products: List<Product>,
    onProductClick: (Product) -> Unit,
    onAddToCart: (Product) -> Unit
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(products) { product ->
            ProductSearchCard(
                product = product,
                onProductClick = { onProductClick(product) },
                onAddToCart = { onAddToCart(product) }
            )
        }
    }
}

@Composable
private fun ProductSearchCard(
    product: Product,
    onProductClick: () -> Unit,
    onAddToCart: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onProductClick() },
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column {
            // Product Image
            AsyncImage(
                model = product.imageUrl,
                contentDescription = product.name,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp)
                    .clip(RoundedCornerShape(topStart = 8.dp, topEnd = 8.dp)),
                contentScale = ContentScale.Crop
            )

            Column(
                modifier = Modifier.padding(12.dp)
            ) {
                // Product Name
                Text(
                    text = product.name,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Medium,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(4.dp))

                // Price
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "₹${product.price}",
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )

                    product.originalPrice?.let { originalPrice ->
                        if (originalPrice > product.price) {
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "₹$originalPrice",
                                style = MaterialTheme.typography.bodySmall,
                                textDecoration = androidx.compose.ui.text.style.TextDecoration.LineThrough,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Add to Cart Button
                Button(
                    onClick = onAddToCart,
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary
                    )
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Add")
                }
            }
        }
    }
}

@Composable
private fun EmptySearchState(
    query: String,
    onClearSearch: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            painter = painterResource(id = R.drawable.outline_search_off_24),
            contentDescription = null,
            modifier = Modifier.size(80.dp),
            tint = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "No results found for \"$query\"",
            style = MaterialTheme.typography.titleMedium,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Try searching with different keywords or check spelling",
            style = MaterialTheme.typography.bodyMedium,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedButton(onClick = onClearSearch) {
            Text("Clear Search")
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SortBottomSheet(
    currentSortOption: SortOption,
    onSortOptionSelect: (SortOption) -> Unit,
    onDismiss: () -> Unit
) {
    val modalBottomSheetState = rememberModalBottomSheetState()

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = modalBottomSheetState
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                text = "Sort by",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            SortOption.values().forEach { option ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onSortOptionSelect(option) }
                        .padding(vertical = 12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    RadioButton(
                        selected = currentSortOption == option,
                        onClick = { onSortOptionSelect(option) }
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(
                        text = option.displayName,
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}