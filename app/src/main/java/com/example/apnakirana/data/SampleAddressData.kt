package com.example.apnakirana.data

import com.example.apnakirana.data.local.entity.Address
import java.util.UUID

object SampleAddressData {

    fun getSampleAddresses(): List<Address> {
        return listOf(
            Address(
                addressId = UUID.randomUUID().toString(),
                name = "John Doe",
                phone = "+91 9876543210",
                addressLine1 = "123, Green Avenue",
                addressLine2 = "Near City Mall",
                landmark = "Opposite to Metro Station",
                city = "Mumbai",
                state = "Maharashtra",
                pincode = "400001",
                addressType = "HOME",
                isDefault = true
            ),
            Address(
                addressId = UUID.randomUUID().toString(),
                name = "John Doe",
                phone = "+91 9876543210",
                addressLine1 = "456, Tech Park, Block A",
                addressLine2 = "4th Floor",
                landmark = "Near Food Court",
                city = "Mumbai",
                state = "Maharashtra",
                pincode = "400002",
                addressType = "OFFICE",
                isDefault = false
            ),
            Address(
                addressId = UUID.randomUUID().toString(),
                name = "Jane Smith",
                phone = "+91 8765432109",
                addressLine1 = "789, Rose Gardens",
                addressLine2 = "Villa No. 15",
                landmark = "Behind Community Center",
                city = "Mumbai",
                state = "Maharashtra",
                pincode = "400003",
                addressType = "OTHER",
                isDefault = false
            )
        )
    }
}