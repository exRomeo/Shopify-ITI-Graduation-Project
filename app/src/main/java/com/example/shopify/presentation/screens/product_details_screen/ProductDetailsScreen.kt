package com.example.shopify.presentation.screens.product_details_screen

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.shopify.Utilities.ShopifyApplication
import com.example.shopify.core.helpers.UiState
import com.example.shopify.data.models.Image
import com.example.shopify.data.models.SingleProduct
import com.example.shopify.data.models.SingleProductResponseBody
import com.example.shopify.data.repositories.product.IProductRepository

@Composable
fun ProductDetailsScreen() {
    val repository: IProductRepository =
        (LocalContext.current.applicationContext as ShopifyApplication).repository
    val productDetailsViewModel: ProductDetailsViewModel =
        viewModel(factory = ProductDetailsViewModelFactory(repository))

    var product: SingleProduct? = null
    val productState: UiState by productDetailsViewModel.productInfoState.collectAsState()

    var imageList : List<Image> = listOf()
    when (val state = productState) {
        is UiState.Success<*> -> {
            LaunchedEffect(key1 = state) {
                product =
                    (productState as UiState.Success<SingleProductResponseBody>).data.body()?.product
                imageList = product?.images!!
                
                if (product !=null)
                    Log.i("TAG", "ProductDetailsScreen: $product")
                else
                    Log.i("TAG", "ProductDetailsScreen: NOT FOUND")
            }
        }

        is UiState.Error -> {
            LaunchedEffect(key1 = state) {
                val error = (productState as UiState.Error).error.message
                Log.i("TAG", "ProductDetailsScreen: $error")
            }
        }

        else -> {}
    }
    Surface(
        Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.onPrimary)
    ) {

    }
   // ProductDetailsContentScreen(productDetailsViewModel,imageList)
}

