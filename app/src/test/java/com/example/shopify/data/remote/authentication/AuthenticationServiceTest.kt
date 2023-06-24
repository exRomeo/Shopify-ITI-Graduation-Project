package com.example.shopify.data.remote.authentication

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.shopify.core.helpers.RetrofitHelper
import com.example.shopify.data.models.Address
import com.example.shopify.data.models.Customer
import com.example.shopify.data.models.CustomerRequestBody
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.hamcrest.Matchers
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.*
import org.hamcrest.core.Is
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
@Config(manifest = Config.NONE)
class AuthenticationServiceTest {
    @get:Rule
    var instantExecuterRule = InstantTaskExecutorRule()

    private var authService: IAuthenticationService? = null

    @Before
    fun setUp() {
        authService =
            RetrofitHelper.getAuthenticationService("https://mad43-alex-and-team2.myshopify.com/")
    }

    @After
    fun tearDown() {
        authService
    }

    @Test
    fun getSingleCustomerFromShopify_customerId_NotNull() = runBlocking {
        //Given -> customer request body
        val customerId = 7121192321330
        //When -> calling  getSingleCustomerFromShopify()
        val response = authService?.getSingleCustomerFromShopify(customerId)
        //Then -> The result must be notNull
        assertThat(response, `is`(notNullValue()))
    }


    @Test
    fun registerUserToShopify_customer_notNull() = runBlocking {
        //Given -> customer request body (must by unique to success)
        val customer = Customer(
            firstName = "first",
            lastName = "second",
            email = "emaill@gmail.com",
            phone = "01245336722",
            verifiedEmail = true,
            addresses = listOf(Address(address1 = "country/city", phone = "01245336628")),
            password = "Password123#",
            passwordConfirmation = "Password123#",
            sendEmailWelcome = false
        )
        val clientResponse = CustomerRequestBody(customer)
        //when calling -> registerUserToShopify
        val response = authService?.registerUserToShopify(clientResponse)
        //Then -> The result must be notNull
        assertThat(response?.customer?.id, Is.`is`(notNullValue()))
    }
}