package com.example.apnakirana.presentation.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.apnakirana.data.local.entity.Category
import com.example.apnakirana.data.local.entity.Product
import com.example.apnakirana.data.repository.SampleDataRepository
import com.example.apnakirana.domain.repository.CartRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import javax.inject.Inject

@OptIn(FlowPreview::class)
@HiltViewModel
class SearchViewModel @Inject constructor(
    private val sampleDataRepository: SampleDataRepository,
    private val cartRepository: CartRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(SearchUiState())
    val uiState: StateFlow<SearchUiState> = _uiState.asStateFlow()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private var allProducts: List<Product> = emptyList()
    private var allCategories: List<Category> = emptyList()

    init {
        loadInitialData()
        setupSearchListener()
    }

    private fun loadInitialData() {
        viewModelScope.launch {
            try {
                // Load all products for search
                sampleDataRepository.getAllProducts().collect { products ->
                    allProducts = products
                    if (_searchQuery.value.isBlank()) {
                        _uiState.value = _uiState.value.copy(
                            searchResults = products,
                            isLoading = false
                        )
                    }
                }

                // Load categories for filtering
                sampleDataRepository.getAllCategories().collect { categories ->
                    allCategories = categories
                    _uiState.value = _uiState.value.copy(categories = categories)
                }
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    errorMessage = e.message
                )
            }
        }
    }

    private fun setupSearchListener() {
        viewModelScope.launch {
            _searchQuery
                .debounce(300) // Wait 300ms after user stops typing
                .distinctUntilChanged()
                .collect { query ->
                    performSearch(query)
                }
        }
    }

    fun updateSearchQuery(query: String) {
        _searchQuery.value = query
        if (query.isNotBlank()) {
            addToSearchHistory(query)
        }
    }

    private fun performSearch(query: String) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)

            try {
                val filteredProducts = if (query.isBlank()) {
                    allProducts
                } else {
                    allProducts.filter { product ->
                        product.name.contains(query, ignoreCase = true) ||
                                product.description.contains(query, ignoreCase = true) ||
                                product.category.contains(query, ignoreCase = true)
                    }
                }

                val finalResults = applyFilters(filteredProducts)

                _uiState.value = _uiState.value.copy(
                    searchResults = finalResults,
                    isLoading = false,
                    showEmptyState = finalResults.isEmpty() && query.isNotBlank()
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    errorMessage = e.message
                )
            }
        }
    }

    private fun applyFilters(products: List<Product>): List<Product> {
        var filteredProducts = products

        // Apply category filter
        val currentState = _uiState.value
        if (currentState.selectedCategory != null) {
            filteredProducts = filteredProducts.filter {
                it.category == currentState.selectedCategory
            }
        }

        // Apply sorting
        filteredProducts = when(currentState.sortOption) {
            SortOption.NAME_ASC -> filteredProducts.sortedBy { it.name }
            SortOption.NAME_DESC -> filteredProducts.sortedByDescending { it.name }
            SortOption.PRICE_LOW_TO_HIGH -> filteredProducts.sortedBy { it.price }
            SortOption.PRICE_HIGH_TO_LOW -> filteredProducts.sortedByDescending { it.price }
            SortOption.RATING -> filteredProducts.sortedByDescending { it.rating }
            SortOption.DISCOUNT -> filteredProducts.sortedByDescending { it.discount }
        }

        return filteredProducts
    }

    fun selectCategory(category: String?) {
        _uiState.value = _uiState.value.copy(selectedCategory = category)
        performSearch(_searchQuery.value)
    }

    fun updateSortOption(sortOption: SortOption) {
        _uiState.value = _uiState.value.copy(sortOption = sortOption)
        performSearch(_searchQuery.value)
    }

    fun clearSearch() {
        _searchQuery.value = ""
        _uiState.value = _uiState.value.copy(
            selectedCategory = null,
            showEmptyState = false
        )
    }

    fun onAddToCartClick(product: Product) {
        viewModelScope.launch {
            cartRepository.addToCart(product.id, 1)
        }
    }

    private fun addToSearchHistory(query: String) {
        val currentHistory = _uiState.value.searchHistory.toMutableList()

        // Remove if already exists
        currentHistory.remove(query)

        // Add to beginning
        currentHistory.add(0, query)

        // Keep only last 10 searches
        if (currentHistory.size > 10) {
            currentHistory.removeAt(currentHistory.size - 1)
        }

        _uiState.value = _uiState.value.copy(searchHistory = currentHistory)
    }

    fun selectFromHistory(query: String) {
        updateSearchQuery(query)
    }

    fun clearSearchHistory() {
        _uiState.value = _uiState.value.copy(searchHistory = emptyList())
    }

    fun clearError() {
        _uiState.value = _uiState.value.copy(errorMessage = null)
    }
}

data class SearchUiState(
    val isLoading: Boolean = true,
    val searchResults: List<Product> = emptyList(),
    val categories: List<Category> = emptyList(),
    val selectedCategory: String? = null,
    val sortOption: SortOption = SortOption.NAME_ASC,
    val searchHistory: List<String> = emptyList(),
    val showEmptyState: Boolean = false,
    val errorMessage: String? = null
)

enum class SortOption(val displayName: String) {
    NAME_ASC("Name A-Z"),
    NAME_DESC("Name Z-A"),
    PRICE_LOW_TO_HIGH("Price Low to High"),
    PRICE_HIGH_TO_LOW("Price High to Low"),
    RATING("Rating"),
    DISCOUNT("Discount")
}