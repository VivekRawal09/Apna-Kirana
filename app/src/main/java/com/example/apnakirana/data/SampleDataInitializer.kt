package com.example.apnakirana.data

import com.example.apnakirana.domain.repository.OrderRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SampleDataInitializer @Inject constructor(
    private val orderRepository: OrderRepository
) {

    suspend fun initializeSampleData() {
        try {
            // Check if addresses already exist
            val existingAddresses = orderRepository.getAddresses()

            // If no addresses exist, add sample addresses
            existingAddresses.collect { addresses ->
                if (addresses.isEmpty()) {
                    SampleAddressData.getSampleAddresses().forEach { address ->
                        orderRepository.addAddress(address)
                    }
                }
            }
        } catch (e: Exception) {
            // Handle error - in production, you might want to log this
            println("Error initializing sample data: ${e.message}")
        }
    }
}