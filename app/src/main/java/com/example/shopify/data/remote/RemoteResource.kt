package com.example.shopify.data.remote

import android.content.Context
import com.example.shopify.data.models.Brand
import com.example.shopify.data.models.Products
import com.example.shopify.data.models.SmartCollections
import retrofit2.Response

class RemoteResource private constructor():IRemoteResource {
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

}