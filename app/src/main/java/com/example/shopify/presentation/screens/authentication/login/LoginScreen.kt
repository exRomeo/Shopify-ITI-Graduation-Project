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
            if (CurrentUserHelper.isLoggedIn()) {
                LaunchedEffect(key1 = authState) {
                    error = "ERROR"
                    Log.i("TAG", "NAVIGATE TO HOME SCREEN")
                    loginNavController.popBackStack()
                    loginNavController.navigate(route = Screens.Home.route, builder = {
                        popUpTo(route = Screens.Home.route) {
                            inclusive = true
                        }
                    })
                }
            } else {
                LottieAnimation(animation = R.raw.shopping_cart_loading_animation)
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
            Log.i("TAG", " ERROR ${authResponse.message}")

        }

        else -> {
            Log.i("TAG", "THERE'S SOMETHING WRONG ")
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
    }

    when (googleState.success != null) {
        true -> {
            LaunchedEffect(googleState) {
                loginNavController.navigate(Screens.Home.route)
            }
        }

        false -> error = googleState.error

    }

    if (ConnectionUtil.isConnected()) {
        if (authRepository.checkedLoggedIn()) {
            Log.i("TAG", "Check logged in: ${authRepository.checkedLoggedIn()}")
            LaunchedEffect(key1 = Unit) {
                loginNavController.navigate(Screens.Home.route)
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

}