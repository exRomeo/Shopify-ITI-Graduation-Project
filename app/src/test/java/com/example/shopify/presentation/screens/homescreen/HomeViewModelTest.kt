package com.example.shopify.presentation.screens.homescreen

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.shopify.MainCoroutineRule
import com.example.shopify.core.helpers.UiState
import com.example.shopify.data.managers.cart.CartManager
import com.example.shopify.data.managers.wishlist.WishlistManager
import com.example.shopify.data.models.Brand
import com.example.shopify.data.models.Image
import com.example.shopify.data.models.Products
import com.example.shopify.data.models.SingleProductResponseBody
import com.example.shopify.data.models.SmartCollections
import com.example.shopify.data.remote.product.FakeRemoteResource
import com.example.shopify.data.repositories.product.FakeProductRepository
import com.example.shopify.data.repositories.product.ProductRepository
import com.example.shopify.data.repositories.user.remote.retrofitclient.ShopifyAPIClient
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.test.runTest
import org.hamcrest.Matchers.isA
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
class HomeViewModelTest {
    @get:Rule
    val mainDispatcherRule= MainCoroutineRule()
    @get:Rule
    val instance= InstantTaskExecutorRule()

    private lateinit var remoteDataSource: FakeRemoteResource
    private lateinit var viewModel: HomeViewModel
    private lateinit var repository: FakeProductRepository

    private var smartCollectionResponse: SmartCollections = SmartCollections(listOf(Brand(1,"adidas", Image(src ="image.png"))))
    private var ProductsResponse: Products = Products()
    private var SingleProductResponse: SingleProductResponseBody = SingleProductResponseBody()


    @Before
    fun setUp() {
        val wishlistManager: WishlistManager by lazy {
            WishlistManager(ShopifyAPIClient.draftOrderAPI)
        }

        val cartManager: CartManager by lazy {
            CartManager(ShopifyAPIClient.draftOrderAPI)
        }
        remoteDataSource = FakeRemoteResource(smartCollectionResponse,ProductsResponse,SingleProductResponse)
        repository= FakeProductRepository(smartCollectionResponse,ProductsResponse,SingleProductResponse)

        viewModel= HomeViewModel(repository,wishlistManager,cartManager)
    }

    @After
    fun tearDown() {
    }




    @Test
    fun getBrands_returnSuccess() = runTest {

        //When retrieving brands Object
        viewModel.getBrands()
        val result = async { viewModel.brandList.first() }
        //Then The object must be inserted
        val resultState = result.await()
        advanceUntilIdle()
        assertThat(resultState, isA(UiState.Success::class.java))
    }

    @Test
    fun getRandomProducts_returnSuccess() = mainDispatcherRule.runBlockingTest{
        //When retrieving products Object
        viewModel.getRandomProducts()
        val result = async { viewModel.randomList.first() }
        //Then The object must be inserted
        val resultState = result.await()
        assertThat(resultState, isA(UiState.Success::class.java))
    }


}