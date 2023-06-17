package com.example.shopify.presentation.screens.product_details_screen

import android.widget.ViewSwitcher.ViewFactory
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.shopify.data.repositories.product.IProductRepository
import com.example.shopify.presentation.screens.authentication.registeration.SignupViewModel

class ProductDetailsViewModelFactory(private val productRepository: IProductRepository):ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if(modelClass.isAssignableFrom(ProductDetailsViewModel::class.java)){
            ProductDetailsViewModel(productRepository) as T
        }else{
            throw IllegalArgumentException("ViewModel class is not found!!")
        }
    }
}
