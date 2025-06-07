package com.example.apnakirana.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "addresses")
data class Address(
    @PrimaryKey
    val addressId: String,
    val userId: String = "default_user",
    val name: String,
    val phone: String,
    val addressLine1: String,
    val addressLine2: String = "",
    val landmark: String = "",
    val city: String,
    val state: String,
    val pincode: String,
    val addressType: String = "HOME",
    val isDefault: Boolean = false,
    val createdAt: Long = System.currentTimeMillis()
)