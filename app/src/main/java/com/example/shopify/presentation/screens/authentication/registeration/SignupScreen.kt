package com.example.shopify.presentation.screens.authentication.registeration

import androidx.compose.foundation.background
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.shopify.R
import com.example.shopify.presentation.screens.authentication.AuthenticationTextField
import com.example.shopify.presentation.screens.authentication.TextFieldType
import com.example.shopify.presentation.screens.authentication.AuthenticationButton
import com.example.shopify.ui.theme.IbarraFont
import com.example.shopify.ui.theme.Pink80
import com.example.shopify.ui.theme.backgroundColor
import com.example.shopify.ui.theme.facebookBackground
import com.example.shopify.ui.theme.hintColor
import com.example.shopify.ui.theme.ibarraBold
import com.example.shopify.ui.theme.ibarraRegular
import com.example.shopify.ui.theme.mainColor
import com.example.shopify.ui.theme.textColor

@Composable
fun SignupScreen(signupViewModel: SignupViewModel) {
    var firstName by remember {
        mutableStateOf("")
    }
    var secondName by remember {
        mutableStateOf("")
    }
    var email by remember {
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
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(start = 24.dp, top = 60.dp, end = 32.dp)
    ) {
        /*OutlinedButton(
            onClick = {
            },
            shape = CircleShape,
            border= BorderStroke(1.dp, Color.White),
            colors = ButtonDefaults.outlinedButtonColors(
                containerColor = backgroundColor,
                contentColor = MaterialTheme.colorScheme.background
            ),
        ) {
            Icon(
                imageVector = Icons.Outlined.ArrowBack,
                contentDescription = null,
            )
        }*/
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
        Spacer(modifier = Modifier.height(24.dp))
        Row(horizontalArrangement = Arrangement.SpaceEvenly,
            modifier = Modifier.fillMaxWidth()) {
            AuthenticationTextField(
                modifier = Modifier
                    .height(48.dp)
                    .weight(1f)
                    //  .fillMaxWidth(0.5f)
                    .background(Color.Transparent),
                text = firstName,
                hintId = R.string.first_name,
                onValueChange = { firstName = it },
                textFieldType = TextFieldType.FirstName
            )
            Spacer(modifier = Modifier.width(8.dp))
            AuthenticationTextField(
                modifier = Modifier
                    .height(48.dp)
                    .weight(1f)
                    //    .fillMaxWidth(0.5f)
                    .background(Color.Transparent),
                text = secondName,
                hintId = R.string.second_name,
                onValueChange = { secondName = it },
                textFieldType = TextFieldType.SecondName
            )
        }

        Spacer(modifier = Modifier.height(8.dp))
        AuthenticationTextField(
            modifier = Modifier
                .height(48.dp)
                .fillMaxWidth()
                .background(Color.Transparent),
            text = email,
            hintId = R.string.email,
            onValueChange = { email = it },
            textFieldType = TextFieldType.Email
        )
        Spacer(modifier = Modifier.height(8.dp))
        AuthenticationTextField(
            modifier = Modifier
                .height(48.dp)
                .fillMaxWidth()
                .background(Color.Transparent),
            text = phone,
            hintId = R.string.phone,
            onValueChange = { phone = it },
            textFieldType = TextFieldType.Phone
        )
        Spacer(modifier = Modifier.height(8.dp))
        AuthenticationTextField(
            modifier = Modifier
                .height(48.dp)
                .fillMaxWidth()
                .background(Color.Transparent),
            text = password,
            hintId = R.string.password,
            onValueChange = { password = it },
            textFieldType = TextFieldType.Password
        )
        Spacer(modifier = Modifier.height(8.dp))
        AuthenticationTextField(
            modifier = Modifier
                .height(48.dp)
                .fillMaxWidth()
                .background(Color.Transparent),
            text = confirmPassword,
            hintId = R.string.confirmPassword,
            onValueChange = { confirmPassword = it },
            textFieldType = TextFieldType.ConfirmPassword
        )
        Spacer(modifier = Modifier.height(32.dp))
        AuthenticationButton(
            modifier = Modifier
                .fillMaxWidth(0.9f)
                .height(48.dp)
                .align(Alignment.CenterHorizontally),
            color = mainColor,
            textId = R.string.signup,
            elevation = 5.dp,
            textStyle = TextStyle(
                fontFamily = IbarraFont,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.background,
                fontSize = 18.sp
            )
        ) {
            println("email is $email , password is $password")
        }
        Spacer(modifier = Modifier.height(8.dp))
        /*Row(
            //horizontalArrangement = Arrangement.Center,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        ) {
            Text(
                text = stringResource(id = R.string.dont_have_an_account),
                style = ibarraBold,
                color = hintColor,
                fontSize = 16.sp
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = stringResource(id = R.string.signup),
                style = ibarraRegular,
                color = mainColor,
                fontSize = 14.sp
            )
        }*/
        Spacer(modifier = Modifier.height(48.dp))
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth(0.7f)
                .align(Alignment.CenterHorizontally)
        ) {

            Divider(
                color = hintColor,
                thickness = 0.7.dp,
                modifier = Modifier.weight(1f)
            )
            Text(
                text = stringResource(id = R.string.or_sign_up_with),
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
                println("email is $email , password is $password")
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
    @Composable
    @Preview
    fun SignupScreenPreview() {
        SignupScreen(SignupViewModel())
    }