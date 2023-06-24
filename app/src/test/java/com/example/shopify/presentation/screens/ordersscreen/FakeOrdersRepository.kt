package com.example.shopify.presentation.screens.ordersscreen

import com.example.shopify.data.models.address.Address
import com.example.shopify.data.models.draftorder.DraftOrder
import com.example.shopify.data.models.order.DiscountCode
import com.example.shopify.data.models.order.OrderIn
import com.example.shopify.data.models.order.OrdersResponse
import com.example.shopify.data.repositories.orders.IOrdersRepository
import com.google.gson.Gson
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileReader

class FakeOrdersRepository : IOrdersRepository {

    private var _orders: MutableSharedFlow<List<OrderIn>> = MutableSharedFlow(1)
    override val orders: SharedFlow<List<OrderIn>> = _orders.asSharedFlow()

    private val gson = Gson()
    private lateinit var currentList: MutableList<OrderIn>

    init {
        GlobalScope.launch {
            getOrders()
        }
    }

    override suspend fun getOrders() {
        val jsonFile = File("src/test/java/com/example/shopify/resources/fake_orders.json")
        val reader = FileReader(jsonFile)

        val fakeOrders: OrdersResponse = gson.fromJson(reader, OrdersResponse::class.java)
        currentList = fakeOrders.orders.toMutableList()
        _orders.emit(currentList)

    }


    override suspend fun makeOrder(
        draftOrder: DraftOrder,
        address: Address,
        discountCodes: List<DiscountCode>?
    ) {
        currentList.add(currentList[1].copy(id = 70))
        _orders.emit(currentList)
    }


    override suspend fun deleteOrder(orderID: Long) {
        currentList.removeIf { it.id == orderID }
        _orders.emit(currentList)
    }
}