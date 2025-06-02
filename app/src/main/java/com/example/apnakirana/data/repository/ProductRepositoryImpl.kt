package com.example.apnakirana.data.repository

import com.example.apnakirana.data.local.dao.ProductDao
import com.example.apnakirana.data.local.entity.Product
import com.example.apnakirana.domain.repository.ProductRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ProductRepositoryImpl @Inject constructor(
    private val productDao: ProductDao
) : ProductRepository {

    override fun getAllProducts(): Flow<List<Product>> {
        return productDao.getAllProducts()
    }

    override fun getProductsByCategory(category: String): Flow<List<Product>> {
        return productDao.getProductsByCategory(category)
    }

    override suspend fun getProductById(productId: String): Product? {
        return productDao.getProductById(productId)
    }

    override fun searchProducts(query: String): Flow<List<Product>> {
        return productDao.searchProducts(query)
    }

    override suspend fun insertProducts(products: List<Product>) {
        productDao.insertProducts(products)
    }
}