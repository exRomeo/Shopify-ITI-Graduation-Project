package com.example.shopify.presentation.screens.product_details_screen

import android.util.Log
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.shopify.R
import com.example.shopify.Utilities.ShopifyApplication
import com.example.shopify.core.helpers.UiState
import com.example.shopify.data.models.SingleProduct
import com.example.shopify.data.models.SingleProductResponseBody
import com.example.shopify.data.repositories.product.IProductRepository
import com.example.shopify.presentation.common.composables.WarningDialog

@Composable
fun ProductDetailsScreen(/*productId : Long*/) {
    var isFavorite by remember {
        mutableStateOf(false)
    }
    var itemCount by remember { mutableIntStateOf(0) }
    val repository: IProductRepository =
        (LocalContext.current.applicationContext as ShopifyApplication).repository
    val productDetailsViewModel: ProductDetailsViewModel =
        viewModel(factory = ProductDetailsViewModelFactory(repository))
    var product: SingleProduct? by remember { mutableStateOf(null) }
    val productState by productDetailsViewModel.productInfoState.collectAsState()
    var showDialog by remember { mutableStateOf(false) }
    var showFavWarningDialog by remember { mutableStateOf(false) }
    var showReviewsDialog by remember { mutableStateOf(true) }
    LaunchedEffect(Unit) {
        productDetailsViewModel.getProductInfo(8398820573490/*productId*/)
    }
    when (val state = productState) {
        is UiState.Success<*> -> {
            LaunchedEffect(key1 = state) {
                product =
                    (productState as UiState.Success<SingleProductResponseBody>).data.body()?.product
                if (product != null)
                    Log.i("TAG", "ProductDetailsScreen: $product")
                else
                    Log.i("TAG", "ProductDetailsScreen: NOT FOUND")
            }
        }

        is UiState.Error -> {
            val error = (productState as UiState.Error).error.message
            Log.i("TAG", "ProductDetailsScreen: $error")
        }

        else -> {}
    }


    product?.let {
        ProductDetailsContentScreen(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            isFavorite = isFavorite,
            onFavoriteChanged = {
                showFavWarningDialog = true
            },
            onAcceptFavChanged = { isFavorite = !isFavorite },
            itemCount = itemCount,
            increase = { if (itemCount < 10) itemCount++ },
            decrease = {
                if (itemCount > 1) itemCount--
                else if (itemCount == 1) showDialog = true
                else if(itemCount == 0)showDialog = false
            },
            showDialog = showDialog,
            showFavWarningDialog = showFavWarningDialog,
            showReviewsDialog = showReviewsDialog,
            onShowDialogAction = {
                showDialog = false
                itemCount = 0
            },
            onShowFavDialogAction = {
                showFavWarningDialog = false
            },
            showReviews = { showReviewsDialog = true },
            it
        )
    }

}



