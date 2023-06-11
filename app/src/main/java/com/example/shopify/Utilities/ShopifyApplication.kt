package com.example.shopify.Utilities

import android.app.Application
import com.example.shopify.data.remote.RemoteResource
import com.example.shopify.data.repositories.product.IProductRepository
import com.example.shopify.data.repositories.product.ProductRepository

class ShopifyApplication:Application() {

    val repository : IProductRepository by lazy {
        ProductRepository(
            RemoteResource.getInstance(context = applicationContext)
        )
    }
}