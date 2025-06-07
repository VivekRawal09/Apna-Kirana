package com.example.apnakirana.data.local.entity

data class PaymentMethod(
    val id: String,
    val name: String,
    val description: String,
    val icon: String, // For icon reference
    val isEnabled: Boolean = true
) {
    companion object {
        fun getDefaultPaymentMethods(): List<PaymentMethod> = listOf(
            PaymentMethod(
                id = "cod",
                name = "Cash on Delivery",
                description = "Pay when your order arrives",
                icon = "cash"
            ),
            PaymentMethod(
                id = "upi",
                name = "UPI Payment",
                description = "Pay using Google Pay, PhonePe, Paytm",
                icon = "upi"
            ),
            PaymentMethod(
                id = "card",
                name = "Credit/Debit Card",
                description = "Visa, MasterCard, RuPay",
                icon = "card"
            )
        )
    }
}