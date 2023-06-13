package com.example.shopify

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.Favorite
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.shopify.Utilities.ShopifyApplication
import com.example.shopify.core.helpers.RetrofitHelper
import com.example.shopify.core.navigation.NavGraph
import com.example.shopify.data.repositories.product.IProductRepository
import com.example.shopify.presentation.screens.homescreen.HomeViewModel
import com.example.shopify.presentation.screens.homescreen.HomeViewModelFactory
import com.example.shopify.ui.theme.ShopifyTheme

private const val BASE_URL = "https://mad43-alex-and-team2.myshopify.com/"
private const val CUSTOMER_PREF_NAME = "customer"

class MainActivity : ComponentActivity() {
//    private val repository: IProductRepository by lazy {
//        (applicationContext as ShopifyApplication).repository
//    }
//
//    private val viewModel: HomeViewModel by lazy {
//        ViewModelProvider(this, HomeViewModelFactory(repository))[HomeViewModel::class.java]
//    }
//    lateinit var loginViewModelFactory: LoginViewModelFactory
//    private lateinit var loginViewModel: LoginViewModel
//    lateinit var signupViewModelFactory: SignupViewModelFactory
//    private lateinit var signupViewModel: SignupViewModel
    lateinit var navController: NavHostController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
 //check network      // Log.i("TAG", "onCreate: ${(applicationContext as ShopifyApplication).currentCustomer})")
//        FirebaseApp.initializeApp(this)
//        loginViewModelFactory =
//            LoginViewModelFactory(
//                AuthRepository(
//                    AuthenticationClient(
//                        RetrofitHelper.getAuthenticationService(BASE_URL),
//                        FirebaseAuth.getInstance(),
//                        FirebaseFirestore.getInstance()
//                    ),
//                    SharedPreference.customPreference(this, CUSTOMER_PREF_NAME)
//                )
//            )
//
//        loginViewModel = ViewModelProvider(this, loginViewModelFactory).get(LoginViewModel::class.java)

//        signupViewModelFactory =
//            SignupViewModelFactory(
//                AuthRepository(
//                    AuthenticationClient(
//                        RetrofitHelper.getAuthenticationService(BASE_URL),
//                        FirebaseAuth.getInstance(),
//                        FirebaseFirestore.getInstance()
//                    ),
//                    SharedPreference.customPreference(this, CUSTOMER_PREF_NAME)
//                )
//            )
//        signupViewModel =
//            ViewModelProvider(this, signupViewModelFactory).get(SignupViewModel::class.java)

        setContent {
            ShopifyTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background,
                ) {
                    NavGraph()
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