package com.example.shopify.data.repositories.product

import com.example.shopify.core.helpers.UiState
import com.example.shopify.data.models.Products
import com.example.shopify.data.models.SingleProductResponseBody
import com.example.shopify.data.models.SmartCollections

import retrofit2.Response

class FakeProductRepository(
    private var smartCollectionResponse:SmartCollections,
    private var ProductsResponse:Products,
    private var SingleProductResponse: SingleProductResponseBody,
):IProductRepository{
    override suspend fun getBrands(): Response<SmartCollections> {
        return Response.success(smartCollectionResponse)
    }


    override suspend fun getSpecificBrandProducts(id: Long): Response<Products> {
        return Response.success(ProductsResponse)
    }

    override suspend fun getProductsBySubcategory(id: Long, type: String): Response<Products> {
        return Response.success(ProductsResponse)
    }

    override suspend fun getSingleProductDetails(productId: Long): UiState {
        return UiState.Success(SingleProductResponse)
    }

    override suspend fun getRandomProducts(): Response<Products> {
        return Response.success(ProductsResponse)
    }
}