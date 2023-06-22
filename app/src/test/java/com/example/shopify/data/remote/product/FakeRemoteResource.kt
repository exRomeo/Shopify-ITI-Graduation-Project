package com.example.shopify.data.remote.product

import com.example.shopify.core.helpers.UiState
import com.example.shopify.data.models.Products
import com.example.shopify.data.models.SingleProductResponseBody
import com.example.shopify.data.models.SmartCollections

import retrofit2.Response
import retrofit2.Response.success

class FakeRemoteResource(private var smartCollectionResponse:SmartCollections,
                         private var ProductsResponse:Products,
                         private var SingleProductResponse: SingleProductResponseBody,
):IRemoteResource
{
    override suspend fun getBrands(): Response<SmartCollections> {
        return success(smartCollectionResponse)
    }

    override suspend fun getRandomProducts(): Response<Products> {
        return success(ProductsResponse)
    }


    override suspend fun getProductInfo(productID: Long): UiState {
       return  UiState.Success(SingleProductResponse)
    }

    override suspend fun getSpecificBrandProducts(id: Long): Response<Products> {
        return success(ProductsResponse)
    }

    override suspend fun getProductsBySubcategory(id: Long, type: String): Response<Products> {
        return success(ProductsResponse)
    }
}