package com.example.shopify.data.repositories.authentication

import android.app.Application
import android.content.SharedPreferences
import android.preference.PreferenceManager
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.shopify.data.models.Address
import com.example.shopify.data.models.Customer
import com.example.shopify.data.models.CustomerRequestBody
import com.example.shopify.data.remote.authentication.IAuthenticationClient
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.*
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)

@Config(manifest= Config.NONE)
class AuthRepositoryTest {
    @get:Rule
    val instanceExecutorRule = InstantTaskExecutorRule()

    private val testDispatcher = UnconfinedTestDispatcher()
    private lateinit var fakeAuthRepo: IAuthRepository
    private lateinit var authClient: IAuthenticationClient
    private lateinit var sharedPreferences: SharedPreferences
    private val testingContext: Application = ApplicationProvider.getApplicationContext()

    @Before
    fun setUp() {
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(testingContext)
        fakeAuthRepo = FakeAuthRepository()
    }

    @After
    fun tearDown() {

    }

    @Test
    fun registerUserToShopify_customerRequestBody_notNullResult() = runTest {
        //Given -> customer request body
        val customer = Customer(
            firstName = "first",
            lastName = "second",
            email = "email12@gmail.com",
            phone = "01245336728",
            verifiedEmail = true,
            addresses = listOf(Address(address1 = "country/city", phone = "01245336628")),
            password = "Password123#",
            passwordConfirmation = "Password123#",
            sendEmailWelcome = false
        )
        val clientResponse = CustomerRequestBody(customer)
        //When -> calling  registerUserToShopify
        val result = fakeAuthRepo.registerUserToShopify(clientResponse)
        //Then -> The result must be notNull
        assertThat(result, `is`(notNullValue()))
    }

    @Test
    fun getSingleCustomerFromShopify_customerID_notNullResult() = runTest {
        //Given -> customer request body
        val customerId = 7121192321330
        //When -> calling  getSingleCustomerFromShopify()
        val result = fakeAuthRepo.getSingleCustomerFromShopify(customerId)
        //Then -> The result must be notNull
        assertThat(result, `is`(notNullValue()))
//        assertEquals(result, AuthenticationResponseState.Success(null))
    }

    @Test
    fun loginUserFirebase_EmailAndPassword_NotNull() = runTest {
        val email = "email12@gmail.com"
        val password = "Password123#"
        //When -> calling  getSingleCustomerFromShopify()
        val result = fakeAuthRepo.loginUserFirebase(email, password)
        //Then -> The result must be notNull
        assertThat(result, `is`(notNullValue()))
    }
    @Test
    fun signOutFirebase_false() = runTest{
        //When -> calling  signOutFirebase()
        val result = fakeAuthRepo.signOutFirebase()
        //Then -> The result must be false
        assertThat(result, `is`(false))
    }
}