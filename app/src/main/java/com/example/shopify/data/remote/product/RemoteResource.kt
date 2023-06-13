package com.example.shopify.data.remote.product

import android.content.Context
import com.example.shopify.core.helpers.UiState
import com.example.shopify.data.models.Products
import com.example.shopify.data.models.SmartCollections
import com.example.shopify.data.remote.RetrofitHelper
import retrofit2.Response

class RemoteResource private constructor() : IRemoteResource /*ProductSource*/ {
    private val apiService = RetrofitHelper.apiService

    companion object {
        @Volatile
        private var remoteDataSourceInstance: RemoteResource? = null

        @Synchronized
        fun getInstance(context: Context): RemoteResource {
            if (remoteDataSourceInstance == null) {
                remoteDataSourceInstance = RemoteResource()
            }
            return remoteDataSourceInstance!!
        }
    }

    override suspend fun getBrands(): Response<SmartCollections> {
        return apiService.getBrands()
    }

    override suspend fun getRandomProducts(): Response<Products> {
        return apiService.getRandomProducts()
    }

    override suspend fun getProductInfo(productID: Long): UiState =
        try{
            val response = apiService.getProductInfo(productID)
            UiState.Success(response)
        }catch (ex:Exception){
            UiState.Error(ex)
        }

}