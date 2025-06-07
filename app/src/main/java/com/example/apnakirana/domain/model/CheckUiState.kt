package com.example.apnakirana.domain.model

import com.example.apnakirana.data.local.entity.Address
import com.example.apnakirana.data.local.entity.PaymentMethod

data class CheckoutUiState(
    val isLoading: Boolean = false,
    val cartItems: List<CartItemWithProduct> = emptyList(),
    val addresses: List<Address> = emptyList(),
    val selectedAddress: Address? = null,
    val paymentMethods: List<PaymentMethod> = emptyList(),
    val selectedPaymentMethod: PaymentMethod? = null,
    val subtotal: Double = 0.0,
    val deliveryFee: Double = 49.0,
    val platformFee: Double = 5.0,
    val discount: Double = 0.0,
    val savings: Double = 0.0,
    val orderNotes: String = "",
    val isPlacingOrder: Boolean = false,
    val errorMessage: String? = null
) {
    val totalAmount: Double
        get() = subtotal + deliveryFee + platformFee - discount

    val canPlaceOrder: Boolean
        get() = cartItems.isNotEmpty() &&
                selectedAddress != null &&
                selectedPaymentMethod != null &&
                !isPlacingOrder
}