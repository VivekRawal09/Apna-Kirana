package com.example.apnakirana.domain.repository

import com.example.apnakirana.domain.model.CartItemWithProduct
import kotlinx.coroutines.flow.Flow

interface CartRepository {
    fun getCartItems(): Flow<List<CartItemWithProduct>>
    fun getCartItemCount(): Flow<Int>
    fun getTotalQuantity(): Flow<Int>
    fun getCartTotal(): Flow<Double>
    suspend fun addToCart(productId: String, quantity: Int = 1)
    suspend fun updateCartItemQuantity(productId: String, quantity: Int)
    suspend fun removeFromCart(productId: String)
    suspend fun clearCart()
}