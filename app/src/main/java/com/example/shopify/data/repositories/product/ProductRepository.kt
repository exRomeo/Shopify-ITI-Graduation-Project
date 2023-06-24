package com.example.shopify.data.repositories.product

import com.example.shopify.core.helpers.UiState
import com.example.shopify.data.models.Products
import com.example.shopify.data.models.SingleProductResponseBody
import com.example.shopify.data.models.SmartCollections
import com.example.shopify.data.remote.product.IRemoteResource
import com.example.shopify.data.remote.product.RemoteResource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.Response

class ProductRepository(private val remoteResource: IRemoteResource) : IProductRepository {


    override suspend fun getBrands(): Response<SmartCollections>{
        return remoteResource.getBrands()
        }


    override suspend fun getRandomProducts(): Response<Products> {
        return remoteResource.getRandomProducts()
    }

    override suspend fun getSingleProductDetails(productId: Long): Response<SingleProductResponseBody> =
        remoteResource.getProductInfo(productId)


    override suspend fun getSpecificBrandProducts(id:Long): Response<Products> {
       return remoteResource.getSpecificBrandProducts(id)

    }

    override suspend fun getProductsBySubcategory(
        id: Long,
        type: String
    ): Response<Products> {
        return remoteResource.getProductsBySubcategory(id,type)
        }
    }

