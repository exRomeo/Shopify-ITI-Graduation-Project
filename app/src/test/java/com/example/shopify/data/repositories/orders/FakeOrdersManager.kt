package com.example.shopify.data.repositories.orders

import com.example.shopify.data.managers.orders.IOrdersManager
import com.example.shopify.data.models.order.OrderBody
import com.example.shopify.data.models.order.OrderIn
import com.example.shopify.data.models.order.OrdersResponse
import com.google.gson.Gson
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import java.io.File
import java.io.FileReader

class FakeOrdersManager : IOrdersManager {

    private var _orders: MutableSharedFlow<List<OrderIn>> = MutableSharedFlow(1)
    override val orders: SharedFlow<List<OrderIn>> = _orders.asSharedFlow()


    private val gson = Gson()

    override suspend fun getOrders() {
        val jsonFile = File("src/test/java/com/example/shopify/resources/fake_orders.json")
        val reader = FileReader(jsonFile)

        val fakeOrders: OrdersResponse = gson.fromJson(reader, OrdersResponse::class.java)
        _orders.emit(fakeOrders.orders)

    }

    override suspend fun makeOrder(orderBody: OrderBody) {
        val list: MutableList<OrderIn> = ArrayList(_orders.replayCache[0])
        list.add(
            OrderIn(
                54, orderURL = "", lineItems = orderBody.order.lineItems,
                date = "", total = "", confirmed = true, currency = "", totalDiscounts = ""
            )
        )

        _orders.emit(list)

    }

    override suspend fun deleteOrder(orderID: Long) {
        val list: MutableList<OrderIn> = ArrayList(_orders.replayCache[0])
        list.removeIf { it.id == orderID }
        _orders.emit(list)
    }

}