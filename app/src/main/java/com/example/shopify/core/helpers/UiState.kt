package com.example.shopify.core.helpers

import okhttp3.ResponseBody

sealed class UiState{
    object Loading: UiState()
    class Success <T>(val data: T): UiState()
    class Error(val error: ResponseBody?): UiState()
}