package com.example.shopify.presentation.common.composables

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.shopify.R

@Composable
fun CustomCornerButton(
    modifier: Modifier,
    color: Color,
    cornerRadius: Dp = 0.dp,
    elevation : Dp = 0.dp,
    onClick: () -> Unit,
    isEnabled :Boolean,
    @DrawableRes imageId: Int? = null,
    @StringRes textId: Int,
    textStyle: TextStyle
) {
    Button(
        onClick = onClick,
        modifier = modifier,
        colors = ButtonDefaults.buttonColors(color),
        shape = RoundedCornerShape(cornerRadius),
        elevation = ButtonDefaults.buttonElevation(elevation),
        enabled = isEnabled
    ) {
        if (imageId != null) {
            Image(
                painter = painterResource(id = imageId),
                contentDescription = stringResource(id = R.string.google),
                contentScale = ContentScale.Fit,
                modifier = Modifier
                    .size(16.dp)
                    .clip(CircleShape)
            )
            Spacer(modifier = Modifier.width(8.dp))
        }
        Text(
            text = stringResource(id = textId),
            style = textStyle
        )
    }
}