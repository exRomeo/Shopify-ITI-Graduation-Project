package com.example.shopify.data.repositories.product

import com.example.shopify.core.helpers.UiState
import com.example.shopify.data.models.Products
import com.example.shopify.data.models.SingleProductResponseBody
import com.example.shopify.data.models.SmartCollections
import kotlinx.coroutines.flow.Flow
import retrofit2.Response


interface IProductRepository {
    suspend fun getBrands(): Response<SmartCollections>
    suspend fun getSpecificBrandProducts(id:Long):Response<Products>
    suspend fun getProductsBySubcategory(id:Long,type:String):Response<Products>
    suspend fun getSingleProductDetails(productId: Long): UiState
    suspend fun getRandomProducts(): Response<Products>

}