package com.example.apnakirana.data.local.dao

import androidx.room.*
import com.example.apnakirana.data.local.entity.Order
import com.example.apnakirana.data.local.entity.OrderItem
import kotlinx.coroutines.flow.Flow

@Dao
interface OrderDao {

    // Order operations
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrder(order: Order)

    @Query("SELECT * FROM orders WHERE userId = :userId ORDER BY orderDate DESC")
    fun getOrdersByUser(userId: String = "default_user"): Flow<List<Order>>

    @Query("SELECT * FROM orders WHERE orderId = :orderId")
    suspend fun getOrderById(orderId: String): Order?

    @Query("UPDATE orders SET status = :status WHERE orderId = :orderId")
    suspend fun updateOrderStatus(orderId: String, status: String)

    @Delete
    suspend fun deleteOrder(order: Order)

    // Order items operations
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrderItems(orderItems: List<OrderItem>)

    @Query("SELECT * FROM order_items WHERE orderId = :orderId")
    suspend fun getOrderItems(orderId: String): List<OrderItem>

    @Query("DELETE FROM order_items WHERE orderId = :orderId")
    suspend fun deleteOrderItems(orderId: String)

    // Statistics
    @Query("SELECT COUNT(*) FROM orders WHERE userId = :userId")
    suspend fun getTotalOrdersCount(userId: String = "default_user"): Int

    @Query("SELECT SUM(totalAmount) FROM orders WHERE userId = :userId AND status != 'CANCELLED'")
    suspend fun getTotalSpent(userId: String = "default_user"): Double?
}