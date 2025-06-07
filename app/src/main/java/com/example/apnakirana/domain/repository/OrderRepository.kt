package com.example.apnakirana.domain.repository

import com.example.apnakirana.data.local.entity.Address
import com.example.apnakirana.data.local.entity.Order
import com.example.apnakirana.data.local.entity.OrderItem
import com.example.apnakirana.data.local.entity.PaymentMethod
import com.example.apnakirana.domain.model.CheckoutData
import kotlinx.coroutines.flow.Flow

interface OrderRepository {
    // Order operations
    suspend fun createOrder(checkoutData: CheckoutData): Result<String>
    suspend fun getOrderById(orderId: String): Order?
    fun getOrderHistory(): Flow<List<Order>>
    suspend fun updateOrderStatus(orderId: String, status: String)

    // Order items
    suspend fun getOrderItems(orderId: String): List<OrderItem>

    // Address operations
    suspend fun addAddress(address: Address)
    fun getAddresses(): Flow<List<Address>>
    suspend fun getDefaultAddress(): Address?
    suspend fun setDefaultAddress(addressId: String)
    suspend fun deleteAddress(addressId: String)

    // Payment methods
    fun getPaymentMethods(): List<PaymentMethod>

    // Statistics
    suspend fun getTotalOrdersCount(): Int
    suspend fun getTotalSpent(): Double
}