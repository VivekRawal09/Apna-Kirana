package com.example.apnakirana.data.repository

import com.example.apnakirana.data.SampleData
import com.example.apnakirana.data.local.entity.Category
import com.example.apnakirana.data.local.entity.Product
import com.example.apnakirana.domain.repository.ProductRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SampleDataRepository @Inject constructor() : ProductRepository {

    override fun getAllProducts(): Flow<List<Product>> {
        return flowOf(SampleData.products)
    }

    override fun getProductsByCategory(category: String): Flow<List<Product>> {
        return flowOf(SampleData.getProductsByCategory(category))
    }

    override suspend fun getProductById(productId: String): Product? {
        return SampleData.products.find { it.id == productId }
    }

    override fun searchProducts(query: String): Flow<List<Product>> {
        return flowOf(SampleData.searchProducts(query))
    }

    override suspend fun insertProducts(products: List<Product>) {
        // For sample data, we don't need to implement this
        // In real implementation, this would save to database
    }

    fun getAllCategories(): Flow<List<Category>> {
        return flowOf(SampleData.categories)
    }

    fun getFeaturedProducts(): Flow<List<Product>> {
        return flowOf(SampleData.getFeaturedProducts())
    }
}