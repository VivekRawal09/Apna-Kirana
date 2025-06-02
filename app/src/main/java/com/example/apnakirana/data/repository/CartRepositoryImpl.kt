package com.example.apnakirana.data.repository

import com.example.apnakirana.data.local.dao.CartDao
import com.example.apnakirana.data.local.entity.CartItem
import com.example.apnakirana.domain.repository.CartRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CartRepositoryImpl @Inject constructor(
    private val cartDao: CartDao
) : CartRepository {

    override fun getAllCartItems(): Flow<List<CartItem>> {
        return cartDao.getAllCartItems()
    }

    override fun getCartItemCount(): Flow<Int> {
        return cartDao.getCartItemCount()
    }

    override fun getTotalQuantity(): Flow<Int> {
        return cartDao.getTotalQuantity()
    }

    override suspend fun addToCart(productId: String, quantity: Int) {
        cartDao.insertCartItem(CartItem(productId, quantity))
    }

    override suspend fun updateCartItemQuantity(productId: String, quantity: Int) {
        if (quantity > 0) {
            cartDao.updateCartItemQuantity(productId, quantity)
        } else {
            cartDao.removeCartItem(productId)
        }
    }

    override suspend fun removeFromCart(productId: String) {
        cartDao.removeCartItem(productId)
    }

    override suspend fun clearCart() {
        cartDao.clearCart()
    }
}
