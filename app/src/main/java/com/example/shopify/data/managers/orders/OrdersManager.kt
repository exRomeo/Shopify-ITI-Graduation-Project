package com.example.shopify.data.managers.orders

import com.example.shopify.BuildConfig
import com.example.shopify.core.helpers.CurrentUserHelper
import com.example.shopify.data.models.order.OrderBody
import com.example.shopify.data.models.order.OrderIn
import com.example.shopify.data.repositories.user.remote.retrofitclient.OrdersAPI
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow

class OrdersManager(private val ordersAPI: OrdersAPI) : IOrdersManager {

    private var _orders: MutableSharedFlow<List<OrderIn>> = MutableSharedFlow(1)
    override val orders = _orders.asSharedFlow()

    override suspend fun getOrders() {
        val response =
            ordersAPI.getCustomerOrders(BuildConfig.ACCESS_TOKEN, CurrentUserHelper.customerID)
        if (response.isSuccessful)
            response.body()?.let { _orders.emit(it.orders) }
    }

    override suspend fun makeOrder(orderBody: OrderBody) {
        orderBody.order.customer.id = CurrentUserHelper.customerID
        val response = ordersAPI.createOrder(
            accessToken = BuildConfig.ACCESS_TOKEN,
            orderBody = orderBody
        )
        getOrders()
    }

    override suspend fun deleteOrder(orderID: Long) {
        ordersAPI.deleteOrder(
            accessToken = BuildConfig.ACCESS_TOKEN, orderID = orderID
        )
        _orders.emit(listOf())
    }

}