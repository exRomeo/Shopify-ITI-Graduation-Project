package com.example.shopify.core.helpers

import com.example.shopify.data.models.Brand
import com.example.shopify.data.models.SmartCollections
import retrofit2.Response

sealed class UiState{
    object Loading: UiState()
    class Success <T>(val data: Response<T>): UiState()
    class Error(val error: Throwable): UiState()
}