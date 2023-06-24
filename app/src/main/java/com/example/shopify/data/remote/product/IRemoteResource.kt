package com.example.shopify.data.remote.product

import com.example.shopify.core.helpers.UiState
import com.example.shopify.data.models.Products
import com.example.shopify.data.models.SingleProductResponseBody
import com.example.shopify.data.models.SmartCollections
import retrofit2.Response

interface IRemoteResource /*IProductResource*/ {
    suspend fun getBrands(): Response<SmartCollections>
    suspend fun getRandomProducts(): Response<Products>
    suspend fun getProductInfo(productID: Long): Response<SingleProductResponseBody>
    suspend fun getSpecificBrandProducts(id:Long): Response<Products>
    suspend fun getProductsBySubcategory(id: Long, type: String): Response<Products>
}