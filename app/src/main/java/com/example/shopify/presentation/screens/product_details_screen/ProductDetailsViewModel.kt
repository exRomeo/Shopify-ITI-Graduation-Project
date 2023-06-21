package com.example.shopify.presentation.screens.product_details_screen

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shopify.core.helpers.UiState
import com.example.shopify.data.managers.cart.CartManager
import com.example.shopify.data.managers.wishlist.WishlistManager
import com.example.shopify.data.models.ProductSample
import com.example.shopify.data.repositories.product.IProductRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ProductDetailsViewModel(
    private val productRepository: IProductRepository,
    private val wishlistManager: WishlistManager,
    private val cartManager: CartManager
) : ViewModel() {

    private var _productInfoState = MutableStateFlow<UiState>(UiState.Loading)
    val productInfoState = _productInfoState
    private var _favProduct: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val favProduct: StateFlow<Boolean> = _favProduct
    fun getProductInfo(productId: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            val response = productRepository.getSingleProductDetails(productId)
            checkResponseState(response)
        }
    }

    fun addWishlistItem(product: ProductSample) {
        viewModelScope.launch(Dispatchers.IO) {
            wishlistManager.addWishlistItem(product.id, product.variants.get(0).id)
        }
    }

    fun removeWishlistItem(product: ProductSample) {
        viewModelScope.launch(Dispatchers.IO) {
            wishlistManager.removeWishlistItem(product.id)
        }
    }
    fun isFavorite(productId: Long/*, variantId: Long*/) {
        viewModelScope.launch(Dispatchers.IO) {
            _favProduct.value = wishlistManager.isFavorite(productId/*, variantId*/)
            Log.i("TAG", "isFavorite: ViewModel id ${_favProduct.value}")
        }
    }
    fun addItemToCart(product: ProductSample) {
        viewModelScope.launch(Dispatchers.IO) {
            cartManager.addCartItem(product.id, product.variants.get(0).id)
        }

    }

    fun removeItemFromCart(product: ProductSample) {
        viewModelScope.launch(Dispatchers.IO) {
            cartManager.removeCart(product.id)
        }
    }

    private fun checkResponseState(responseState: UiState) {
        when (responseState) {
            is UiState.Success<*> -> {
                _productInfoState.value = UiState.Success(responseState.data)
                Log.i("TAG", "Success checkProductDetails: ${responseState.data.body()}")
            }

            is UiState.Error -> {
                _productInfoState.value = UiState.Error(responseState.error)
                Log.i("TAG", "ERROR checkProductDetails: ${responseState.error.message}")
            }

            is UiState.Loading -> {
                Log.i("TAG", "Loading checkProductDetails: LOADING")

            }
        }
    }
}