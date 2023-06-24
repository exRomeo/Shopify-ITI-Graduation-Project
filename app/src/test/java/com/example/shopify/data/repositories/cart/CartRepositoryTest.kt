package com.example.shopify.data.repositories.cart


import com.example.shopify.resources.FakeProducts
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Before
import org.junit.Test

class CartRepositoryTest {

    lateinit var cartRepository: CartRepository

    @Before
    fun setup() {
        cartRepository = CartRepository(FakeCartManager())
    }

    @Test
    fun getWishlistItems_itemsReceived() = runTest {

        //given set of 3 wishlist items
        val items = cartRepository.cart
        cartRepository.getCartItems()

        //when received
        val result = items.first()

        //Then orders list size is 2
        assertThat(result.size, `is`(3))
    }

    @Test
    fun addWishlistItem_wishlistItemsIncreased() = runTest {
        //given set of 3 wishlist items
        val items = cartRepository.cart
        cartRepository.getCartItems()

        //when added new wishlist item
        cartRepository.addCartItem(2656468, 286161867)
        val result = items.first()
        //then wishlist is increased by one
        assertThat(result.size, `is`(4))
    }

    @Test
    fun removeWishlistItem_wishlistItemsDecreased() = runTest {

        //given set of 3 wishlist items
        val items = cartRepository.cart
        cartRepository.getCartItems()

        //when removing wishlist item
        cartRepository.removeCart(FakeProducts.product1.id)
        val result = items.first()

        //then wishlist is decreased by one
        assertThat(result.size, `is`(2))
    }

    @Test
    fun increaseCartItemCount_initialCountTwo_newCountThree() = runTest {

        //Given a cart item
        cartRepository.getCartItems()
        val initialValue = cartRepository.getCartItemCount(FakeProducts.product1)
        //when increased its count
        cartRepository.increaseCartItemCount(FakeProducts.product1)

        //count is increased by one
        val result = cartRepository.getCartItemCount(FakeProducts.product1)
        assertThat(result, `is`(initialValue + 1))
    }

    @Test
    fun decreaseCartItemCount_initialCountTwo_newCountOne() = runTest {

        //Given a cart item
        cartRepository.getCartItems()
        val initialValue = cartRepository.getCartItemCount(FakeProducts.product1)
        //when decreased its count
        cartRepository.decreaseCartItemCount(FakeProducts.product1)

        //count is increased by one
        val result = cartRepository.getCartItemCount(FakeProducts.product1)
        assertThat(result, `is`(initialValue - 1))
    }


}