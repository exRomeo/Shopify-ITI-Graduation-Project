package com.example.shopify.presentation.screens.product_details_screen

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shopify.core.helpers.UiState
import com.example.shopify.data.repositories.product.IProductRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ProductDetailsViewModel(private val productRepository: IProductRepository) : ViewModel() {

    private var _productInfoState = MutableStateFlow<UiState>(UiState.Loading)
    val productInfoState: StateFlow<UiState> = _productInfoState

    fun getProductInfo(productId: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            val response = productRepository.getSingleProductDetails(productId)
            checkResponseState(response)
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