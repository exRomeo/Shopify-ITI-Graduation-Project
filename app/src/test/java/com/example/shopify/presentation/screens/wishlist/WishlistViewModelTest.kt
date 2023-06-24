package com.example.shopify.presentation.screens.wishlist

import com.example.shopify.MainCoroutineRule
import com.example.shopify.core.utils.ConnectionUtil
import com.example.shopify.resources.FakeProducts
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.hamcrest.CoreMatchers
import org.hamcrest.MatcherAssert
import org.junit.Before

import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class WishlistViewModelTest{

    @OptIn(ExperimentalCoroutinesApi::class)
    @get:Rule
    val mainDispatcherRule = MainCoroutineRule()

    lateinit var wishlistViewModel: WishlistViewModel

    @Before
    fun setup(){
        ConnectionUtil.setTestMode()
        wishlistViewModel = WishlistViewModel(FakeWishlistRepository())
    }




    @Test
    fun getWishlistItems_itemsReceived() = runTest {

        //given set of 3 wishlist items
        val items = wishlistViewModel.wishlist

        //when received
        val result = items.first()

        //Then wishlist list size is 2
        MatcherAssert.assertThat(result.size, CoreMatchers.`is`(3))
    }



    @Test
    fun removeWishlistItem_wishlistItemsDecreased() = runTest {

        //given set of 3 wishlist items
        val items = wishlistViewModel.wishlist

        //when removing wishlist item
        wishlistViewModel.removeWishlistItem(FakeProducts.product1.id)
        val result = items.first()

        //then wishlist is decreased by one
        MatcherAssert.assertThat(result.size, CoreMatchers.`is`(2))
    }


}