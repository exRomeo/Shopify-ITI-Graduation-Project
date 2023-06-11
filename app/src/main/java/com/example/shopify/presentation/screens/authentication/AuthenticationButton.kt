package com.example.shopify.presentation.screens.authentication

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.shopify.presentation.components.CustomCornerButton

@Composable
fun AuthenticationButton(
    modifier: Modifier,
    color: Color,
    @DrawableRes imageId: Int? = null,
    @StringRes textId: Int,
    elevation : Dp,
    isEnabled : Boolean? = null,
    textStyle: TextStyle,
    onClick: () -> Unit,

    ) {
    CustomCornerButton(
        modifier = modifier,
        color = color,
        cornerRadius = 10.dp,
        elevation = elevation,
        onClick = onClick,
        isEnabled = isEnabled ?: true,
        imageId = imageId,
        textId = textId,
        textStyle = textStyle
    )
}