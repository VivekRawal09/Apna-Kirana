package com.example.apnakirana.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "products")
data class Product(
    @PrimaryKey
    val id: String,
    val name: String,
    val description: String,
    val price: Double,
    val originalPrice: Double? = null,
    val imageUrl: String,
    val category: String,
    val unit: String, // kg, piece, liter etc
    val isInStock: Boolean = true,
    val rating: Float = 0f,
    val discount: Int = 0 // percentage
)