package com.example.shopify.data.managers.orders

import com.example.shopify.data.models.order.OrderBody
import com.example.shopify.data.models.order.OrderIn
import kotlinx.coroutines.flow.SharedFlow

interface IOrdersManager {
    val orders: SharedFlow<List<OrderIn>>

    suspend fun getOrders()

    suspend fun makeOrder(orderBody: OrderBody)

    suspend fun deleteOrder(orderID: Long)
}