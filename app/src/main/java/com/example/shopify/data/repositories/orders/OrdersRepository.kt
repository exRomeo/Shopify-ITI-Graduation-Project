package com.example.shopify.data.repositories.orders

import com.example.shopify.data.managers.orders.IOrdersManager
import com.example.shopify.data.models.address.Address
import com.example.shopify.data.models.draftorder.DraftOrder
import com.example.shopify.data.models.order.DiscountCode
import com.example.shopify.data.models.order.OrderBody
import com.example.shopify.data.models.order.OrderIn
import com.example.shopify.data.models.order.OrderOut
import kotlinx.coroutines.flow.SharedFlow

class OrdersRepository(private val ordersManager: IOrdersManager) : IOrdersRepository {

    override val orders: SharedFlow<List<OrderIn>> = ordersManager.orders
    override suspend fun makeOrder(
        draftOrder: DraftOrder,
        address: Address,
        discountCodes: List<DiscountCode>?
    ) =
        ordersManager
            .makeOrder(
                OrderBody(
                    OrderOut(
                        shippingAddress = address,
                        lineItems = draftOrder.lineItems,
                        discountCodes = discountCodes
                    )
                )
            )

    override suspend fun getOrders() = ordersManager.getOrders()

    override suspend fun deleteOrder(orderID: Long) = ordersManager.deleteOrder(orderID = orderID)
}