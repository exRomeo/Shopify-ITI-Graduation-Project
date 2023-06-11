package com.example.shopify

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.ViewModelProvider
import com.example.shopify.Utilities.ShopifyApplication
import com.example.shopify.data.repositories.product.IProductRepository
import com.example.shopify.presentation.screens.homescreen.HomeScreen
import com.example.shopify.presentation.screens.homescreen.HomeViewModel
import com.example.shopify.presentation.screens.homescreen.HomeViewModelFactory
import com.example.shopify.presentation.screens.homescreen.ScaffoldStructure
import com.example.shopify.ui.theme.ShopifyTheme

class MainActivity : ComponentActivity() {
    private val repository: IProductRepository by lazy {
        (applicationContext as ShopifyApplication).repository
    }

    private val viewModel: HomeViewModel by lazy {
        ViewModelProvider(this, HomeViewModelFactory(repository))[HomeViewModel::class.java]
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ShopifyTheme {
                // A surface container using the 'background' color from the theme
              //  Surface {


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