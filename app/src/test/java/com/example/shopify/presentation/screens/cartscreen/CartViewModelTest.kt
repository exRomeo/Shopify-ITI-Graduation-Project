package com.example.shopify.presentation.screens.cartscreen

import com.example.shopify.MainCoroutineRule
import com.example.shopify.core.utils.ConnectionUtil
import com.example.shopify.resources.FakeProducts
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
class CartViewModelTest {

    @OptIn(ExperimentalCoroutinesApi::class)
    @get:Rule
    val mainDispatcherRule = MainCoroutineRule()

    private lateinit var cartViewModel: CartViewModel

    @Before
    fun setup() {
        ConnectionUtil.setTestMode()
        cartViewModel = CartViewModel(FakeCartRepository())
    }


    @Test
    fun getWishlistItems_itemsReceived() = runTest {

        //given set of  wishlist items
        val items = cartViewModel.cart
        val originalValue = items.value.size
        //when received
        val result = items.first()

        //Then orders list size is same as original value
        assertThat(result.size, `is`(originalValue))
    }



    @Test
    fun removeCartItem_wishlistItemsDecreased() = runTest {

        //given set of cart items
        val items = cartViewModel.cart

        val originalValue = items.value.size

        //when removing cart item
        cartViewModel.removeCart(FakeProducts.product1.id)
        val result = items.first()

        //then Cart is decreased by one
        assertThat(result.size, `is`(originalValue-1))
    }

    @Test
    fun increaseCartItemCount_initialCountTwo_newCountThree() = runTest {

        //Given a cart
        val initialValue = cartViewModel.getCartItemCount(FakeProducts.product1)
        //when increased an item count
        cartViewModel.increaseCartItemCount(FakeProducts.product1)

        //count is increased by one
        val result = cartViewModel.getCartItemCount(FakeProducts.product1)
        assertThat(result, `is`(initialValue + 1))
    }

    @Test
    fun decreaseCartItemCount_initialCountTwo_newCountOne() = runTest {

        //Given a cart item

        val initialValue = cartViewModel.getCartItemCount(FakeProducts.product1)
        //when decreased an item count
        cartViewModel.decreaseCartItemCount(FakeProducts.product1)

        //count is increased by one
        val result = cartViewModel.getCartItemCount(FakeProducts.product1)
        assertThat(result, `is`(initialValue - 1))
    }
}