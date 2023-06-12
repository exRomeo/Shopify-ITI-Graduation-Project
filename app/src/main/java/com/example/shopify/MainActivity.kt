package com.example.shopify

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
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
import com.example.shopify.core.helpers.RetrofitHelper
import com.example.shopify.core.navigation.NavGraph
import com.example.shopify.core.utils.SharedPreference
import com.example.shopify.data.remote.authentication.AuthenticationClient
import com.example.shopify.data.repositories.authentication.AuthRepository
import com.example.shopify.presentation.screens.authentication.login.LoginViewModel
import com.example.shopify.presentation.screens.authentication.login.LoginViewModelFactory
import com.example.shopify.presentation.screens.authentication.registeration.SignupViewModel
import com.example.shopify.presentation.screens.authentication.registeration.SignupViewModelFactory
import com.example.shopify.presentation.screens.homescreen.HomeScreen
import com.example.shopify.presentation.screens.homescreen.ScaffoldStructure
import com.example.shopify.ui.theme.ShopifyTheme
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

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
    lateinit var loginViewModelFactory: LoginViewModelFactory
    private lateinit var loginViewModel: LoginViewModel
    lateinit var signupViewModelFactory: SignupViewModelFactory
    private lateinit var signupViewModel: SignupViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        FirebaseApp.initializeApp(this)
        loginViewModelFactory =
            LoginViewModelFactory(
                AuthRepository(
                    AuthenticationClient(
                        RetrofitHelper.getAuthenticationService(BASE_URL),
                        FirebaseAuth.getInstance(),
                        FirebaseFirestore.getInstance()
                    ),
                    SharedPreference.customPreference(this, CUSTOMER_PREF_NAME)
                )
            )

        loginViewModel =
            ViewModelProvider(this, loginViewModelFactory).get(LoginViewModel::class.java)

        signupViewModelFactory =
            SignupViewModelFactory(
                AuthRepository(
                    AuthenticationClient(
                        RetrofitHelper.getAuthenticationService(BASE_URL),
                        FirebaseAuth.getInstance(),
                        FirebaseFirestore.getInstance()
                    ),
                    SharedPreference.customPreference(this, CUSTOMER_PREF_NAME)
                )
            )
        signupViewModel =
            ViewModelProvider(this, signupViewModelFactory).get(SignupViewModel::class.java)
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