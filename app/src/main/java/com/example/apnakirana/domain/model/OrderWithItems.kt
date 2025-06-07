package com.example.apnakirana.domain.model

import com.example.apnakirana.data.local.entity.Order
import com.example.apnakirana.data.local.entity.OrderItem

data class OrderWithItems(
    val order: Order,
    val items: List<OrderItem>
)