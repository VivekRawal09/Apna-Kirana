package com.example.apnakirana.data.repository

import com.example.apnakirana.data.SampleData
import com.example.apnakirana.data.local.entity.Category
import com.example.apnakirana.data.local.entity.Product
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SampleDataRepository @Inject constructor() {

    fun getAllProducts(): Flow<List<Product>> {
        return flowOf(SampleData.products)
    }

    fun getProductsByCategory(category: String): Flow<List<Product>> {
        return flowOf(SampleData.getProductsByCategory(category))
    }

    suspend fun getProductById(productId: String): Product? {
        return SampleData.products.find { it.id == productId }
    }

    fun searchProducts(query: String): Flow<List<Product>> {
        return flowOf(SampleData.searchProducts(query))
    }

    fun getAllCategories(): Flow<List<Category>> {
        return flowOf(SampleData.categories)
    }

    fun getFeaturedProducts(): Flow<List<Product>> {
        return flowOf(SampleData.getFeaturedProducts())
    }
}