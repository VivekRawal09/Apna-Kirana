package com.example.apnakirana.presentation.cart

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.apnakirana.domain.model.CartItemWithProduct
import com.example.apnakirana.domain.repository.CartRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CartViewModel @Inject constructor(
    private val cartRepository: CartRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(CartUiState())
    val uiState: StateFlow<CartUiState> = _uiState.asStateFlow()

    init {
        observeCartData()
    }

    private fun observeCartData() {
        viewModelScope.launch {
            combine(
                cartRepository.getCartItems(),
                cartRepository.getCartTotal(),
                cartRepository.getTotalQuantity()
            ) { cartItems, total, totalQuantity ->
                CartUiState(
                    cartItems = cartItems,
                    totalAmount = total,
                    totalQuantity = totalQuantity,
                    deliveryFee = if (cartItems.isNotEmpty()) 20.0 else 0.0,
                    isLoading = false
                )
            }.collect { newState ->
                _uiState.value = newState
            }
        }
    }

    fun updateQuantity(productId: String, quantity: Int) {
        viewModelScope.launch {
            cartRepository.updateCartItemQuantity(productId, quantity)
        }
    }

    fun removeItem(productId: String) {
        viewModelScope.launch {
            cartRepository.removeFromCart(productId)
        }
    }

    fun clearCart() {
        viewModelScope.launch {
            cartRepository.clearCart()
        }
    }

    fun proceedToCheckout() {
        // TODO: Navigate to checkout screen
    }
}

data class CartUiState(
    val isLoading: Boolean = true,
    val cartItems: List<CartItemWithProduct> = emptyList(),
    val totalAmount: Double = 0.0,
    val totalQuantity: Int = 0,
    val deliveryFee: Double = 0.0,
    val errorMessage: String? = null
) {
    val subtotal: Double
        get() = cartItems.sumOf { it.totalPrice }

    val totalSavings: Double
        get() = cartItems.sumOf { it.totalSavings }

    val finalTotal: Double
        get() = subtotal + deliveryFee

    val isEmpty: Boolean
        get() = cartItems.isEmpty()
}