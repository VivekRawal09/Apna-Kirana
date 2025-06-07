package com.example.apnakirana.data.repository

import com.example.apnakirana.data.local.dao.AddressDao
import com.example.apnakirana.data.local.dao.OrderDao
import com.example.apnakirana.data.local.dao.ProductDao
import com.example.apnakirana.data.local.entity.Address
import com.example.apnakirana.data.local.entity.Order
import com.example.apnakirana.data.local.entity.OrderItem
import com.example.apnakirana.data.local.entity.PaymentMethod
import com.example.apnakirana.domain.model.CheckoutData
import com.example.apnakirana.domain.repository.OrderRepository
import kotlinx.coroutines.flow.Flow
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class OrderRepositoryImpl @Inject constructor(
    private val orderDao: OrderDao,
    private val addressDao: AddressDao,
    private val productDao: ProductDao
) : OrderRepository {

    override suspend fun createOrder(checkoutData: CheckoutData): Result<String> {
        return try {
            val orderId = "ORDER_${System.currentTimeMillis()}_${UUID.randomUUID().toString().take(6).uppercase()}"

            // Create order
            val order = Order(
                orderId = orderId,
                subtotal = checkoutData.subtotal,
                deliveryFee = checkoutData.deliveryFee,
                discount = checkoutData.discount,
                totalAmount = checkoutData.totalAmount,
                deliveryAddressId = checkoutData.selectedAddress.addressId,
                paymentMethod = checkoutData.selectedPaymentMethod.id,
                paymentStatus = if (checkoutData.selectedPaymentMethod.id == "cod") "PENDING" else "PAID"
            )

            // Create order items from cart
            val orderItems = checkoutData.cartItems.map { cartItem ->
                OrderItem(
                    orderItemId = "${orderId}_${cartItem.product.id}",
                    orderId = orderId,
                    productId = cartItem.product.id,
                    productName = cartItem.product.name,
                    productPrice = cartItem.product.price,
                    quantity = cartItem.quantity,
                    totalPrice = cartItem.product.price * cartItem.quantity
                )
            }

            // Save to database
            orderDao.insertOrder(order)
            orderDao.insertOrderItems(orderItems)

            Result.success(orderId)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getOrderById(orderId: String): Order? {
        return orderDao.getOrderById(orderId)
    }

    override fun getOrderHistory(): Flow<List<Order>> {
        return orderDao.getOrdersByUser()
    }

    override suspend fun updateOrderStatus(orderId: String, status: String) {
        orderDao.updateOrderStatus(orderId, status)
    }

    override suspend fun getOrderItems(orderId: String): List<OrderItem> {
        return orderDao.getOrderItems(orderId)
    }

    // Address operations
    override suspend fun addAddress(address: Address) {
        // If this is the first address, make it default
        val existingAddresses = addressDao.getAddressesByUser()

        val finalAddress = if (address.isDefault) {
            // Clear other default addresses
            addressDao.clearDefaultAddress()
            address
        } else {
            address
        }

        addressDao.insertAddress(finalAddress)
    }

    override fun getAddresses(): Flow<List<Address>> {
        return addressDao.getAddressesByUser()
    }

    override suspend fun getDefaultAddress(): Address? {
        return addressDao.getDefaultAddress()
    }

    override suspend fun setDefaultAddress(addressId: String) {
        addressDao.clearDefaultAddress()
        addressDao.setDefaultAddress(addressId)
    }

    override suspend fun deleteAddress(addressId: String) {
        addressDao.deleteAddressById(addressId)
    }

    override fun getPaymentMethods(): List<PaymentMethod> {
        return PaymentMethod.getDefaultPaymentMethods()
    }

    override suspend fun getTotalOrdersCount(): Int {
        return orderDao.getTotalOrdersCount()
    }

    override suspend fun getTotalSpent(): Double {
        return orderDao.getTotalSpent() ?: 0.0
    }
}