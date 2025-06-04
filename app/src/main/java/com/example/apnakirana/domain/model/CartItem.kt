package com.example.apnakirana.domain.model

import com.example.apnakirana.data.local.entity.Product

data class CartItemWithProduct(
    val product: Product,
    val quantity: Int,
    val addedAt: Long = System.currentTimeMillis()
) {
    val totalPrice: Double
        get() = product.price * quantity

    val totalOriginalPrice: Double?
        get() = product.originalPrice?.let { it * quantity }

    val totalSavings: Double
        get() = totalOriginalPrice?.let { it - totalPrice } ?: 0.0
}