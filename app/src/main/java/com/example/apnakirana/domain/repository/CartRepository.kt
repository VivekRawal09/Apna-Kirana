package com.example.apnakirana.domain.repository

import com.example.apnakirana.data.local.entity.CartItem
import kotlinx.coroutines.flow.Flow

interface CartRepository {
    fun getAllCartItems(): Flow<List<CartItem>>
    fun getCartItemCount(): Flow<Int>
    fun getTotalQuantity(): Flow<Int>
    suspend fun addToCart(productId: String, quantity: Int)
    suspend fun updateCartItemQuantity(productId: String, quantity: Int)
    suspend fun removeFromCart(productId: String)
    suspend fun clearCart()
}