package com.example.shopify.data.repositories.wishlist

import com.example.shopify.resources.FakeProducts
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class WishlistRepositoryTest {

    lateinit var wishlistRepository: WishlistRepository

    @Before
    fun setup() {
        wishlistRepository = WishlistRepository(FakeWishlistManager())
    }

    @Test
    fun getWishlistItems_itemsReceived() = runTest {

        //given set of 3 wishlist items
        val items = wishlistRepository.wishlist
        wishlistRepository.getWishlistItems()

        //when received
        val result = items.first()

        //Then wishlist list size is 2
        assertThat(result.size, `is`(3))
    }

    @Test
    fun addWishlistItem_wishlistItemsIncreased() = runTest {
        //given set of 3 wishlist items
        val items = wishlistRepository.wishlist
        wishlistRepository.getWishlistItems()

        //when added new wishlist item
        wishlistRepository.addWishlistItem(2656468, 286161867)
        val result = items.first()
        //then wishlist is increased by one
        assertThat(result.size, `is`(4))
    }

    @Test
    fun removeWishlistItem_wishlistItemsDecreased() = runTest {

        //given set of 3 wishlist items
        val items = wishlistRepository.wishlist
        wishlistRepository.getWishlistItems()

        //when removing wishlist item
        wishlistRepository.removeWishlistItem(FakeProducts.product1.id)
        val result = items.first()

        //then wishlist is decreased by one
        assertThat(result.size, `is`(2))
    }

    @Test
    fun isFavorite_removedItems_itemIsNoLongerInWishlist() = runTest {

        //Given a wishlist
        wishlistRepository.getWishlistItems()

        //when removing a wishlist item
        wishlistRepository.removeWishlistItem(FakeProducts.product1.id)


        //the item is no longer in wishlist
        val result = wishlistRepository.isFavorite(FakeProducts.product1.id, FakeProducts.product1.variants[0].id)
        assertThat(result, `is`(false))
    }

    @Test
    fun isFavorite_itemInWishList_itemIsInWishlist() = runTest {

        //Given a wishlist
        wishlistRepository.getWishlistItems()

        //when checking a wishlist item
        val result = wishlistRepository.isFavorite(FakeProducts.product1.id, FakeProducts.product1.variants[0].id)

        //the item is in wishlist
        assertThat(result, `is`(true))
    }
}