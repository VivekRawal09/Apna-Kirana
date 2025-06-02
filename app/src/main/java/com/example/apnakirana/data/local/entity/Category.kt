package com.example.apnakirana.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "categories")
data class Category(
    @PrimaryKey
    val id: String,
    val name: String,
    val icon: String, // emoji or image url
    val isActive: Boolean = true
)