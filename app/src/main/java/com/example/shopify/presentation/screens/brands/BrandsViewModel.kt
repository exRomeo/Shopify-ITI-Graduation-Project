package com.example.shopify.presentation.screens.brands

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.shopify.core.helpers.UiState
import com.example.shopify.data.managers.cart.CartManager
import com.example.shopify.data.managers.wishlist.WishlistManager
import com.example.shopify.data.repositories.product.IProductRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class BrandsViewModel(
    private val repository: IProductRepository,
    private val wishlistManager: WishlistManager,
    private val cartManager: CartManager
    ):ViewModel() {
    private var _products: MutableStateFlow<UiState> = MutableStateFlow(UiState.Loading)
    val brandList: StateFlow<UiState> = _products
    var id:Long = 0


     fun getSpecificBrandProducts() {
        viewModelScope.launch(Dispatchers.IO) {
            val response = repository.getSpecificBrandProducts(id)
            withContext(Dispatchers.Main) {
                response
                    .catch {
                        _products.value = UiState.Error(it)
                    }
                    .collect {
                        Log.i("menna", "getproducts")
                        _products.value = UiState.Success(it)

                    }

            }
        }
    }

    fun addWishlistItem(productId: Long,variantId: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            wishlistManager.addWishlistItem(productId,variantId)
        }
    }
    fun removeWishlistItem(productId: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            wishlistManager.removeWishlistItem(productId)
        }
    }

    fun addItemToCart(productId: Long,variantId: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            cartManager.addCartItem(productId,variantId)
        }

    }

}


    class BrandsViewModelFactory( private val repository: IProductRepository,
                                  private val wishlistManager: WishlistManager,
                                  private val cartManager: CartManager) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return if (modelClass.isAssignableFrom(BrandsViewModel::class.java))
                BrandsViewModel(repository, wishlistManager,cartManager) as T else throw IllegalArgumentException("View Model Class Not Found !!!")
        }
    }
