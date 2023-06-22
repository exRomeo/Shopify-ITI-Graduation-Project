package com.example.shopify.presentation.screens.authentication.login

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
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.*
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.test.runBlockingTest


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
    lateinit var viewModel: LoginViewModel
    private val customer = Customer(
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
    private val testingContext: Application = ApplicationProvider.getApplicationContext()

    @Before
    fun setUp() {
        authClient = FakeAuthenticationClientTest(clientResponse)
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(testingContext)
        authRepository = AuthRepository(authClient, sharedPreferences)
        viewModel = LoginViewModel(authRepository)
    }

    @After
    fun tearDown() {
    }

    @Test
    fun loginUser_emailAndPassword_notNullResult() = runTest{
        //Given -> right email and password
        val email = "nadaelshafey@yahoo.com"
        val password = "Nada123#"

        //When -> calling  loginUser()
        viewModel.loginUser(email, password)
        val result= async { viewModel.authResponse.drop(1).first() }
        val resultState = result.await()
        advanceUntilIdle()

        //Then -> The result must be success
        assertThat(resultState, isA(AuthenticationResponseState.Success::class.java))

    }
}