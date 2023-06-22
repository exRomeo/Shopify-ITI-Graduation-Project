package com.example.shopify.presentation.screens.authentication.registeration

import android.app.Application
import android.content.SharedPreferences
import android.preference.PreferenceManager
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.shopify.MainCoroutineRule
import com.example.shopify.core.helpers.AuthenticationResponseState
import com.example.shopify.data.models.Address
import com.example.shopify.data.models.Customer
import com.example.shopify.data.models.CustomerRequestBody
import com.example.shopify.data.remote.authentication.FakeAuthenticationClientTest
import com.example.shopify.data.remote.authentication.IAuthenticationClient
import com.example.shopify.data.repositories.authentication.AuthRepository
import com.example.shopify.data.repositories.authentication.IAuthRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.test.runTest
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.*
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
class SignupViewModelTest {
    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val mainDispatcherRule = MainCoroutineRule()

    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var authClient: IAuthenticationClient
    private lateinit var authRepository: IAuthRepository
    lateinit var viewModel: SignupViewModel
    val customer = Customer(
        firstName = "firstName",
        lastName = "secondName",
        email = "email@gmail.com",
        phone = "01245336628",
        verifiedEmail = true,
        addresses = listOf(Address(address1 = "country/city", phone = "01245336628")),
        password = "Password123#",
        passwordConfirmation = "Password123#",
        sendEmailWelcome = false
    )
    private var clientResponse = CustomerRequestBody(customer)
    val testingContext: Application = ApplicationProvider.getApplicationContext()

    @Before
    fun setUp() {
        authClient = FakeAuthenticationClientTest(clientResponse)
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(testingContext)
        authRepository = AuthRepository(authClient, sharedPreferences)
        viewModel = SignupViewModel(authRepository)
    }

    @After
    fun tearDown() {
    }

    @Test
    fun registerUserToShopify_customerRequestBody_notNullResult() = runTest {
            //Given -> customer request body
            //When -> calling  Object
            viewModel.registerUserToShopify(clientResponse)
            val result= async { viewModel.authResponse.first() }
            val resultState = result.await()
             advanceUntilIdle()

            //Then -> The result must be not null
            assertThat(resultState, isA(AuthenticationResponseState.Success::class.java))

        }

    @Test
    fun registerUserToFirebase_emailAndPassword_notNullResult() =
        mainDispatcherRule.runBlockingTest {
            //Given email and password
            val email = "email@gmail.com"
            val password = "Email1234#"
            val customerId = 123456789L
            //When calling  Object
            viewModel.registerUserToFirebase(email, password, customerId)
            val result= async { viewModel.authResponse.first() }
            val resultState = result.await()
            advanceUntilIdle()

            //Then -> The result must be not null
            assertThat(resultState, isA(AuthenticationResponseState.Success::class.java))
        }
}