package com.example.shopify.data.repositories.user

import com.example.shopify.data.models.address.Address
import com.example.shopify.data.models.address.AddressBody
import com.example.shopify.data.models.address.AddressesResponse
import com.example.shopify.data.models.address.DeleteResponse
import com.example.shopify.data.models.address.NewAddressResponse
import com.example.shopify.data.repositories.user.remote.IUserDataRemoteSource
import com.example.shopify.data.repositories.user.remote.UserDataRemoteSource
import com.example.shopify.data.repositories.user.remote.retrofitclient.USER_ID
import retrofit2.Response

class UserDataRepository(private val userDataRemoteSource: IUserDataRemoteSource): IUserDataRepository {


    /**
     * Address Functions
     */

    override suspend fun getAddresses(userID: Long): Response<AddressesResponse> =
        userDataRemoteSource.getAddresses(
            userID = userID
        )

    override suspend fun addAddress(
        address: Address
    ): Response<NewAddressResponse> =
        userDataRemoteSource.addAddress(
            userID = USER_ID,
            address = AddressBody(address)
        )

    override suspend fun updateAddress(
        address: Address
    ): Response<NewAddressResponse> =
        userDataRemoteSource.updateAddress(
            userID = address.customerID,
            addressID = address.id,
            address = AddressBody(address)
        )

    override suspend fun removeAddress(
        address: Address
    ): Response<DeleteResponse> =
        userDataRemoteSource.removeAddress(
            userID = address.customerID,
            addressID = address.id
        )
}