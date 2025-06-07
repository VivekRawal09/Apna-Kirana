package com.example.apnakirana.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "orders")
data class Order(
    @PrimaryKey
    val orderId: String,
    val userId: String = "default_user",
    val orderDate: Long = System.currentTimeMillis(),
    val status: String = "PLACED",
    val subtotal: Double,
    val deliveryFee: Double,
    val discount: Double = 0.0,
    val totalAmount: Double,
    val deliveryAddressId: String,
    val paymentMethod: String,
    val paymentStatus: String = "PENDING",
    val estimatedDeliveryDate: Long = System.currentTimeMillis() + (24 * 60 * 60 * 1000),
    val actualDeliveryDate: Long? = null,
    val orderNotes: String = ""
)