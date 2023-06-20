package com.example.shopify.data.repositories.orders

import com.example.shopify.data.models.address.Address
import com.example.shopify.data.models.draftorder.DraftOrder
import com.example.shopify.data.models.order.DiscountCode
import com.example.shopify.data.models.order.OrderIn
import kotlinx.coroutines.flow.SharedFlow

interface IOrdersRepository {
    val orders: SharedFlow<List<OrderIn>>

    suspend fun makeOrder(
        draftOrder: DraftOrder,
        address: Address,
        discountCodes: List<DiscountCode>? = null
    )

    suspend fun getOrders()

    suspend fun deleteOrder(orderID: Long)
}