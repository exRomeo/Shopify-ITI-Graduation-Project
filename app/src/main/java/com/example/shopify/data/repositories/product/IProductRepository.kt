package com.example.shopify.data.repositories.product

import com.example.shopify.core.helpers.UiState
import com.example.shopify.data.models.Products
import com.example.shopify.data.models.SingleProductResponseBody
import com.example.shopify.data.models.SmartCollections
import kotlinx.coroutines.flow.Flow
import retrofit2.Response


interface IProductRepository {
    suspend fun getBrands(): Flow<Response<SmartCollections>>
    suspend fun getSpecificBrandProducts(id:Long): Flow<Response<Products>>
    suspend fun getProductsBySubcategory(id:Long,type:String): Flow<Response<Products>>
    suspend fun getSingleProductDetails(productId: Long): UiState
    suspend fun getRandomProducts(): Flow<Response<Products>>

}