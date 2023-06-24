package com.example.shopify.presentation.screens.ordersscreen

import com.example.shopify.MainCoroutineRule
import com.example.shopify.core.helpers.UserScreenUISState
import com.example.shopify.core.utils.ConnectionUtil
import com.example.shopify.data.models.order.OrderIn
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class OrdersViewModelTest {
    @OptIn(ExperimentalCoroutinesApi::class)
    @get:Rule
    val mainDispatcherRule = MainCoroutineRule()

    private lateinit var ordersViewModel: OrdersViewModel

    @Before
    fun setup() {
        ConnectionUtil.setTestMode()
        ordersViewModel = OrdersViewModel(FakeOrdersRepository())
    }


    @Test
    fun getOrdersState_ordersReceivedSuccessfully() = runTest {

        //Given orders state
        val state = ordersViewModel.state

        //When received orders list
        val result = state.first()

        //Then state is success
        assertThat(
            result::class.java.isAssignableFrom(UserScreenUISState.Success::class.java),
            `is`(true)
        )
    }


    @Test
    fun getOrders_ordersReceived() = runTest {

        //Given orders state
        val state = ordersViewModel.state

        //When received orders list
        val items = (state.first() as UserScreenUISState.Success<*>).data as List<OrderIn>

        //Then orders list size is 2
        assertThat(items.size, `is`(2))

    }


    @Test
    fun deleteOrder_ordersDecreased() = runTest {

        //Given orders state
        val state = ordersViewModel.state

        //When canceling an order
        ordersViewModel.cancelOrder(5389652853042)

        //When received orders list
        val items = (state.first() as UserScreenUISState.Success<*>).data as List<OrderIn>


        //then order list is decreased by one
        assertThat(items.size, `is`(1))
    }
}