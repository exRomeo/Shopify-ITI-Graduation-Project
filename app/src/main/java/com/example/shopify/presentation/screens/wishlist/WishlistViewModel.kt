package com.example.shopify.presentation.screens.wishlist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.shopify.core.helpers.UserScreenUISState
import com.example.shopify.data.models.ProductSample
import com.example.shopify.data.repositories.wishlist.IWishlistRepository
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class WishlistViewModel(private val wishlistRepository: IWishlistRepository) : ViewModel() {

    private var _snackbarMessage: MutableSharedFlow<Int> = MutableSharedFlow()
    val snackbarMessage = _snackbarMessage.asSharedFlow()

    private var _screenState: MutableStateFlow<UserScreenUISState> =
        MutableStateFlow(UserScreenUISState.Loading)
    val screenState = _screenState.asStateFlow()

    private var _wishlist: MutableStateFlow<List<ProductSample>> = MutableStateFlow(listOf())
    val wishlist = _wishlist.asStateFlow()

    init {
        getWishlistItems()
    }

    private fun getWishlistItems() {
        viewModelScope.launch {
            wishlistRepository.getWishlistItems()
            wishlistRepository.wishlist.collect {
                _wishlist.value = it
                _screenState.value = UserScreenUISState.Success(it)
            }
        }
    }

    fun addWishlistItem(productID: Long, variantID: Long) {
        viewModelScope.launch {
            wishlistRepository.addWishlistItem(productID = productID, variantID = variantID)
        }
    }

    fun removeWishlistItem(productID: Long) {
        viewModelScope.launch {
            wishlistRepository.removeWishlistItem(productID = productID)
        }
    }
}

class WishlistViewModelFactory(private val wishlistRepository: IWishlistRepository) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(WishlistViewModel::class.java))
            WishlistViewModel(wishlistRepository) as T
        else throw Exception("View model not found!")
    }
}