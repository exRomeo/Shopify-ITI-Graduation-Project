package com.example.shopify

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.example.shopify.core.navigation.NavGraph
import com.example.shopify.presentation.screens.product_details_screen.ProductDetailsScreen
import com.example.shopify.ui.theme.ShopifyTheme
import com.google.accompanist.pager.ExperimentalPagerApi


class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //check network      // Log.i("TAG", "onCreate: ${(applicationContext as ShopifyApplication).currentCustomer})")

        setContent {
            ShopifyTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background,
                ) {
//                    NavGraph()
//                    ProductDetailsScreen()
//                    SliderLayout()
                    ProductDetailsScreen()
//                    ImageCard("Title", "Description")
                }
            }
        }
    }
}
