package com.example.shopify.data.managers.orders

import android.util.Log
import com.example.shopify.BuildConfig
import com.example.shopify.core.helpers.CurrentUserHelper
import com.example.shopify.data.models.order.OrderBody
import com.example.shopify.data.models.order.OrderIn
import com.example.shopify.data.repositories.user.remote.retrofitclient.OrdersAPI
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow

class OrdersManager(private val ordersAPI: OrdersAPI) : IOrdersManager {

    private var _orders: MutableSharedFlow<List<OrderIn>> = MutableSharedFlow(1)
    override val orders = _orders.asSharedFlow()

    override suspend fun getOrders() {
        if (CurrentUserHelper.hasOrder()) {
            val orderIDS = listOf(CurrentUserHelper.orderID)
            _orders.emit(getOrdersByID(orderIDS))

        }
    }

    override suspend fun makeOrder(orderBody: OrderBody) {
        val response = ordersAPI.createOrder(
            accessToken = BuildConfig.ACCESS_TOKEN,
            orderBody = orderBody
        )
        if (response.isSuccessful)
            response.body()?.let {
                CurrentUserHelper.orderID = it.order.id
                var oldList: List<OrderIn>? = null
                try {
                    oldList = ArrayList(orders.replayCache).first()
                } catch (e: Exception) {
                    Log.i("OrdersManager", "makeOrder: ${e.message}")
                }
                val list: MutableList<OrderIn> = mutableListOf()
                if (!oldList.isNullOrEmpty())
                    list.addAll(oldList)
                list += it.order
                _orders.emit(list)
            }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    override suspend fun deleteOrder(orderID: Long) {
        ordersAPI.deleteOrder(
            accessToken = BuildConfig.ACCESS_TOKEN, orderID = orderID
        )
        _orders.resetReplayCache()
        CurrentUserHelper.orderID = -1
    }

    private suspend fun getOrdersByID(ids: List<Long>): List<OrderIn> {
        val ordersList: MutableList<OrderIn> = mutableListOf()
        ids.forEach {
            val response = ordersAPI.getOrder(
                accessToken = BuildConfig.ACCESS_TOKEN,
                orderID = it
            )
            if (response.isSuccessful)
                response.body()?.let { body -> ordersList.add(body.order) }
        }
        return ordersList
    }

}