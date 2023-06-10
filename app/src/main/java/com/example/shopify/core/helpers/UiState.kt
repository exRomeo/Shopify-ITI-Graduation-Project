package com.example.shopify.core.helpers

import com.example.shopify.data.models.Brand
import retrofit2.Response

sealed class UiState{
    object Loading: UiState()
    class Success (val data: Response<List<Brand>>): UiState()
     class Error(val error: Throwable): UiState()
}