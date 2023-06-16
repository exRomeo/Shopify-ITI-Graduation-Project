package com.example.shopify.presentation.common.composables

import androidx.annotation.StringRes
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.material3.*
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.shopify.R
import com.example.shopify.presentation.screens.authentication.common_auth_components.TextFieldType
import com.example.shopify.ui.theme.hintColor

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomTextField(
    modifier: Modifier,
    text: String,
    textStyle: TextStyle,
    @StringRes hintId: Int,
    hintTextStyle: TextStyle,
    onValueChange: (String) -> Unit,
    color: Color,
    cornerRadius: Dp = 0.dp,
    textFieldType: TextFieldType,
    isError: Boolean
) {

    var passwordVisible by remember {
        mutableStateOf(true)
    }
    var confirmPasswordVisible by remember {
        mutableStateOf(true)
    }
    var trailingId = when (textFieldType) {
        TextFieldType.Password -> R.drawable.visibility_off
        TextFieldType.ConfirmPassword -> R.drawable.visibility_off
        else -> null
    }
    if (trailingId != null) {
        trailingId = if (passwordVisible) R.drawable.visibility_off else R.drawable.visibility_on
    }
    TextField(
        modifier = modifier
            .fillMaxWidth(),
        value = text,
        textStyle = textStyle,
        onValueChange = onValueChange,
       // isError = isError,
        label = {
            Text(
                text = stringResource(id = hintId),
                style = hintTextStyle
            )
        },
        shape = RoundedCornerShape(cornerRadius),
        colors = TextFieldDefaults.textFieldColors(
            containerColor = color,
            cursorColor = hintColor,
            unfocusedIndicatorColor = Color.Transparent,
            disabledIndicatorColor = Color.Transparent,
            focusedIndicatorColor = Color.Transparent
        ),
        visualTransformation = if (textFieldType == TextFieldType.Password || textFieldType == TextFieldType.ConfirmPassword) {
            if (passwordVisible) PasswordVisualTransformation() else VisualTransformation.None
        } else VisualTransformation.None,

        trailingIcon = {
            if (trailingId != null) {
                Icon(
                    modifier = Modifier.clickable {
                        passwordVisible = !passwordVisible
                        confirmPasswordVisible = !confirmPasswordVisible
                    },
                    painter = painterResource(id = trailingId), contentDescription = stringResource(
                        id = R.string.password
                    ),
                    tint = hintColor
                )
            }
        }

    )
}