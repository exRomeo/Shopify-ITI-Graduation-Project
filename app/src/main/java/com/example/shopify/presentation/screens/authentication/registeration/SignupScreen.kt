package com.example.shopify.presentation.screens.authentication.registeration

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
fun SignupScreen(signupViewModel: SignupViewModel/*,signupNavController: NavController*/) {
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
    val authState by signupViewModel.authResponse.collectAsState()
    when (val authResponse = authState) {
        is AuthenticationResponseState.Success -> {
            error = ""
            Log.i("TAG", "NAVIGATE TO LOGIN SCREEN")
            // signupNavController.popBackStack()
        }

        is AuthenticationResponseState.Loading -> {
            error = ""
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
       // signupNavController = signupNavController
    )
}