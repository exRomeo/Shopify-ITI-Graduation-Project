package com.example.shopify.presentation.screens.authentication.login

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.shopify.R
import com.example.shopify.core.helpers.AuthenticationResponseState
import com.example.shopify.core.navigation.Screens
import com.example.shopify.core.utils.Constant
import com.example.shopify.presentation.screens.authentication.AuthenticationButton
import com.example.shopify.presentation.screens.authentication.AuthenticationTextField
import com.example.shopify.presentation.screens.authentication.TextFieldType
import com.example.shopify.ui.theme.IbarraFont
import com.example.shopify.ui.theme.facebookBackground
import com.example.shopify.ui.theme.hintColor
import com.example.shopify.ui.theme.ibarraBold
import com.example.shopify.ui.theme.ibarraRegular
import com.example.shopify.ui.theme.mainColor
import com.example.shopify.ui.theme.textColor
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions


@Composable
fun LoginContentScreen(
    loginViewModel: LoginViewModel,
    email: String,
    onEmailChanged: (String) -> Unit,
    password: String,
    onPasswordChanged: (String) -> Unit,
    isDataEntered: Boolean,
    errorResponse: String,
     loginNavController : NavController
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(start = 24.dp, top = 100.dp, end = 32.dp)
    ) {
        Text(
            text = stringResource(id = R.string.welcome_to),
            style = ibarraBold,
            color = textColor,
            fontSize = 32.sp
        )
        Text(
            text = "Shopify",
            style = ibarraBold,
            color = mainColor,
            fontSize = 32.sp
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = stringResource(id = R.string.please_enter_your_details),
            style = ibarraRegular,
            color = hintColor,
            fontSize = 18.sp
        )

        Spacer(modifier = Modifier.height(16.dp))
        AuthenticationTextField(
            modifier = Modifier
                .height(72.dp)
                .fillMaxWidth()
                .align(CenterHorizontally)
                .background(Color.Transparent),
            text = email,
            errorMsg = R.string.email_is_required,
            hintId = R.string.email,
            onValueChange = onEmailChanged,
            textFieldType = TextFieldType.Email
        )

        Spacer(modifier = Modifier.height(16.dp))
        AuthenticationTextField(
            modifier = Modifier
                .height(72.dp)
                .fillMaxWidth()
                .align(CenterHorizontally)
                .background(Color.Transparent),
            text = password,
            errorMsg = R.string.password_is_required,
            hintId = R.string.password,
            onValueChange = onPasswordChanged,
            textFieldType = TextFieldType.Password
        )

        Spacer(modifier = Modifier.height(2.dp))
        Box(Modifier.fillMaxWidth()) {
            Text(
                modifier = Modifier.align(Alignment.TopEnd),
                text = stringResource(id = R.string.forget_password),
                style = ibarraRegular,
                color = mainColor,
                fontSize = 14.sp
            )
        }
        if (errorResponse.isNotEmpty()) {
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = errorResponse,
                style = ibarraRegular,
                color = MaterialTheme.colorScheme.error,
                fontSize = 14.sp
            )
        }
        Spacer(modifier = Modifier.height(32.dp))
        AuthenticationButton(
            modifier = Modifier
                .fillMaxWidth(0.9f)
                .height(48.dp)
                .align(CenterHorizontally),
            color = mainColor,
            textId = R.string.login,
            elevation = 5.dp,
            isEnabled = isDataEntered,
            textStyle = TextStyle(
                fontFamily = IbarraFont,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.background,
                fontSize = 18.sp
            )
        ) {
            loginViewModel.loginUser(email, password)
            println("email is $email , password is $password")
        }
        Spacer(modifier = Modifier.height(8.dp))
        Row(
            //horizontalArrangement = Arrangement.Center,
            modifier = Modifier.align(CenterHorizontally)
        ) {
            Text(
                text = stringResource(id = R.string.dont_have_an_account),
                style = ibarraBold,
                color = hintColor,
                fontSize = 16.sp
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                modifier = Modifier.clickable {
                    loginNavController.navigate(Screens.Signup.route)
                },
                text = stringResource(id = R.string.signup),
                style = ibarraRegular,
                color = mainColor,
                fontSize = 14.sp
            )
        }
        Spacer(modifier = Modifier.height(48.dp))
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth(0.7f)
                .align(CenterHorizontally)
        ) {

            Divider(
                color = hintColor,
                thickness = 0.7.dp,
                modifier = Modifier.weight(1f)
            )
            Text(
                text = stringResource(id = R.string.or_sign_in_with),
                style = ibarraRegular,
                color = hintColor,
                fontSize = 12.sp
            )
            Divider(
                color = hintColor,
                thickness = 0.7.dp,
                modifier = Modifier.weight(1f)
            )
        }
        Spacer(modifier = Modifier.height(32.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            AuthenticationButton(
                modifier = Modifier
                    .width(150.dp)
                    .height(36.dp),
                color = Color.White,
                imageId = R.drawable.google,
                textId = R.string.google,
                elevation = 20.dp,
                textStyle = TextStyle(
                    fontFamily = IbarraFont,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.Black,
                    fontSize = 14.sp
                )
            ) {
//                val google = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
//                    .requestEmail()
//                    .requestIdToken(Constant.webClient)
////                    .requestProfile()
//                    .build()
//                val googleSignInClient = GoogleSignIn.getClient(google)
//                loginViewModel.
//                println("email is $email , password is $password")
            }
            AuthenticationButton(
                modifier = Modifier
                    .width(150.dp)
                    .height(36.dp),
                color = facebookBackground,
                imageId = R.drawable.facebook,
                textId = R.string.facebook,
                elevation = 5.dp,
                textStyle = TextStyle(
                    fontFamily = IbarraFont,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.White,
                    fontSize = 14.sp
                )
            ) {
                println("email is $email , password is $password")
            }
        }

    }
}
/*
@Composable
@Preview
fun LoginScreenPreview() {
     LoginScreen()
}*/