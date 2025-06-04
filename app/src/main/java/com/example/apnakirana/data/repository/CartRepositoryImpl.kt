package com.example.apnakirana.data.repository

import com.example.apnakirana.data.local.entity.CartItem
import com.example.apnakirana.data.repository.SampleDataRepository
import com.example.apnakirana.domain.model.CartItemWithProduct
import com.example.apnakirana.domain.repository.CartRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CartRepositoryImpl @Inject constructor(
    private val sampleDataRepository: SampleDataRepository
) : CartRepository {

    // In-memory cart storage (in real app, this would use Room database)
    private val _cartItems = MutableStateFlow<Map<String, Int>>(emptyMap())

    override fun getCartItems(): Flow<List<CartItemWithProduct>> {
        return combine(
            _cartItems,
            sampleDataRepository.getAllProducts()
        ) { cartItems, products ->
            cartItems.mapNotNull { (productId, quantity) ->
                products.find { it.id == productId }?.let { product ->
                    CartItemWithProduct(
                        product = product,
                        quantity = quantity
                    )
                }
            }.sortedByDescending { it.addedAt }
        }
    }

    override fun getCartItemCount(): Flow<Int> {
        return _cartItems.map { it.size }
    }

    override fun getTotalQuantity(): Flow<Int> {
        return _cartItems.map { cartItems ->
            cartItems.values.sum()
        }
    }

    override fun getCartTotal(): Flow<Double> {
        return getCartItems().map { items ->
            items.sumOf { it.totalPrice }
        }
    }

    override suspend fun addToCart(productId: String, quantity: Int) {
        val currentItems = _cartItems.value.toMutableMap()
        val existingQuantity = currentItems[productId] ?: 0
        currentItems[productId] = existingQuantity + quantity
        _cartItems.value = currentItems
    }

    override suspend fun updateCartItemQuantity(productId: String, quantity: Int) {
        val currentItems = _cartItems.value.toMutableMap()
        if (quantity > 0) {
            currentItems[productId] = quantity
        } else {
            currentItems.remove(productId)
        }
        _cartItems.value = currentItems
    }

    override suspend fun removeFromCart(productId: String) {
        val currentItems = _cartItems.value.toMutableMap()
        currentItems.remove(productId)
        _cartItems.value = currentItems
    }

    override suspend fun clearCart() {
        _cartItems.value = emptyMap()
    }
}