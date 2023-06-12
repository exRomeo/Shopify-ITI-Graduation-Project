package com.example.shopify.data.repositories.user.remote

import com.example.shopify.BuildConfig
import com.example.shopify.data.models.Product
import com.example.shopify.data.models.address.AddressBody
import com.example.shopify.data.models.address.AddressesResponse
import com.example.shopify.data.models.address.DeleteResponse
import com.example.shopify.data.models.address.NewAddressResponse
import com.example.shopify.data.repositories.user.remote.retrofitclient.CustomerAddressAPI
import retrofit2.Response

class UserDataRemoteSource(
    private val customerAddressAPI: CustomerAddressAPI
) : IUserDataRemoteSource {

    /**
     * Address Functions
     */

    override suspend fun getAddresses(userID: Long): Response<AddressesResponse> =
        customerAddressAPI.getAddresses(
            accessToken = BuildConfig.ACCESS_TOKEN,
            userID = userID
        )

    override suspend fun addAddress(
        userID: Long,
        address: AddressBody
    ): Response<NewAddressResponse> =
        customerAddressAPI.addAddress(
            accessToken = BuildConfig.ACCESS_TOKEN,
            userID = userID,
            address = address
        )

    override suspend fun updateAddress(
        userID: Long,
        addressID: Long,
        address: AddressBody
    ): Response<NewAddressResponse> =
        customerAddressAPI.updateAddress(
            accessToken = BuildConfig.ACCESS_TOKEN,
            userID = userID,
            addressID = addressID,
            address = address
        )

    override suspend fun removeAddress(
        userID: Long,
        addressID: Long
    ): Response<DeleteResponse> =
        customerAddressAPI.removeAddress(
            accessToken = BuildConfig.ACCESS_TOKEN,
            userID = userID,
            addressID = addressID
        )

    /**
     * Orders Functions
     */

    override suspend fun getOrderItems(userID: Int) {
        TODO("Not yet implemented")
    }

    override suspend fun updateOrderItem(product: Product) {
        TODO("Not yet implemented")
    }

    override suspend fun addOrderItem(product: Product) {
        TODO("Not yet implemented")
    }

    override suspend fun removeOrderItem(product: Product) {
        TODO("Not yet implemented")
    }

    /**
     * Wishlist Functions
     */

    override suspend fun getWishlist(userID: Int) {
        TODO("Not yet implemented")
    }

    override suspend fun updateWishlistItem(product: Product) {
        TODO("Not yet implemented")
    }

    override suspend fun addWishlistItem(product: Product) {
        TODO("Not yet implemented")
    }

    override suspend fun removeWishlistItem(product: Product) {
        TODO("Not yet implemented")
    }
}