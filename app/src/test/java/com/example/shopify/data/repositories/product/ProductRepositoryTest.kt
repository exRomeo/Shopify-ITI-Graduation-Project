package com.example.shopify.data.repositories.product

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.shopify.data.models.Brand
import com.example.shopify.data.models.Image
import com.example.shopify.data.models.Products
import com.example.shopify.data.models.SingleProductResponseBody
import com.example.shopify.data.models.SmartCollections
import com.example.shopify.data.remote.product.FakeRemoteResource
import com.example.shopify.presentation.screens.brands.BrandsViewModel
import com.example.weatherapplication.MainCoroutineRule
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.test.runTest
import org.hamcrest.Matchers
import org.junit.Assert.*

import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class ProductRepositoryTest {
    @get:Rule
    val mainDispatcherRule= MainCoroutineRule()
    @get:Rule
    val instance= InstantTaskExecutorRule()
    private lateinit var remoteDataSource: FakeRemoteResource
    private lateinit var repository: ProductRepository
    private var smartCollectionResponse: SmartCollections = SmartCollections(listOf(Brand(1,"adidas", Image(src ="image.png"))))
    private var ProductsResponse: Products = Products()
    private var SingleProductResponse: SingleProductResponseBody = SingleProductResponseBody()

    @Before
    fun setUp() {
        remoteDataSource = FakeRemoteResource(smartCollectionResponse,ProductsResponse,SingleProductResponse)
        repository= ProductRepository(remoteDataSource)
    }

    @After
    fun tearDown() {
    }

    @Test
    fun getBrands()= runTest{
        //when
        val result = repository.getBrands()
        //then
        assertThat(result.body(), Matchers.`is`(smartCollectionResponse))
    }

    @Test
    fun getRandomProducts()= runTest{
        //when
        val result = repository.getRandomProducts()
        //then
            assertThat(result.body(), Matchers.`is`(ProductsResponse))

    }

//    @Test
//    fun getSingleProductDetails() = runTest{
//        //given
//        val id = 8398828339506
//        //when
//        val result = repository.getSingleProductDetails(id)
//        //then
//        assertThat(result, Matchers.`is`(SingleProductResponse))
//    }

    @Test
    fun getSpecificBrandProducts()= runTest{
        //given
        val id = 8398828339506
        //when
        val result = repository.getSpecificBrandProducts(id)
        //then
        assertThat(result.body(), Matchers.`is`(ProductsResponse))
    }

    @Test
    fun getProductsBySubcategory()= runTest{
        //given
        val id = 8398828339506
        val type = "MEN"
        //when
        val result = repository.getProductsBySubcategory(id,type)
        //then
        assertThat(result.body(), Matchers.`is`(ProductsResponse))
    }
}