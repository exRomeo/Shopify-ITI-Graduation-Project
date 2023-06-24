package com.example.shopify.data.repositories.orders

import com.example.shopify.data.models.draftorder.DraftOrder
import com.example.shopify.resources.FakeAddress
import com.example.shopify.resources.FakeLineItems
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class OrdersRepositoryTest {


    lateinit var ordersRepository: OrdersRepository

    @Before
    fun setup() {
        ordersRepository = OrdersRepository(FakeOrdersManager())
    }

    @Test
    fun getOrders_ordersReceived() = runTest {

        //Given set of 2 orders
        val items = ordersRepository.orders
        ordersRepository.getOrders()

        //When received orders list
        val result = items.first()

        //Then orders list size is 2
        assertThat(result.size, `is`(2))
    }

    @Test
    fun addOrder_ordersIncreased() = runTest {

        //Given set of 2 orders
        val items = ordersRepository.orders
        ordersRepository.getOrders()

        //When making new order
        ordersRepository.makeOrder(
            DraftOrder(150L, "order", FakeLineItems.lineItems, "500"),
            FakeAddress.address
        )
        val result = items.first()

        //then order list is increased by one
        assertThat(result.size, `is`(3))
    }

    @Test
    fun deleteOrder_ordersDecreased() = runTest {

        //Given set of 2 orders
        val items = ordersRepository.orders
        ordersRepository.getOrders()

        //When canceling an order
        ordersRepository.deleteOrder(5389652853042)
        val result = items.first()

        //then order list is decreased by one
        assertThat(result.size, `is`(1))
    }

}