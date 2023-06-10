package com.example.shopify.data.remote

import com.example.shopify.presentation.screens.homescreen.Brand
import retrofit2.Response

interface RemoteResource {
suspend fun getBrands():Response<Brand>
}