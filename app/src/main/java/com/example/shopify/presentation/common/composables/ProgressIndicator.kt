package com.example.shopify.presentation.common.composables

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.annotation.RawRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import com.example.shopify.R

@Preview(showSystemUi = true, uiMode = UI_MODE_NIGHT_YES)
@Composable
fun ProgressIndicatorPreview() {
    Scaffold() {
        Column(
            modifier = Modifier
                .padding(it)
        ) {
            LottieAnimation(animation = R.raw.loading_animation_circular)
        }
    }
}

@Composable
fun LottieAnimation(
    modifier: Modifier = Modifier.size(150.dp),
    @RawRes animation: Int
) {
    Column(
        modifier = Modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(modifier = modifier) {
            val composition by rememberLottieComposition(
                spec = LottieCompositionSpec.RawRes(
                    animation
                )
            )
            LottieAnimation(
                composition = composition,
                iterations = LottieConstants.IterateForever,
            )
        }
    }
}

@Composable
fun ChangingColorProgressBar(
) {

    CircularProgressIndicator(
        modifier = Modifier.size(150.dp),
        strokeWidth = 5.dp,
        color = Color.Green
    )
}