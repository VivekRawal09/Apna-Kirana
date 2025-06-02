package com.example.apnakirana.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "cart_items")
data class CartItem(
    @PrimaryKey
    val productId: String,
    val quantity: Int,
    val addedAt: Long = System.currentTimeMillis()
)