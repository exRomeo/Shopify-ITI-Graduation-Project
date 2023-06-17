package com.example.shopify

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.shopify.core.navigation.NavGraph
import com.example.shopify.core.navigation.Screens
import com.example.shopify.ui.theme.ShopifyTheme

class MainActivity : ComponentActivity() {



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        setContent {
            val navController = rememberNavController()
            val backStackEntry by navController.currentBackStackEntryAsState()
            BackHandler {
                when (backStackEntry?.destination?.route) {
                    Screens.Login.route, Screens.Signup.route -> {
                        finish()
                    }

                    Screens.Home.route -> {
                        moveTaskToBack(true)
                    }

                    else -> {
                        navController.navigateUp()
                    }
                }
            }
            ShopifyTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background,
                ) {
                    NavGraph(navController)
                }
            }
        }
    }
}
