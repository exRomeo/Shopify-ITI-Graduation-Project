package com.example.shopify

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.shopify.presentation.screens.homescreen.HomeScreen
import com.example.shopify.presentation.screens.homescreen.HomeViewModel
import com.example.shopify.presentation.screens.homescreen.ScaffoldStructure
import com.example.shopify.ui.theme.ShopifyTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ShopifyTheme {
                // A surface container using the 'background' color from the theme
              //  Surface {

                 val viewModel: HomeViewModel = HomeViewModel()

                    ScaffoldStructure ("Home"){HomeScreen(viewModel = viewModel) }

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