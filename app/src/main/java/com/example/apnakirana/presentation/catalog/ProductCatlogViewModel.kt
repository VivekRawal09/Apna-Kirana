package com.example.apnakirana.presentation.catalog

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.apnakirana.data.local.entity.Product
import com.example.apnakirana.data.repository.SampleDataRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductCatalogViewModel @Inject constructor(
    private val sampleDataRepository: SampleDataRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val categoryId: String = savedStateHandle.get<String>("categoryId") ?: ""
    private val categoryName: String = savedStateHandle.get<String>("categoryName") ?: ""

    private val _uiState = MutableStateFlow(ProductCatalogUiState())
    val uiState: StateFlow<ProductCatalogUiState> = _uiState.asStateFlow()

    private var allProducts: List<Product> = emptyList()

    init {
        loadProducts()
    }

    private fun loadProducts() {
        viewModelScope.launch {
            try {
                _uiState.value = _uiState.value.copy(
                    isLoading = true,
                    categoryName = categoryName
                )

                if (categoryId.isNotEmpty()) {
                    // Load products for specific category
                    sampleDataRepository.getProductsByCategory(categoryId).collect { products ->
                        allProducts = products
                        _uiState.value = _uiState.value.copy(
                            products = products,
                            filteredProducts = products,
                            isLoading = false
                        )
                    }
                } else {
                    // Load all products
                    sampleDataRepository.getAllProducts().collect { products ->
                        allProducts = products
                        _uiState.value = _uiState.value.copy(
                            products = products,
                            filteredProducts = products,
                            isLoading = false,
                            categoryName = "All Products"
                        )
                    }
                }
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    errorMessage = e.message
                )
            }
        }
    }

    fun onSearchQueryChange(query: String) {
        _uiState.value = _uiState.value.copy(searchQuery = query)
        filterProducts(query, _uiState.value.selectedSortOption)
    }

    fun onSortOptionChange(sortOption: SortOption) {
        _uiState.value = _uiState.value.copy(selectedSortOption = sortOption)
        filterProducts(_uiState.value.searchQuery, sortOption)
    }

    private fun filterProducts(query: String, sortOption: SortOption) {
        var filteredProducts = if (query.isBlank()) {
            allProducts
        } else {
            allProducts.filter { product ->
                product.name.contains(query, ignoreCase = true) ||
                        product.description.contains(query, ignoreCase = true)
            }
        }

        // Apply sorting
        filteredProducts = when (sortOption) {
            SortOption.NAME_ASC -> filteredProducts.sortedBy { it.name }
            SortOption.NAME_DESC -> filteredProducts.sortedByDescending { it.name }
            SortOption.PRICE_LOW_HIGH -> filteredProducts.sortedBy { it.price }
            SortOption.PRICE_HIGH_LOW -> filteredProducts.sortedByDescending { it.price }
            SortOption.RATING -> filteredProducts.sortedByDescending { it.rating }
            SortOption.DISCOUNT -> filteredProducts.sortedByDescending { it.discount }
        }

        _uiState.value = _uiState.value.copy(filteredProducts = filteredProducts)
    }

    fun onProductClick(product: Product) {
        // TODO: Navigate to product detail
    }

    fun onAddToCartClick(product: Product) {
        // TODO: Add to cart functionality
        // For now, just show a simple feedback
    }

    fun clearError() {
        _uiState.value = _uiState.value.copy(errorMessage = null)
    }
}
data class ProductCatalogUiState(
    val isLoading: Boolean = false,
    val categoryName: String = "",
    val products: List<Product> = emptyList(),
    val filteredProducts: List<Product> = emptyList(),
    val searchQuery: String = "",
    val selectedSortOption: SortOption = SortOption.NAME_ASC,
    val errorMessage: String? = null
)

enum class SortOption(val displayName: String) {
    NAME_ASC("Name A-Z"),
    NAME_DESC("Name Z-A"),
    PRICE_LOW_HIGH("Price: Low to High"),
    PRICE_HIGH_LOW("Price: High to Low"),
    RATING("Rating"),
    DISCOUNT("Discount")
}