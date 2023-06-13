package com.example.shopify.data.repositories.user.remote

import com.example.shopify.data.models.Product
import com.example.shopify.data.models.ProductSample
import com.example.shopify.data.models.address.AddressBody
import com.example.shopify.data.models.address.AddressesResponse
import com.example.shopify.data.models.address.DeleteResponse
import com.example.shopify.data.models.address.NewAddressResponse
import com.example.shopify.data.models.draftorder.DraftOrderResponse
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


    suspend fun getWishlistItems(draftOrderID: Long): DraftOrderResponse

    suspend fun addWishlistItem(draftOrderID: Long): DraftOrderResponse

    suspend fun updateWishlistItem(product: ProductSample)


}