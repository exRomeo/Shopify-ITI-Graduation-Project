package com.example.shopify.presentation.screens.categories

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.ext.junit.runners.AndroidJUnit4
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
import com.example.shopify.presentation.screens.brands.BrandsViewModel
import com.example.weatherapplication.MainCoroutineRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runBlockingTest
import org.hamcrest.Matchers
import org.hamcrest.core.IsNull
import org.junit.Assert.*

import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
class CategoriesViewModelTest {
    @get:Rule
    val mainDispatcherRule= MainCoroutineRule()
    @get:Rule
    val instance= InstantTaskExecutorRule()
    private lateinit var remoteDataSource: FakeRemoteResource
    private lateinit var viewModel: CategoriesViewModel
    private lateinit var repository: FakeProductRepository

    private var smartCollectionResponse: SmartCollections = SmartCollections(listOf(Brand(1,"adidas", Image(src ="image.png"))))
    private var ProductsResponse: Products = Products()
    private var SingleProductResponse: SingleProductResponseBody = SingleProductResponseBody()

    @Before
    fun setUp() {

        remoteDataSource = FakeRemoteResource(smartCollectionResponse,ProductsResponse,SingleProductResponse)
        repository= FakeProductRepository(smartCollectionResponse,ProductsResponse,SingleProductResponse)

        viewModel= CategoriesViewModel(repository)
    }


    @After
    fun tearDown() {
    }

    @Test
    fun getProductsBySubcategory_returnSuccess() = mainDispatcherRule.runBlockingTest{
        //When retrieving brands Object
        viewModel.getProductsBySubcategory()
        val result = async { viewModel.productsList.first() }
        //Then The object must be inserted
        val resultState = result.await()
        advanceUntilIdle()
        assertThat(resultState, Matchers.isA(UiState.Success::class.java))
    }
    @Test
    fun getProductsBySubcategory_returnNotnull() = mainDispatcherRule.runBlockingTest{
        //When retrieving brands Object
        val result = viewModel.getProductsBySubcategory()
        advanceUntilIdle()
        assertThat(result, IsNull.notNullValue())
    }
}