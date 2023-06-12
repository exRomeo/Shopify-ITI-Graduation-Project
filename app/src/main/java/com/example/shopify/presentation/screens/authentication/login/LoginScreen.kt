package com.example.shopify.presentation.screens.authentication.login

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.shopify.core.helpers.AuthenticationResponseState
import com.example.shopify.core.helpers.RetrofitHelper
import com.example.shopify.core.navigation.Screens
import com.example.shopify.core.utils.SharedPreference
import com.example.shopify.data.remote.authentication.AuthenticationClient
import com.example.shopify.data.repositories.authentication.AuthRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

private const val BASE_URL = "https://mad43-alex-and-team2.myshopify.com/"
private const val CUSTOMER_PREF_NAME = "customer"
@Composable
fun LoginScreen( loginNavController: NavController) { //state hoisting move state to the caller of composable

    var email by rememberSaveable { //for configuration change
        mutableStateOf("")
    }

    var password by rememberSaveable {
        mutableStateOf("")
    }
    var error by rememberSaveable {
        mutableStateOf("")
    }
    val isDataEntered by remember {
        derivedStateOf {
            email != "" && password != ""
        }
    }
    val loginViewModelFactory =
        LoginViewModelFactory(
            AuthRepository(
                AuthenticationClient(
                    RetrofitHelper.getAuthenticationService(BASE_URL),
                    FirebaseAuth.getInstance(),
                    FirebaseFirestore.getInstance()
                ),
                SharedPreference.customPreference(LocalContext.current.applicationContext, CUSTOMER_PREF_NAME)
            )
        )
    val loginViewModel: LoginViewModel = viewModel(factory = loginViewModelFactory)

    val authState by loginViewModel.authResponse.collectAsState()
    when (val authResponse = authState) {
        is AuthenticationResponseState.Success -> {
            error = ""
            Log.i("TAG", "NAVIGATE TO HOME SCREEN")
            loginNavController.navigate(route = Screens.Home.route)
        }

        is AuthenticationResponseState.Loading -> {
            Log.i("TAG", " LOADING")
        }

        is AuthenticationResponseState.Error -> {
            Log.i("TAG", " ERROR ${authResponse.message}")
            error = authResponse.message.toString()
        }

        else -> {
            Log.i("TAG", "THERE'S SOMETHING WRONG ")
        }
    }
    LoginContentScreen(
        loginViewModel = loginViewModel,
        email = email,
        onEmailChanged = { email = it },
        password = password,
        onPasswordChanged = { password = it },
        isDataEntered = isDataEntered,
        errorResponse = error,
        loginNavController = loginNavController
    )
}