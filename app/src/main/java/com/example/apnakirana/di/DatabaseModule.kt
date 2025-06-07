package com.example.apnakirana.di

import android.content.Context
import androidx.room.Room
import com.example.apnakirana.data.local.ApnaKiranaDatabase
import com.example.apnakirana.data.local.dao.*
import com.example.apnakirana.data.repository.OrderRepositoryImpl
import com.example.apnakirana.domain.repository.OrderRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideApnaKiranaDatabase(
        @ApplicationContext context: Context
    ): ApnaKiranaDatabase {
        return Room.databaseBuilder(
            context,
            ApnaKiranaDatabase::class.java,
            ApnaKiranaDatabase.DATABASE_NAME
        )
            .fallbackToDestructiveMigration() // ✅ This will recreate DB with new schema
            .build()
    }

    @Provides
    fun provideProductDao(database: ApnaKiranaDatabase): ProductDao {
        return database.productDao()
    }

    @Provides
    fun provideCartDao(database: ApnaKiranaDatabase): CartDao {
        return database.cartDao()
    }

    @Provides
    fun provideCategoryDao(database: ApnaKiranaDatabase): CategoryDao {
        return database.categoryDao()
    }

    @Provides
    fun provideOrderDao(database: ApnaKiranaDatabase): OrderDao {
        return database.orderDao()
    }

    @Provides
    fun provideAddressDao(database: ApnaKiranaDatabase): AddressDao {
        return database.addressDao()
    }

    @Provides
    @Singleton
    fun provideOrderRepository(
        orderRepositoryImpl: OrderRepositoryImpl
    ): OrderRepository = orderRepositoryImpl
}