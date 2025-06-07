package com.example.apnakirana.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "order_items")
data class OrderItem(
    @PrimaryKey
    val orderItemId: String,
    val orderId: String, // ✅ No foreign key constraint
    val productId: String, // ✅ No foreign key constraint
    val productName: String, // Snapshot of product name at time of order
    val productPrice: Double, // Snapshot of product price at time of order
    val quantity: Int,
    val totalPrice: Double // productPrice * quantity
)