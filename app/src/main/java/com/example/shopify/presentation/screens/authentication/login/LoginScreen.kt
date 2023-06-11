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
import androidx.navigation.NavController
import com.example.shopify.core.helpers.AuthenticationResponseState

@Composable
fun LoginScreen(loginViewModel: LoginViewModel/*, loginNavController: NavController*/) { //state hoisting move state to the caller of composable
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
    val authState by loginViewModel.authResponse.collectAsState()
    when (val authResponse = authState) {
        is AuthenticationResponseState.Success -> {
            error = ""
            Log.i("TAG", "NAVIGATE TO HOME SCREEN")
            //loginNavController.navigate(route = Screens.Login.route)
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
        //loginNavController = loginNavController
    )
}