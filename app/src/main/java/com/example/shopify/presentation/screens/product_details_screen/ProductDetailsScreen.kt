package com.example.shopify.presentation.screens.product_details_screen

import android.util.Log
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.shopify.R
import com.example.shopify.utilities.ShopifyApplication
import com.example.shopify.core.helpers.UiState
import com.example.shopify.core.utils.ConnectionUtil
import com.example.shopify.data.managers.cart.CartManager
import com.example.shopify.data.managers.wishlist.WishlistManager
import com.example.shopify.data.models.Image
import com.example.shopify.data.models.ProductSample
import com.example.shopify.data.models.SingleProduct
import com.example.shopify.data.models.SingleProductResponseBody
import com.example.shopify.data.repositories.product.IProductRepository
import com.example.shopify.presentation.common.composables.LottieAnimation
import com.example.shopify.presentation.common.composables.ShowCustomDialog
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collect
import java.io.IOException
import kotlin.random.Random

@Composable
fun ProductDetailsScreen(navController: NavHostController, productId: Long) {

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
    var itemCount by remember { mutableStateOf(0) }
    var productAddedToCart by remember { mutableStateOf(false) }
    var showNetworkDialog by remember { mutableStateOf(false) }
    var showCartDialog by remember { mutableStateOf(false) }
    var showFavDialog by remember { mutableStateOf(false) }
    var showToast by remember { mutableStateOf(false) }
    var showReviewsDialog by remember { mutableStateOf(false) }
    var isFavorite by remember { mutableStateOf(false) }
    var toastMessage by remember { mutableStateOf(0) }
    var dialogMessage by remember { mutableStateOf(0) }

    var rating by remember {
        mutableStateOf(3.5)
    }
    LaunchedEffect(Unit) { rating = Random.nextDouble(3.0, 5.0) }

    LaunchedEffect(Unit) {
        productDetailsViewModel.getProductInfo(productId/*8398820573490*/)
        productDetailsViewModel.isFavorite(productId)
        productDetailsViewModel.favProduct.collect() { state ->
            if (state) {
                isFavorite = true
            }
        }
    }

    when (val state = productState) {
        is UiState.Success<*> -> {
            LaunchedEffect(key1 = state /*, favoriteState*/) {
                product =
                    (productState as UiState.Success<SingleProductResponseBody>).data.product
            }
            if (product != null) {
                ProductDetailsContentScreen(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    productNavController = navController,
                    isFavorite = isFavorite,
                    showFavWarningDialog = showFavDialog,
                    showReviewsDialog = showReviewsDialog,
                    showToast = showToast,
                    showCartDialog = showCartDialog,
                    rating = rating,
                    dialogMessage = dialogMessage,
                    toastMessage = toastMessage,
                    itemCount = itemCount,
                    product = product,
                    onFavoriteChanged = {
                        showFavDialog = true
                        showToast = false
                        dialogMessage =
                            if (isFavorite) R.string.fav_item_removal_warning else
                                R.string.add_item_to_fav_message
                    },
                    onAcceptFavChanged = {
                        showFavDialog = false
                        showToast = true
//                        productDetailsViewModel.isFavorite(productId)
//                        isFavorite = favoriteState
                        isFavorite = !isFavorite
                        toastMessage = if (isFavorite) {
                            productDetailsViewModel.addWishlistItem(productSample)
                            R.string.product_add_to_fav_success
                        } else {
                            productDetailsViewModel.removeWishlistItem(productSample)
                            R.string.product_remove_from_fav_success

                        }

                    },
                    onDismissFavChanged = {
                        showFavDialog = false
                    },

                    increaseItemCount = {
                        showToast = false
                        if (itemCount < 10) itemCount++
                    },
                    decreaseItemCount = {
                        showToast = false
                        if (itemCount > 1) itemCount--
                        else if (itemCount == 1 && productAddedToCart) {
                            showCartDialog = true
                            dialogMessage = R.string.cart_item_removal_warning
                        } else if (itemCount == 1 && !productAddedToCart) {
                            itemCount = 0
                        }
                    },

                    onAcceptRemoveCart = {
                        if (productAddedToCart) {
                            productDetailsViewModel.removeItemFromCart(productSample)
                            productAddedToCart = false
                            showToast = true
                            toastMessage = R.string.product_remove_from_cart_success
                        }
                        itemCount = 0
                        showCartDialog = false
                    },
                    onDismissRemoveCart = {
                        showCartDialog = false
                    },

                    addToCartAction = {
                        showToast = false
                        if (itemCount > 0) {
                            productDetailsViewModel.addItemToCart(productSample)
                            productAddedToCart = true
                            showToast = true
                            toastMessage = R.string.product_add_to_cart_success
                        } else {
                            showToast = true
                            toastMessage = R.string.please_enter_quantity_of_product
                        }
                    },

                    showReviews = { showReviewsDialog = true },
                    onDismissShowReview = { showReviewsDialog = false },
//        onTryAgainConnection = {
//            showNetworkDialog = false }
                )
            } else {
                showNetworkDialog = true
                if (showNetworkDialog)
                    Surface(color = Color.Gray) {
                        ShowCustomDialog(
                            title = R.string.something_is_wrong,
                            description = R.string.unexpected_error,
                            buttonText = R.string.tryAgain,
                            animatedId = R.raw.error_animation,
                            onDismiss = { showNetworkDialog = false },
                            onClose = {
                                showNetworkDialog = false
                                navController.popBackStack()
                            }
                        )

                    }

            }

        }

        is UiState.Error -> {
            when (state.error) {
                is IOException -> {
                    showNetworkDialog = true
                    val error = (productState as UiState.Error).error
                    Log.i("TAG", "ProductDetailsScreen: $error")
                    if (showNetworkDialog)
                        Surface(color = Color.Gray) {
                            ShowCustomDialog(
                                title = R.string.network_connection,
                                description = R.string.not_connection,
                                buttonText = R.string.tryAgain,
                                animatedId = R.raw.custom_network_error,
                                onDismiss = { showNetworkDialog = false },
                                onClose = {
                                    showNetworkDialog = false
                                    navController.popBackStack()
                                }
                            )

                        }
                }

                else -> {
                    showNetworkDialog = true
                    val error = (productState as UiState.Error).error
                    Log.i("TAG", "ProductDetailsScreen: $error")
                    if (showNetworkDialog)
                        Surface(color = Color.Gray) {
                            ShowCustomDialog(
                                title = R.string.something_is_wrong,
                                description = R.string.unexpected_error,
                                buttonText = R.string.tryAgain,
                                animatedId = R.raw.error_animation,
                                onDismiss = { showNetworkDialog = false },
                                onClose = {
                                    showNetworkDialog = false
                                    navController.popBackStack()
                                }
                            )

                        }
                }
            }

        }

        else -> {
            LottieAnimation(animation = R.raw.shopping_cart_loading_animation)
        }
    }
}


