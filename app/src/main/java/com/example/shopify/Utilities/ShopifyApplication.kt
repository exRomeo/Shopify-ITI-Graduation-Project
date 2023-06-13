package com.example.shopify.Utilities

import android.app.Application
import com.example.shopify.data.remote.RemoteResource
import com.example.shopify.data.repositories.product.IProductRepository
import com.example.shopify.data.repositories.product.ProductRepository
import com.example.shopify.data.repositories.user.IUserDataRepository
import com.example.shopify.data.repositories.user.UserDataRepository
import com.example.shopify.data.repositories.user.remote.IUserDataRemoteSource
import com.example.shopify.data.repositories.user.remote.UserDataRemoteSource
import com.example.shopify.data.repositories.user.remote.retrofitclient.RetrofitClient

class ShopifyApplication : Application() {

    val repository: IProductRepository by lazy {
        ProductRepository(
            RemoteResource.getInstance(context = applicationContext)
        )
    }


    private val userDataRemoteSource: IUserDataRemoteSource by lazy {
        UserDataRemoteSource(
            RetrofitClient.customerAddressAPI,
            RetrofitClient.draftOrderAPI
        )
    }
    val userDataRepository: IUserDataRepository by lazy {
        UserDataRepository(
            userDataRemoteSource
        )
    }
}