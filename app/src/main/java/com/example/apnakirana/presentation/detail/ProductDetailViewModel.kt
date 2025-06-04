package com.example.apnakirana.presentation.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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
class ProductDetailViewModel @Inject constructor(
    private val sampleDataRepository: SampleDataRepository,
    private val cartRepository: CartRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val productId: String = savedStateHandle.get<String>("productId") ?: ""

    private val _uiState = MutableStateFlow(ProductDetailUiState())
    val uiState: StateFlow<ProductDetailUiState> = _uiState.asStateFlow()

    init {
        loadProductDetail()
    }

    private fun loadProductDetail() {
        viewModelScope.launch {
            try {
                _uiState.value = _uiState.value.copy(isLoading = true)

                val product = sampleDataRepository.getProductById(productId)
                if (product != null) {
                    _uiState.value = _uiState.value.copy(
                        product = product,
                        isLoading = false
                    )
                    loadRelatedProducts(product.category)
                } else {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        errorMessage = "Product not found"
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

    private fun loadRelatedProducts(category: String) {
        viewModelScope.launch {
            sampleDataRepository.getProductsByCategory(category).collect { products ->
                val relatedProducts = products.filter { it.id != productId }.take(4)
                _uiState.value = _uiState.value.copy(relatedProducts = relatedProducts)
            }
        }
    }

    fun onQuantityChange(newQuantity: Int) {
        _uiState.value = _uiState.value.copy(quantity = newQuantity)
    }

    fun onAddToCart() {
        val currentState = _uiState.value
        if (currentState.product != null && currentState.quantity > 0) {
            viewModelScope.launch {
                cartRepository.addToCart(currentState.product.id, currentState.quantity)
                _uiState.value = _uiState.value.copy(
                    isAddedToCart = true,
                    showAddToCartMessage = true
                )
            }
        }
    }

    fun onBuyNow() {
        // TODO: Implement buy now functionality
    }

    fun onRelatedProductClick(product: Product) {
        // TODO: Navigate to related product detail
    }

    fun clearAddToCartMessage() {
        _uiState.value = _uiState.value.copy(showAddToCartMessage = false)
    }

    fun clearError() {
        _uiState.value = _uiState.value.copy(errorMessage = null)
    }
}

data class ProductDetailUiState(
    val isLoading: Boolean = false,
    val product: Product? = null,
    val relatedProducts: List<Product> = emptyList(),
    val quantity: Int = 1,
    val isAddedToCart: Boolean = false,
    val showAddToCartMessage: Boolean = false,
    val errorMessage: String? = null
)