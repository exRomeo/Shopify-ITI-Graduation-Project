package com.example.shopify

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.shopify.core.navigation.NavGraph
import com.example.shopify.core.navigation.Screens
import com.example.shopify.core.utils.SharedPreference.hasCompletedOnBoarding
import com.example.shopify.presentation.screens.onBoarding.OnBoardingScreen
import com.example.shopify.ui.theme.ShopifyTheme
import com.example.shopify.utilities.ShopifyApplication

class MainActivity : ComponentActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

val sharedPreference = (applicationContext as ShopifyApplication).sharedPreference
        setContent {
            val navController = rememberNavController()
            val backStackEntry by navController.currentBackStackEntryAsState()
           // sharedPreference.hasCompletedOnBoarding =false
//            if (!sharedPreference.hasCompletedOnBoarding) {
//                OnBoardingScreen(onComplete = {
//                    sharedPreference.hasCompletedOnBoarding = true
//                }, navController = navController )
//            } else {


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
