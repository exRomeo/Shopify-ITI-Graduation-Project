package com.example.shopify.presentation.screens.product_details_screen

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.shopify.data.models.Image
import com.google.accompanist.pager.ExperimentalPagerApi

@OptIn(ExperimentalPagerApi::class)
@Composable
fun ProductDetailsContentScreen(productDetailsViewModel:ProductDetailsViewModel,imageList:List<Image>){
    productDetailsViewModel.getProductInfo(8398828339506)
    Surface(Modifier.fillMaxSize()) {
        Text(
            text = "Hello ProductScreen",
            modifier = Modifier.fillMaxSize()
        )
    }
   // SliderLayout(imageList)
}