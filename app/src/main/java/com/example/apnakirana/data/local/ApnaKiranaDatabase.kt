package com.example.apnakirana.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.apnakirana.data.local.dao.AddressDao
import com.example.apnakirana.data.local.dao.CartDao
import com.example.apnakirana.data.local.dao.CategoryDao
import com.example.apnakirana.data.local.dao.OrderDao
import com.example.apnakirana.data.local.dao.ProductDao
import com.example.apnakirana.data.local.entity.Address
import com.example.apnakirana.data.local.entity.CartItem
import com.example.apnakirana.data.local.entity.Category
import com.example.apnakirana.data.local.entity.Order
import com.example.apnakirana.data.local.entity.OrderItem
import com.example.apnakirana.data.local.entity.Product

@Database(
    entities = [
        Product::class,
        CartItem::class,
        Category::class,
        Order::class,
        OrderItem::class,
        Address::class
    ],
    version = 3, // ✅ Increment version again to force migration
    exportSchema = false
)
abstract class ApnaKiranaDatabase : RoomDatabase() {

    abstract fun productDao(): ProductDao
    abstract fun cartDao(): CartDao
    abstract fun categoryDao(): CategoryDao
    abstract fun orderDao(): OrderDao
    abstract fun addressDao(): AddressDao

    companion object {
        const val DATABASE_NAME = "apna_kirana_database"
    }
}