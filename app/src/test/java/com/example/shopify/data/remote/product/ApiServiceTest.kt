package com.example.shopify.data.remote.product

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.shopify.data.remote.RetrofitHelper
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.hamcrest.MatcherAssert
import org.hamcrest.Matchers
import org.hamcrest.core.Is
import org.junit.Assert.*

import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
@Config(manifest= Config.NONE)
class ApiServiceTest {
    @get:Rule
    var instantExecuterRule = InstantTaskExecutorRule()

    private var apiService:ApiService? = null

    @Before
    fun setUp() {
        apiService = RetrofitHelper.apiService
    }

    @After
    fun tearDown() {
        apiService
    }

    @Test
    fun getBrands_brandsNotNull() = runBlocking{

            //when
            val response = apiService?.getBrands()

            //then
            assertThat(response?.code()as Int, Is.`is`(200))
            MatcherAssert.assertThat(response.body()!!.smart_collections, Matchers.notNullValue())
    }

    @Test
    fun getRandomProducts_productsNotNull() = runBlocking{

    //when
        val response = apiService?.getRandomProducts()

        //then
        assertThat(response?.code()as Int, Is.`is`(200))
        MatcherAssert.assertThat(response.body()!!.products, Matchers.notNullValue())
    }

    @Test
    fun getSpecificBrandProducts_correctId_ShouldPass() = runBlocking{
        //given
        val id = 8398828339506

        //when
        val response = apiService?.getSpecificBrandProducts(id)

        //then
        assertThat(response?.code()as Int, Is.`is`(200))
        MatcherAssert.assertThat(response.body()!!.products, Matchers.notNullValue())
    }

    @Test
    fun getProductsBySubcategory_coorectIdType_shouldPass() = runBlocking{
        //given
        val id = 8398828339506
        val type = "MEN"
        //when
        val response = apiService?.getProductsBySubcategory(id,type)

        //then
        assertThat(response?.code()as Int, Is.`is`(200))
        MatcherAssert.assertThat(response.body()!!.products, Matchers.notNullValue())
    }

    @Test
    fun getProductInfo_correctId_shouldPass()= runBlocking{
        //given
        val id = 8398828339506
        //when
        val response = apiService?.getProductInfo(id)

        //then
      //  assertThat(response?.code()as Int, Is.`is`(200))
        if (response != null) {
            MatcherAssert.assertThat(response.body()!!.product, Matchers.notNullValue())
        }
    }

    @Test
    fun getProductInfo_wrongId_shouldFail()= runBlocking{
        //given
        val id = 839882833950
        //when
        val response = apiService?.getProductInfo(id)

        //then
          assertThat(response?.code()as Int, Is.`is`(404))

        }

}