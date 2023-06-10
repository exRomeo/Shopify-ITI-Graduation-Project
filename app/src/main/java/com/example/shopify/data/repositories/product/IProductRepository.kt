package com.example.shopify.data.repositories.product

import com.example.shopify.data.models.Brand
import kotlinx.coroutines.flow.Flow
import retrofit2.Response


interface IProductRepository {
    suspend fun getBrands(): Flow<Response<List<Brand>>>
}