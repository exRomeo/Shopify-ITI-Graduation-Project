package com.example.shopify.data.remote

import com.example.shopify.data.models.Brand
import com.example.shopify.data.models.Products
import com.example.shopify.data.models.SmartCollections
import retrofit2.Response

interface IRemoteResource {
suspend fun getBrands():Response<SmartCollections>
suspend fun getRandomProducts():Response<Products>
suspend fun getSpecificBrandProducts(id:Long):Response<Products>
}