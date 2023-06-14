package com.example.shopify.data.repositories.product

import com.example.shopify.data.models.Brand
import com.example.shopify.data.models.Products
import com.example.shopify.data.models.SmartCollections
import com.example.shopify.data.remote.RemoteResource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.Response

class ProductRepository(val remoteResource: RemoteResource):IProductRepository {


    override suspend fun getBrands(): Flow<Response<SmartCollections>> {
        return flow {
            emit(remoteResource.getBrands())
        }
    }

    override suspend fun getRandomproducts(): Flow<Response<Products>> {
        return flow {
            emit(remoteResource.getRandomProducts())
        }
    }

    override suspend fun getSpecificBrandProducts(id:Long): Flow<Response<Products>> {
        return flow{
            emit(remoteResource.getSpecificBrandProducts(id))
        }
    }

}