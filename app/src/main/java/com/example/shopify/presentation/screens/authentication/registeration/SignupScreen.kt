package com.example.shopify.presentation.screens.authentication.registeration

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
import com.example.shopify.Utilities.ShopifyApplication
import com.example.shopify.core.helpers.AuthenticationResponseState
import com.example.shopify.data.repositories.authentication.IAuthRepository

@Composable
fun SignupScreen(signupNavController: NavController) {
    var firstName by remember {
        mutableStateOf("")
    }
    var secondName by remember {
        mutableStateOf("")
    }
    var email by remember {
        mutableStateOf("")
    }
    var address by remember {
        mutableStateOf("")
    }
    var phone by remember {
        mutableStateOf("")
    }
    var password by remember {
        mutableStateOf("")
    }
    var confirmPassword by remember {
        mutableStateOf("")
    }
    var error by rememberSaveable {
        mutableStateOf("")
    }
    val isDataEntered by remember {
        derivedStateOf {
            firstName != "" && secondName != "" &&
                    email != "" && phone != "" && address != "" && password != "" && confirmPassword != ""
                    && (password == confirmPassword)
        }
    }
    val authRepository: IAuthRepository =
        (LocalContext.current.applicationContext as ShopifyApplication).authRepository
    val signupViewModelFactory = SignupViewModelFactory(authRepository)
    val signupViewModel: SignupViewModel = viewModel(factory = signupViewModelFactory)
    val authState by signupViewModel.authResponse.collectAsState()

    when (val authResponse = authState) {
        is AuthenticationResponseState.Success -> {
            LaunchedEffect(key1 = authState) {
                Log.i("TAG", "NAVIGATE TO LOGIN SCREEN")
                signupNavController.popBackStack()
            }

        }

        is AuthenticationResponseState.Loading -> {
            error = ""
            Log.i("TAG", " LOADING")
        }

        is AuthenticationResponseState.Error -> {
            error = stringResource(id = R.string.email_password_must_be_unique)
            Log.i("TAG", " ERROR ${authResponse.message}")
        }

        else -> {
            error = stringResource(id = R.string.something_is_wrong)
            Log.i("TAG", "THERE'S SOMETHING WRONG ")
        }
    }
    SignupContentScreen(
        signupViewModel = signupViewModel,
        firstName = firstName,
        onFirstNameChanged = { firstName = it },
        secondName = secondName,
        onSecondNameChanged = { secondName = it },
        email = email,
        onEmailChanged = { email = it },
        phone = phone,
        onPhoneChanged = { phone = it },
        address = address,
        onAddressChanged = { address = it },
        password = password,
        onPasswordChanged = { password = it },
        confirmPassword = confirmPassword,
        onConfirmPasswordChanged = { confirmPassword = it },
        isDataEntered = isDataEntered,
        errorResponse = error,
        signupNavController = signupNavController
    )
}