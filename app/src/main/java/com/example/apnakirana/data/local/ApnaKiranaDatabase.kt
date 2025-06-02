package com.example.apnakirana.data.local

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import android.content.Context
import com.example.apnakirana.data.local.dao.*
import com.example.apnakirana.data.local.entity.*

@Database(
    entities = [
        Product::class,
        CartItem::class,
        Category::class
    ],
    version = 1,
    exportSchema = false
)
abstract class ApnaKiranaDatabase : RoomDatabase() {

    abstract fun productDao(): ProductDao
    abstract fun cartDao(): CartDao
    abstract fun categoryDao(): CategoryDao

    companion object {
        const val DATABASE_NAME = "apna_kirana_database"
    }
}