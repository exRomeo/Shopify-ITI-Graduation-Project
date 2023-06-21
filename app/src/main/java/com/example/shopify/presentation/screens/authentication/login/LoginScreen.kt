package com.example.shopify.presentation.screens.authentication.login

import android.content.SharedPreferences
import android.util.Log
import androidx.annotation.RawRes
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.shopify.R
import com.example.shopify.utilities.ShopifyApplication
import com.example.shopify.core.helpers.AuthenticationResponseState
import com.example.shopify.core.navigation.Screens
import com.example.shopify.core.utils.SharedPreference
import com.example.shopify.core.utils.SharedPreference.hasCompletedOnBoarding
import com.example.shopify.data.repositories.authentication.IAuthRepository
import com.example.shopify.presentation.common.composables.ShowCustomDialog
import java.io.IOException

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
    var showNetworkDialog by rememberSaveable { //for configuration change
        mutableStateOf(false)
    }
    val authRepository: IAuthRepository =
        (LocalContext.current.applicationContext as ShopifyApplication).authRepository
    val sharedPreference: SharedPreferences =
        (LocalContext.current.applicationContext as ShopifyApplication).sharedPreference
    val loginViewModelFactory = LoginViewModelFactory(authRepository)
    val loginViewModel: LoginViewModel = viewModel(factory = loginViewModelFactory)
    sharedPreference.hasCompletedOnBoarding = true
    val authState by loginViewModel.authResponse.collectAsState()
    val googleState by loginViewModel.googleState.collectAsState()
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
            when (authResponse.message) {
                is IOException ->
                    error = stringResource(id = R.string.please_check_network)

                else -> error = stringResource(id = R.string.please_check_email_password)
            }
            Log.i("TAG", " ERROR ${authResponse.message}")

        }

        else -> {
            Log.i("TAG", "THERE'S SOMETHING WRONG ")
        }
    }
    LaunchedEffect(googleState) {
        when (googleState.success != null) {
            true -> loginNavController.navigate(Screens.Home.route)

            false -> error = R.string.not_connection.toString()

        }

    }

    if (authRepository.checkedLoggedIn()) {
        try {
            showNetworkDialog = false
            loginNavController.navigate(Screens.Home.route)
        } catch (ex: Exception) {
            var title = R.string.something_is_wrong
            var description = R.string.something_is_wrong
            var animatedId = R.raw.error_animation
            showNetworkDialog = true
            when(ex){
                is IOException ->{
                    title =  R.string.network_connection
                    description = R.string.not_connection
                    animatedId = R.raw.custom_network_error
                }
            }
            Surface(color = Color.Gray) {
                ShowCustomDialog(
                    title = title,
                    description = description,
                    buttonText = R.string.tryAgain,
                    animatedId = animatedId,
                    onDismiss = { showNetworkDialog = false },
                    onClose = {
                        showNetworkDialog = false
                        loginNavController.popBackStack()
                    }
                )

            }
        }
    } else { //Not logged in
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
}