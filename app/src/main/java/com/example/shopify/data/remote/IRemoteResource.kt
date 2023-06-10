package com.example.shopify.data.remote

import com.example.shopify.data.models.Brand
import retrofit2.Response

interface IRemoteResource {
suspend fun getBrands():Response<List<Brand>>
}