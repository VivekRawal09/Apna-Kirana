package com.example.apnakirana.domain.repository

import com.example.apnakirana.data.local.entity.Product
import kotlinx.coroutines.flow.Flow

interface ProductRepository {
    fun getAllProducts(): Flow<List<Product>>
    fun getProductsByCategory(category: String): Flow<List<Product>>
    suspend fun getProductById(productId: String): Product?
    fun searchProducts(query: String): Flow<List<Product>>
    suspend fun insertProducts(products: List<Product>)
}