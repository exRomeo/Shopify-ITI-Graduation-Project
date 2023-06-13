package com.example.shopify.data.repositories.user.remote

import com.example.shopify.data.models.Product
import com.example.shopify.data.models.ProductResponse
import com.example.shopify.data.models.address.AddressBody
import com.example.shopify.data.models.address.AddressesResponse
import com.example.shopify.data.models.address.DeleteResponse
import com.example.shopify.data.models.address.NewAddressResponse
import com.example.shopify.data.models.draftorder.DraftOrderBody
import retrofit2.Response

interface IUserDataRemoteSource {

    /**
     * Address Functions
     */
    suspend fun getAddresses(userID: Long): Response<AddressesResponse>

    suspend fun addAddress(
        userID: Long,
        address: AddressBody
    ): Response<NewAddressResponse>

    suspend fun updateAddress(
        userID: Long,
        addressID: Long,
        address: AddressBody
    ): Response<NewAddressResponse>

    suspend fun removeAddress(
        userID: Long,
        addressID: Long
    ): Response<DeleteResponse>


    /**
     * Orders Functions
     */
    suspend fun getOrderItems(userID: Int)

    suspend fun addOrderItem(product: Product)

    suspend fun updateOrderItem(product: Product)

    suspend fun removeOrderItem(product: Product)


    /**
     * Wishlist functions
     */


    suspend fun getDraftOrder(
        draftOrderID: Long
    ): Response<DraftOrderBody>

    suspend fun createDraftOrder(
        draftOrderBody: DraftOrderBody
    ): Response<DraftOrderBody>

    suspend fun updateDraftOrder(
        draftOrderID: Long,
        draftOrderBody: DraftOrderBody
    ): Response<DraftOrderBody>

    suspend fun getProductByID(productID:Long): Response<ProductResponse>

}