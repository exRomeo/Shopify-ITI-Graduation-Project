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
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.shopify.R
import com.example.shopify.utilities.ShopifyApplication
import com.example.shopify.core.helpers.AuthenticationResponseState
import com.example.shopify.core.helpers.CurrentUserHelper
import com.example.shopify.core.navigation.Screens
import com.example.shopify.core.utils.ConnectionUtil
import com.example.shopify.data.models.GoogleSignInState
import com.example.shopify.data.repositories.authentication.IAuthRepository
import com.example.shopify.presentation.common.composables.LottieAnimation
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
    val loginViewModelFactory = LoginViewModelFactory(authRepository)
    val loginViewModel: LoginViewModel = viewModel(factory = loginViewModelFactory)
    val authState by loginViewModel.authResponse.collectAsState()
    val googleState by loginViewModel.googleState.collectAsState()

    when (val authResponse = authState) {
        is AuthenticationResponseState.Success -> {
            LaunchedEffect(key1 = authState) {
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
            LottieAnimation(animation = R.raw.shopping_cart_loading_animation)
        }

        is AuthenticationResponseState.Error -> {
            when (authResponse.message) {
                is IOException -> {
                    error = stringResource(id = R.string.please_check_network)
                    showNetworkDialog = true
                    if (showNetworkDialog)
                        ShowCustomDialog(
                            title = R.string.network_connection,
                            description = R.string.not_connection,
                            buttonText = R.string.tryAgain,
                            animatedId = R.raw.no_network_error_page_with_cat,
                            onClickButton = { showNetworkDialog = false },
                            onClose = {
                                showNetworkDialog = false
                            }
                        )
                }

                else -> error = stringResource(id = R.string.please_check_email_password)
            }

        }

        else -> {
            showNetworkDialog = true
            if (showNetworkDialog)
                ShowCustomDialog(
                    title = R.string.something_is_wrong,
                    description = R.string.something_is_wrong,
                    buttonText = R.string.tryAgain,
                    animatedId = R.raw.sign_for_error_or_explanation_alert,
                    onClickButton = { showNetworkDialog = false },
                    onClose = {
                        showNetworkDialog = false
                    }
                )
        }
    }

    when (googleState) {
        is GoogleSignInState -> {
            if (googleState.success != null) {
                LaunchedEffect(googleState) {
                    Log.i("TAG", "Google navigate: ")
                    loginNavController.navigate(Screens.Home.route)
                }
            } else {
                Log.i("TAG", "LoginScreen: Error in google")
            }
        }
    }

    if (ConnectionUtil.isConnected()) {
        if (loginViewModel.checkedLoggedIn()) {
            Log.i("TAG", "Check logged in: ${authRepository.checkedLoggedIn()}")
            LaunchedEffect(key1 = Unit) {
                loginNavController.navigate(Screens.Home.route)
            }
        }
    } else {
        showNetworkDialog = true
        if (showNetworkDialog)
            ShowCustomDialog(
                title = R.string.network_connection,
                description = R.string.not_connection,
                buttonText = R.string.tryAgain,
                animatedId = R.raw.no_network_error_page_with_cat,
                onClickButton = { showNetworkDialog = false },
                onClose = {
                    showNetworkDialog = false
                }
            )
    }
    Log.i("TAG", "Error in gener: $error")
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