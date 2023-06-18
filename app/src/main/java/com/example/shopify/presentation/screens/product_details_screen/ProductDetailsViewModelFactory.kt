package com.example.shopify.presentation.screens.product_details_screen

import android.widget.ViewSwitcher.ViewFactory
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.shopify.data.managers.CartManager
import com.example.shopify.data.managers.WishlistManager
import com.example.shopify.data.repositories.product.IProductRepository
import com.example.shopify.presentation.screens.authentication.registeration.SignupViewModel

class ProductDetailsViewModelFactory(
    private val productRepository: IProductRepository,
    private val wishlistManager: WishlistManager,
    private val cartManager: CartManager
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(ProductDetailsViewModel::class.java)) {
            ProductDetailsViewModel(productRepository,wishlistManager,cartManager) as T
        } else {
            throw IllegalArgumentException("ViewModel class is not found!!")
        }
    }
}
