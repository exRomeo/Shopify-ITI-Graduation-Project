package com.example.shopify

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview

import com.example.shopify.data.models.Product

import com.example.shopify.presentation.screens.homescreen.BrandCards
import com.example.shopify.presentation.screens.homescreen.HomeSection
import com.example.shopify.presentation.screens.homescreen.ItemCards
import com.example.shopify.presentation.screens.homescreen.adsCarousel
import com.example.shopify.ui.theme.ShopifyTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ShopifyTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                   // modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    var isf by remember {

                        mutableStateOf(false)
                    }
                   // Greeting("Android")
                    //BrandCardDesign(modifier = Modifier,{})
                  //  BrandCardDesign(Modifier,{}, { BrandCardContent() })
                   // BrandCardDesign(Modifier,{}, { ItemCardContent(isFavourite = isf, onClicked ={isf = !isf} ) })
                    //adsCarousel()
//                    HomeSection(sectionTitle = R.string.brands, sectionContent = {
//                        ItemCards(brands = listOf(
//                            Product(1, "menna", 100.0),
//                            Product(1,"menna",100.0)
//                        ,Product(1,"menna",100.0)
//                        ,Product(1,"menna",100.0)
//                        ,Product(1,"menna",100.0)
//                        ,Product(1,"menna",100.0)
//                        ,Product(1,"menna",100.0)
//                        ,Product(1,"menna",100.0)))
//                    })
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    ShopifyTheme {
        Greeting("Android")
    }
}