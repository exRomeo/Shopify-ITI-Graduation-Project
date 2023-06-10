package com.example.shopify.data.repositories.product

import com.example.shopify.data.models.Brand
import com.example.shopify.data.remote.RemoteResource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.Response

class ProductRepository(val remoteResource: RemoteResource):IProductRepository {


    override suspend fun getBrands(): Flow<Response<List<Brand>>> {
        return flow {
            emit(remoteResource.getBrands())
        }
    }

}