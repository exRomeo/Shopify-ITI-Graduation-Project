package com.example.shopify.presentation.screens.authentication.registeration

import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.shopify.BuildConfig
import com.example.shopify.R
import com.example.shopify.core.navigation.Screens
import com.example.shopify.core.utils.Constant
import com.example.shopify.core.utils.CredentialsValidator
import com.example.shopify.data.models.Address
import com.example.shopify.data.models.Customer
import com.example.shopify.data.models.CustomerRequestBody
import com.example.shopify.presentation.screens.authentication.common_auth_components.AuthenticationTextField
import com.example.shopify.presentation.screens.authentication.common_auth_components.TextFieldType
import com.example.shopify.presentation.screens.authentication.common_auth_components.AuthenticationButton
import com.example.shopify.ui.theme.IbarraFont
import com.example.shopify.ui.theme.backgroundColor
import com.example.shopify.ui.theme.facebookBackground
import com.example.shopify.ui.theme.hintColor
import com.example.shopify.ui.theme.ibarraBold
import com.example.shopify.ui.theme.ibarraRegular
import com.example.shopify.ui.theme.mainColor
import com.example.shopify.ui.theme.textColor
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.GoogleAuthProvider

@Composable
fun SignupContentScreen(
    signupViewModel: SignupViewModel,
    firstName: String,
    onFirstNameChanged: (String) -> Unit,
    secondName: String,
    onSecondNameChanged: (String) -> Unit,
    email: String,
    onEmailChanged: (String) -> Unit,
    phone: String,
    onPhoneChanged: (String) -> Unit,
    address: String,
    onAddressChanged: (String) -> Unit,
    password: String,
    onPasswordChanged: (String) -> Unit,
    confirmPassword: String,
    onConfirmPasswordChanged: (String) -> Unit,
    isDataEntered: Boolean,
    errorResponse: String,
    signupNavController: NavController
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .background(MaterialTheme.colorScheme.background)
            .padding(start = 24.dp, top = 10.dp, end = 32.dp)
    ) {
        OutlinedButton(
            onClick = {
                signupNavController.popBackStack()
                signupNavController.navigate(route = Screens.Login.route , builder = {
                    popUpTo(route = Screens.Login.route){
                        inclusive = true
                    }
                })
            },
            shape = CircleShape,
            border = BorderStroke(1.dp, Color.White),
            colors = ButtonDefaults.outlinedButtonColors(
                containerColor = backgroundColor,
                contentColor = MaterialTheme.colorScheme.onBackground
            ),
        ) {
            Icon(
                imageVector = Icons.Outlined.ArrowBack,
                contentDescription = stringResource(id = R.string.back),
            )
        }
        Text(
            text = stringResource(id = R.string.create_account),
            style = ibarraBold,
            color = textColor,
            fontSize = 32.sp
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = stringResource(id = R.string.lets_create_account),
            style = ibarraRegular,
            color = hintColor,
            fontSize = 18.sp
        )
        Spacer(modifier = Modifier.height(12.dp))
        Row(
            horizontalArrangement = Arrangement.SpaceEvenly,
            modifier = Modifier.fillMaxWidth()
        ) {
            AuthenticationTextField(
                modifier = Modifier
                    .height(72.dp)
                    .weight(1f)
                    // .fillMaxWidth(0.5f)
                    .background(Color.Transparent),
                text = firstName,
                errorMsg = R.string.first_name_is_required,
                hintId = R.string.first_name,
                onValueChange = onFirstNameChanged,
                textFieldType = TextFieldType.FirstName
            )
            Spacer(modifier = Modifier.width(8.dp))
            AuthenticationTextField(
                modifier = Modifier
                    .height(72.dp)
                    .weight(1f)
                    // .fillMaxWidth(0.5f)
                    .background(Color.Transparent),
                text = secondName,
                errorMsg = R.string.second_name_is_required,
                hintId = R.string.second_name,
                onValueChange = onSecondNameChanged,
                textFieldType = TextFieldType.SecondName
            )
        }

        Spacer(modifier = Modifier.height(8.dp))
        AuthenticationTextField(
            modifier = Modifier
                .height(72.dp)
                .fillMaxWidth()
                .background(Color.Transparent),
            text = email,
            errorMsg = R.string.email_is_required,
            validationErrorMsg = stringResource(id = R.string.email_is_not_valid),
            isValid = CredentialsValidator.isValidEmail(email),
            hintId = R.string.email,
            onValueChange = onEmailChanged,
            textFieldType = TextFieldType.Email
        )
        Spacer(modifier = Modifier.height(8.dp))
        AuthenticationTextField(
            modifier = Modifier
                .height(72.dp)
                .fillMaxWidth()
                .background(Color.Transparent),
            text = phone,
            errorMsg = R.string.phone_is_required,
            validationErrorMsg = stringResource(id = R.string.phone_is_not_valid),
            isValid = CredentialsValidator.isPhoneNumberValid(phone),
            hintId = R.string.phone,
            onValueChange = onPhoneChanged,
            textFieldType = TextFieldType.Phone
        )
        Spacer(modifier = Modifier.height(8.dp))
        AuthenticationTextField(
            modifier = Modifier
                .height(72.dp)
                .fillMaxWidth()
                .background(Color.Transparent),
            text = address,
            errorMsg = R.string.address_is_required,
            hintId = R.string.address,
            onValueChange = onAddressChanged,
            textFieldType = TextFieldType.Address
        )
        Spacer(modifier = Modifier.height(8.dp))
        AuthenticationTextField(
            modifier = Modifier
                .height(72.dp)
                .fillMaxWidth()
                .background(Color.Transparent),
            text = password,
            errorMsg = R.string.password_is_required,
            validationErrorMsg = stringResource(id = R.string.password_is_not_valid),
            isValid = CredentialsValidator.isValidPassword(password),
            hintId = R.string.password,
            onValueChange = onPasswordChanged,
            textFieldType = TextFieldType.Password
        )
        Spacer(modifier = Modifier.height(8.dp))
        AuthenticationTextField(
            modifier = Modifier
                .height(72.dp)
                .fillMaxWidth()
                .background(Color.Transparent),
            text = confirmPassword,
            errorMsg = R.string.confirmPassword,
            validationErrorMsg = stringResource(id = R.string.confirm_password_is_not_match),
            isValid = (confirmPassword == password),
            hintId = R.string.confirmPassword,
            onValueChange = onConfirmPasswordChanged,
            textFieldType = TextFieldType.ConfirmPassword
        )
        if (errorResponse.isNotEmpty()) {
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = errorResponse,
                style = ibarraRegular,
                color = MaterialTheme.colorScheme.error,
                fontSize = 14.sp
            )
        }
        Spacer(modifier = Modifier.height(16.dp))
        AuthenticationButton(
            modifier = Modifier
                .fillMaxWidth(0.9f)
                .height(48.dp)
                .align(Alignment.CenterHorizontally),
            color = mainColor,
            textId = R.string.signup,
            elevation = 8.dp,
            isEnabled = isDataEntered,
            textStyle = TextStyle(
                fontFamily = IbarraFont,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.background,
                fontSize = 18.sp
            )
        ) {
            when (dataIsValid(email, phone, password, confirmPassword)) {
                true -> signupViewModel.registerUserToShopify(
                    requestBody =
                    CustomerRequestBody(
                        customer = Customer(
                            firstName = firstName,
                            lastName = secondName,
                            email = email,
                            phone = phone,
                            verifiedEmail = true,
                            addresses = listOf(Address(address1 = address, phone = phone)),
                            password = password,
                            passwordConfirmation = confirmPassword,
                            sendEmailWelcome = false
                        )
                    )
                )//signupViewModel.registerUser(email, password) //Log.i("TAG", "SignupScreen: is valid")
                false -> Log.i("TAG", "SignupScreen: is not valid")//
            }

        }
    }

}

fun dataIsValid(email: String, phone: String, password: String, confirmPassword: String): Boolean {
    var isValid = false
    // var msg: String = ""
    if (CredentialsValidator.isValidEmail(email)
        && CredentialsValidator.isPhoneNumberValid(phone)
        && CredentialsValidator.isValidPassword(password)
    ) {
        if (password == confirmPassword) {
            isValid = true
        }
    }
    return isValid
}
/*
@Composable
@Preview
fun SignupScreenPreview() {
    SignupScreen(/*SignupViewModel()*/)
}*/