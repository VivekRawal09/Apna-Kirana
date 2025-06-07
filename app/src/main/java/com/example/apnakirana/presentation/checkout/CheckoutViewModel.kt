package com.example.apnakirana.presentation.checkout

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.apnakirana.data.local.entity.Address
import com.example.apnakirana.data.local.entity.PaymentMethod
import com.example.apnakirana.domain.model.CheckoutData
import com.example.apnakirana.domain.model.CheckoutUiState
import com.example.apnakirana.domain.repository.CartRepository
import com.example.apnakirana.domain.repository.OrderRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CheckoutViewModel @Inject constructor(
    private val cartRepository: CartRepository,
    private val orderRepository: OrderRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(CheckoutUiState())
    val uiState: StateFlow<CheckoutUiState> = _uiState.asStateFlow()

    private val _orderCreated = MutableStateFlow<String?>(null)
    val orderCreated: StateFlow<String?> = _orderCreated.asStateFlow()

    init {
        loadCheckoutData()
    }

    private fun loadCheckoutData() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)

            try {
                // Combine cart items and addresses
                combine(
                    cartRepository.getCartItems(),
                    orderRepository.getAddresses()
                ) { cartItems, addresses ->
                    val subtotal = cartItems.sumOf { it.totalPrice }
                    val savings = cartItems.sumOf { it.totalSavings }
                    val deliveryFee = if (subtotal > 499) 0.0 else 49.0 // Free delivery above ₹499

                    // Load payment methods
                    val paymentMethods = orderRepository.getPaymentMethods()

                    // Get default address
                    val defaultAddress = orderRepository.getDefaultAddress()
                    val selectedAddress = defaultAddress ?: addresses.firstOrNull()

                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        cartItems = cartItems,
                        addresses = addresses,
                        selectedAddress = selectedAddress,
                        paymentMethods = paymentMethods,
                        selectedPaymentMethod = paymentMethods.firstOrNull { it.id == "cod" }, // Default to COD
                        subtotal = subtotal,
                        deliveryFee = deliveryFee,
                        savings = savings
                    )
                }.collect {}

            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    errorMessage = e.message
                )
            }
        }
    }

    fun selectAddress(address: Address) {
        _uiState.value = _uiState.value.copy(selectedAddress = address)
    }

    fun selectPaymentMethod(paymentMethod: PaymentMethod) {
        _uiState.value = _uiState.value.copy(selectedPaymentMethod = paymentMethod)
    }

    fun updateOrderNotes(notes: String) {
        _uiState.value = _uiState.value.copy(orderNotes = notes)
    }

    fun placeOrder() {
        val currentState = _uiState.value

        println("🔍 DEBUG: Place order button clicked!")
        println("🛒 DEBUG: Cart items: ${currentState.cartItems.size}")
        println("🏠 DEBUG: Selected address: ${currentState.selectedAddress?.name ?: "NONE"}")
        println("💳 DEBUG: Selected payment: ${currentState.selectedPaymentMethod?.name ?: "NONE"}")
        println("✅ DEBUG: Can place order: ${currentState.canPlaceOrder}")
        println("🔄 DEBUG: Is placing order: ${currentState.isPlacingOrder}")

        if (!currentState.canPlaceOrder) {
            val errorMsg = buildString {
                append("Cannot place order: ")
                if (currentState.cartItems.isEmpty()) append("No items in cart. ")
                if (currentState.selectedAddress == null) append("No address selected. ")
                if (currentState.selectedPaymentMethod == null) append("No payment method selected. ")
                if (currentState.isPlacingOrder) append("Already placing order. ")
            }

            println("❌ DEBUG: $errorMsg")
            _uiState.value = currentState.copy(
                errorMessage = errorMsg
            )
            return
        }

        println("🚀 DEBUG: Starting order placement...")

        viewModelScope.launch {
            _uiState.value = currentState.copy(isPlacingOrder = true, errorMessage = null)

            try {
                println("📦 DEBUG: Creating checkout data...")

                val checkoutData = CheckoutData(
                    cartItems = currentState.cartItems,
                    selectedAddress = currentState.selectedAddress!!,
                    selectedPaymentMethod = currentState.selectedPaymentMethod!!,
                    subtotal = currentState.subtotal,
                    deliveryFee = currentState.deliveryFee,
                    discount = currentState.discount,
                    totalAmount = currentState.totalAmount,
                    orderNotes = currentState.orderNotes
                )

                println("💾 DEBUG: Calling repository to create order...")
                val result = orderRepository.createOrder(checkoutData)

                result.fold(
                    onSuccess = { orderId ->
                        println("✅ DEBUG: Order created successfully! ID: $orderId")

                        // Clear cart after successful order
                        println("🧹 DEBUG: Clearing cart...")
                        cartRepository.clearCart()

                        println("📱 DEBUG: Setting order created state...")
                        _orderCreated.value = orderId
                        _uiState.value = currentState.copy(isPlacingOrder = false)

                        println("🎉 DEBUG: Order placement complete!")
                    },
                    onFailure = { error ->
                        println("❌ DEBUG: Order creation failed: ${error.message}")
                        error.printStackTrace()
                        _uiState.value = currentState.copy(
                            isPlacingOrder = false,
                            errorMessage = error.message ?: "Failed to place order"
                        )
                    }
                )

            } catch (e: Exception) {
                println("💥 DEBUG: Exception during order placement: ${e.message}")
                e.printStackTrace()
                _uiState.value = currentState.copy(
                    isPlacingOrder = false,
                    errorMessage = e.message ?: "Failed to place order"
                )
            }
        }
    }

    fun addNewAddress(address: Address) {
        viewModelScope.launch {
            try {
                orderRepository.addAddress(address)
                // The UI will automatically update through the Flow
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    errorMessage = e.message ?: "Failed to add address"
                )
            }
        }
    }

    fun setDefaultAddress(addressId: String) {
        viewModelScope.launch {
            try {
                orderRepository.setDefaultAddress(addressId)
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    errorMessage = e.message ?: "Failed to set default address"
                )
            }
        }
    }

    fun clearError() {
        _uiState.value = _uiState.value.copy(errorMessage = null)
    }

    fun resetOrderCreated() {
        _orderCreated.value = null
    }
}