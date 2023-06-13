package com.example.shopify.data.repositories.product

import com.example.shopify.core.helpers.UiState
import com.example.shopify.data.models.Products
import com.example.shopify.data.models.SmartCollections
import com.example.shopify.data.remote.product.RemoteResource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.Response

class ProductRepository(private val remoteResource: RemoteResource) : IProductRepository {


    override suspend fun getBrands(): Flow<Response<SmartCollections>> {
        return flow {
            emit(remoteResource.getBrands())
        }
    }

    override suspend fun getRandomProducts(): Flow<Response<Products>> {
        return flow {
            emit(remoteResource.getRandomProducts())
        }
    }

    override suspend fun getSingleProductDetails(productId: Long): UiState =
        remoteResource.getProductInfo(productId)


}