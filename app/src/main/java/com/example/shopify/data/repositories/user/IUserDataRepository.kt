package com.example.shopify.data.repositories.user

import com.example.shopify.data.models.ProductSample
import com.example.shopify.data.models.address.Address
import com.example.shopify.data.models.address.AddressesResponse
import com.example.shopify.data.models.address.DeleteResponse
import com.example.shopify.data.models.address.NewAddressResponse
import kotlinx.coroutines.flow.SharedFlow
import retrofit2.Response

interface IUserDataRepository {

    val wishlist: SharedFlow<List<ProductSample>>
    val cart: SharedFlow<List<ProductSample>>
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


    suspend fun getWishlistItems()
    suspend fun getCartItems()

}