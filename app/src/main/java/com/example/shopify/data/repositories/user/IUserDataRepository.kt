package com.example.shopify.data.repositories.user

import com.example.shopify.data.models.ProductResponse
import com.example.shopify.data.models.address.Address
import com.example.shopify.data.models.address.AddressesResponse
import com.example.shopify.data.models.address.DeleteResponse
import com.example.shopify.data.models.address.NewAddressResponse
import com.example.shopify.data.models.draftorder.DraftOrderBody
import retrofit2.Response

interface IUserDataRepository {

    /**
     * Address Functions
     */

    suspend fun getAddresses(userID: Long): Response<AddressesResponse>

    suspend fun addAddress(
        userID: Long,
        address: Address
    ): Response<NewAddressResponse>

    suspend fun updateAddress(
        address: Address
    ): Response<NewAddressResponse>

    suspend fun removeAddress(
        address: Address
    ): Response<DeleteResponse>


    /**
     * DraftOrder functions
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

    suspend fun deleteDraftOrder(
        draftOrderID: Long
    )


    suspend fun getProductByID(productID: Long): Response<ProductResponse>

}