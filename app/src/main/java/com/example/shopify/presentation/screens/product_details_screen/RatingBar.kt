package com.example.shopify.presentation.screens.product_details_screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.shopify.R
import com.example.shopify.ui.theme.ratingColor
import kotlin.math.ceil
import kotlin.math.floor

@Composable
fun RatingBar(
    modifier: Modifier = Modifier,
    rating: Double = 0.0,
    stars: Int = 5,
    starsColor: Color = ratingColor,
) {
    val filledStars = floor(rating).toInt()
    val unfilledStars = (stars - ceil(rating)).toInt()
    val halfStar = !(rating.rem(1).equals(0.0))

    Row() {
        repeat(filledStars) {
            Icon(
                painter = painterResource(id = R.drawable.filled_star),
                contentDescription = stringResource(id = R.string.half_star),
                tint = starsColor
            )
            Spacer(modifier = modifier.width(2.dp))
        }
        if (halfStar) {
            Icon(
                painter = painterResource(id = R.drawable.half_star),
                contentDescription = stringResource(id = R.string.half_star),
                tint = starsColor
            )
            Spacer(modifier = modifier.width(2.dp))

        }
        repeat(unfilledStars) {
            Icon(
                painter = painterResource(id = R.drawable.outlined_star),
                contentDescription = stringResource(id = R.string.outlined_star),
                tint = starsColor
            )
            Spacer(modifier = modifier.width(2.dp))

        }
    }
}