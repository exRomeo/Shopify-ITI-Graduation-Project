package com.example.shopify.presentation.screens.authentication

import androidx.annotation.StringRes
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.shopify.presentation.components.CustomTextField
import com.example.shopify.ui.theme.IbarraFont
import com.example.shopify.ui.theme.backgroundColor
import com.example.shopify.ui.theme.hintColor

@Composable
fun AuthenticationTextField(
    modifier: Modifier,
    text : String,
    @StringRes hintId : Int,
    onValueChange : (String)-> Unit,
    textFieldType: TextFieldType

){
    val textStyle =  TextStyle(
        fontFamily = IbarraFont,
        fontWeight = FontWeight.Normal,
        color = Color.Black,
        fontSize = 14.sp
    )
    val hintStyle =  TextStyle(
        fontFamily = IbarraFont,
        fontWeight = FontWeight.Normal,
        color = hintColor,
        fontSize = 14.sp
    )
    CustomTextField(
        modifier = modifier,
        text = text,
        textStyle = textStyle,
        hintId = hintId,
        hintTextStyle =  hintStyle,
        onValueChange = onValueChange,
        color = backgroundColor,
        cornerRadius = 20.dp,
        textFieldType = textFieldType
    )
}