package com.example.shopify.presentation.common.composables

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextDecoration
import androidx.navigation.NavHostController
import com.example.shopify.R
import com.example.shopify.core.navigation.Screens

@Composable
fun NotLoggedInScreen(modifier: Modifier = Modifier, navController: NavHostController) {
    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = stringResource(id = R.string.login_request_message), color = Color.Gray)
        TextButton(onClick = {
            navController.navigate(Screens.Login.route, builder = {
                popUpToRoute
            })
        }
        ) {
            Text(
                text = stringResource(id = R.string.login),
                style = TextStyle(textDecoration = TextDecoration.Underline)
            )
        }
    }
}