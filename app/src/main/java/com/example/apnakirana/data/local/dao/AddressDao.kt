package com.example.apnakirana.data.local.dao

import androidx.room.*
import com.example.apnakirana.data.local.entity.Address
import kotlinx.coroutines.flow.Flow

@Dao
interface AddressDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAddress(address: Address)

    @Query("SELECT * FROM addresses WHERE userId = :userId ORDER BY isDefault DESC, createdAt DESC")
    fun getAddressesByUser(userId: String = "default_user"): Flow<List<Address>>

    @Query("SELECT * FROM addresses WHERE addressId = :addressId")
    suspend fun getAddressById(addressId: String): Address?

    @Query("SELECT * FROM addresses WHERE userId = :userId AND isDefault = 1 LIMIT 1")
    suspend fun getDefaultAddress(userId: String = "default_user"): Address?

    @Query("UPDATE addresses SET isDefault = 0 WHERE userId = :userId")
    suspend fun clearDefaultAddress(userId: String = "default_user")

    @Query("UPDATE addresses SET isDefault = 1 WHERE addressId = :addressId")
    suspend fun setDefaultAddress(addressId: String)

    @Delete
    suspend fun deleteAddress(address: Address)

    @Query("DELETE FROM addresses WHERE addressId = :addressId")
    suspend fun deleteAddressById(addressId: String)
}