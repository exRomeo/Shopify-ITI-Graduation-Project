package com.example.shopify.presentation.common.composables

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.tv.material3.Text
import com.example.shopify.R

@Composable
@Preview(showSystemUi = true)
fun NoData(message: String = "No Data To Show") {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        SmallLottieAnimation(modifier = Modifier.fillMaxSize(0.7f), animation = R.raw.empty)
        Text(text = message)
    }
}