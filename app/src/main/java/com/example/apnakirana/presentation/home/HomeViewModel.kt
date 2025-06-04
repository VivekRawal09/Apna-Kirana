package com.example.apnakirana.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.apnakirana.data.local.entity.Category
import com.example.apnakirana.data.local.entity.Product
import com.example.apnakirana.data.repository.SampleDataRepository
import com.example.apnakirana.domain.repository.CartRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val sampleDataRepository: SampleDataRepository,
    private val cartRepository: CartRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    init {
        loadHomeData()
    }

    private fun loadHomeData() {
        viewModelScope.launch {
            try {
                _uiState.value = _uiState.value.copy(isLoading = true)

                // Load categories
                sampleDataRepository.getAllCategories().collect { categories ->
                    _uiState.value = _uiState.value.copy(categories = categories)
                }

                // Load featured products
                sampleDataRepository.getFeaturedProducts().collect { featuredProducts ->
                    _uiState.value = _uiState.value.copy(
                        featuredProducts = featuredProducts,
                        isLoading = false
                    )
                }
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    errorMessage = e.message
                )
            }
        }
    }

    fun onCategoryClick(category: Category) {
        // TODO: Navigate to category products screen
    }

    fun onProductClick(product: Product) {
        // TODO: Navigate to product detail screen
    }

    fun onAddToCartClick(product: Product) {
        viewModelScope.launch {
            cartRepository.addToCart(product.id, 1)
        }
    }

    fun onSearchClick() {
        // TODO: Navigate to search screen
    }

    fun clearError() {
        _uiState.value = _uiState.value.copy(errorMessage = null)
    }
}

data class HomeUiState(
    val isLoading: Boolean = false,
    val categories: List<Category> = emptyList(),
    val featuredProducts: List<Product> = emptyList(),
    val errorMessage: String? = null
)