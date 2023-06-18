package com.example.shopify.presentation.screens.authentication.login

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import com.example.shopify.Utilities.ShopifyApplication
import com.example.shopify.core.helpers.AuthenticationResponseState
import com.example.shopify.core.navigation.Screens
import com.example.shopify.data.repositories.authentication.IAuthRepository

@Composable
fun LoginScreen(loginNavController: NavController) { //state hoisting move state to the caller of composable

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
    val authRepository: IAuthRepository =
        (LocalContext.current.applicationContext as ShopifyApplication).authRepository
    val loginViewModelFactory = LoginViewModelFactory(authRepository)
    val loginViewModel: LoginViewModel = viewModel(factory = loginViewModelFactory)

    val authState by loginViewModel.authResponse.collectAsState()
    when (val authResponse = authState) {
        is AuthenticationResponseState.Success -> {
            LaunchedEffect(key1 = authState) {
                error = ""
                Log.i("TAG", "NAVIGATE TO HOME SCREEN")
                loginNavController.popBackStack()
                loginNavController.navigate(route = Screens.Home.route, builder = {
                    popUpTo(route = Screens.Home.route) {
                        inclusive = true
                    }
                })

            }
        }

        is AuthenticationResponseState.Loading -> {
            Log.i("TAG", " LOADING IN LOGIN SCREEN")
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