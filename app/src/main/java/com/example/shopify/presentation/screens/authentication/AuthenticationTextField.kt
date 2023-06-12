package com.example.shopify.presentation.screens.authentication

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
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
    @StringRes errorMsg : Int,
    validationErrorMsg :String? = "",
    @StringRes hintId : Int,
    isValid : Boolean = true,
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
    Column(modifier = modifier) {
        CustomTextField(
            modifier = Modifier.height(48.dp),
            text = text,
            textStyle = textStyle,
            hintId = hintId,
            hintTextStyle =  hintStyle,
            onValueChange = onValueChange,
            color = backgroundColor,
            cornerRadius = 20.dp,
            textFieldType = textFieldType
        )
        if (text == "") {
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = stringResource(id = errorMsg),
                style = TextStyle(color = MaterialTheme.colorScheme.error)
            )
        }
        if (!isValid) {
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = validationErrorMsg ?: "",
                style = TextStyle(color = MaterialTheme.colorScheme.error)
            )
        }
    }

}