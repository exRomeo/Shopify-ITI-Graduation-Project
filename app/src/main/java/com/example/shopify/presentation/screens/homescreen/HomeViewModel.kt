package com.example.shopify.presentation.screens.homescreen

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
import kotlinx.coroutines.launch

class HomeViewModel(
    private val repository: IProductRepository,
    private val wishlistManager: WishlistManager,
    private val cartManager: CartManager,
) : ViewModel() {

    private var _brandsList: MutableStateFlow<UiState> = MutableStateFlow(UiState.Loading)
    val brandList: StateFlow<UiState> = _brandsList

    private var _randomList: MutableStateFlow<UiState> = MutableStateFlow(UiState.Loading)
    val randomList: StateFlow<UiState> = _randomList

    init {
        getBrands()
        getRandomProducts()

    }

     fun getBrands() {
        viewModelScope.launch(Dispatchers.IO) {
        val response = repository.getBrands()
            if(response.isSuccessful && response.body()!= null){
                _brandsList.value = UiState.Success(response.body())
            }
         //   _brandsList.value = UiState.Error
        }
    }

     fun getRandomProducts() {
         viewModelScope.launch(Dispatchers.IO) {
             val response = repository.getRandomProducts()
             if (response.isSuccessful && response.body() != null) {
                 _randomList.value = UiState.Success(response.body())
             }
             else{
                 _randomList.value = UiState.Error(response.errorBody())
             }
         }
     }


    fun addWishlistItem(productId: Long, variantId: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            wishlistManager.addWishlistItem(productId, variantId)
        }
    }

    suspend fun isFavorite(productId: Long/*, variantId: Long*/) :Boolean{
        return wishlistManager.isFavorite(productId)
    }

    fun removeWishlistItem(productId: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            wishlistManager.removeWishlistItem(productId)
        }
    }

    fun addItemToCart(productId: Long, variantId: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            cartManager.addCartItem(productId, variantId)
        }

    }

}

class HomeViewModelFactory(
    private val repository: IProductRepository,
    private val wishlistManager: WishlistManager,
    private val cartManager: CartManager,
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(HomeViewModel::class.java))
            HomeViewModel(
                repository,
                wishlistManager,
                cartManager,
            ) as T else throw IllegalArgumentException("View Model Class Not Found !!!")
    }
}