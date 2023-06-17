package com.example.shopify.presentation.screens.product_details_screen

import android.util.Log
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.shopify.R
import com.example.shopify.Utilities.ShopifyApplication
import com.example.shopify.core.helpers.UiState
import com.example.shopify.data.managers.CartManager
import com.example.shopify.data.managers.WishlistManager
import com.example.shopify.data.models.Image
import com.example.shopify.data.models.ProductSample
import com.example.shopify.data.models.SingleProduct
import com.example.shopify.data.models.SingleProductResponseBody
import com.example.shopify.data.repositories.product.IProductRepository

@Composable
fun ProductDetailsScreen(navController: NavHostController, productId: Long) {
    var isFavorite by remember {
        mutableStateOf(false)
    }
    var itemCount by remember { mutableStateOf(0) }

    //product
    val productRepository: IProductRepository =
        (LocalContext.current.applicationContext as ShopifyApplication).repository
    val wishlistManager: WishlistManager =
        (LocalContext.current.applicationContext as ShopifyApplication).wishlistManager
    val cartManager: CartManager =
        (LocalContext.current.applicationContext as ShopifyApplication).cartManager
    val productDetailsViewModel: ProductDetailsViewModel =
        viewModel(
            factory = ProductDetailsViewModelFactory(
                productRepository, wishlistManager,
                cartManager
            )
        )
    var product: SingleProduct? by remember { mutableStateOf(null) }
    val productState by productDetailsViewModel.productInfoState.collectAsState()
    val productSample = ProductSample(
        id = product?.id ?: 0,
        title = product?.title ?: "",
        variants = product?.variants ?: listOf(),
        images = product?.images ?: listOf(),
        image = Image(product?.images?.get(0)?.src ?: "")
    )

    var productAddedToCart by remember { mutableStateOf(false) }
    var dialogIsAppeared by remember { mutableStateOf(false) }
    var showDialog by remember { mutableStateOf(false) }
    var showToast by remember { mutableStateOf(false) }
    var showFavWarningDialog by remember { mutableStateOf(false) }
    var showReviewsDialog by remember { mutableStateOf(false) }
    var toastMessage by remember { mutableStateOf(0) }

    LaunchedEffect(Unit) {
        productDetailsViewModel.getProductInfo(productId/*8398820573490*/)
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
            productNavController = navController,
            onFavoriteChanged = {
                showFavWarningDialog = true
                showToast = false
            },
            onAcceptFavChanged = {
                isFavorite = !isFavorite
                if (isFavorite) {
                    productDetailsViewModel.addWishlistItem(productSample)
                    toastMessage = R.string.product_add_to_fav_success
                }
                if (!isFavorite) {
                    productDetailsViewModel.removeWishlistItem(productSample)
                    toastMessage = R.string.product_remove_from_fav_success
                }
                showToast = true
            },
            itemCount = itemCount,
            increase = {
                showToast = false
                if (itemCount < 10) itemCount++
            },
            decrease = {
                if (itemCount > 1) {
                    itemCount--
                    showToast = false
                } else if (itemCount == 1 && !dialogIsAppeared) showDialog = true
            },
            showDialog = showDialog,
            showFavWarningDialog = showFavWarningDialog,
            showReviewsDialog = showReviewsDialog,
            showToast = if (!dialogIsAppeared) showToast else false,
            toastMessage = toastMessage,
            onShowDialogAction = {
                showDialog = false
                dialogIsAppeared = true
            },
            onDismissDialogAction = {
                showFavWarningDialog = false
            },
            onAcceptRemoveCart = {
                if (itemCount == 1) {
                    itemCount = 0
                    if (productAddedToCart) {
//                        productDetailsViewModel.removeItemFromCart(productSample)
                        toastMessage = R.string.product_remove_from_cart_success
                        showToast = true
                        productAddedToCart = false
                    }
                }
                showDialog = false
            },
            onDismissRemoveCart = {
                dialogIsAppeared = false
                showDialog = false
            },
            addToCartAction = {
                toastMessage = if (itemCount != 0) {
                    productDetailsViewModel.addItemToCart(
                        product = ProductSample(
                            id = product?.id ?: 0,
                            title = product?.title ?: "",
                            variants = product?.variants ?: listOf(),
                            images = product?.images ?: listOf(),
                            image = Image(product?.images?.get(0)?.src ?: "")
                        )
                    )
                    productAddedToCart = true
                    R.string.product_add_to_cart_success
                } else {
                    productAddedToCart = false
                    R.string.please_enter_quantity_of_product
                }
                showToast = true

            },
            showReviews = { showReviewsDialog = true },
            onDismissShowReview = { showReviewsDialog = false },
            it
        )
    }
}




