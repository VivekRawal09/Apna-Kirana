package com.example.apnakirana.di

import com.example.apnakirana.data.repository.CartRepositoryImpl
import com.example.apnakirana.data.repository.ProductRepositoryImpl
import com.example.apnakirana.domain.repository.CartRepository
import com.example.apnakirana.domain.repository.ProductRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindProductRepository(
        productRepositoryImpl: ProductRepositoryImpl
    ): ProductRepository

    @Binds
    @Singleton
    abstract fun bindCartRepository(
        cartRepositoryImpl: CartRepositoryImpl
    ): CartRepository
}