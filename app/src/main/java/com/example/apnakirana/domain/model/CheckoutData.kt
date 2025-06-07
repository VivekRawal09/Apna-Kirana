package com.example.apnakirana.domain.model

import com.example.apnakirana.data.local.entity.Address
import com.example.apnakirana.data.local.entity.PaymentMethod

data class CheckoutData(
    val cartItems: List<CartItemWithProduct>,
    val selectedAddress: Address,
    val selectedPaymentMethod: PaymentMethod,
    val subtotal: Double,
    val deliveryFee: Double,
    val discount: Double,
    val totalAmount: Double,
    val orderNotes: String = ""
)