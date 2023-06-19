package com.example.shopify.data.repositories.user

import com.example.shopify.data.managers.cart.ICartManager
import com.example.shopify.data.managers.wishlist.IWishlistManager
import com.example.shopify.data.models.ProductSample
import com.example.shopify.data.models.address.Address
import com.example.shopify.data.models.address.AddressBody
import com.example.shopify.data.models.address.AddressesResponse
import com.example.shopify.data.models.address.DeleteResponse
import com.example.shopify.data.models.address.NewAddressResponse
import com.example.shopify.data.repositories.user.remote.IUserDataRemoteSource
import kotlinx.coroutines.flow.SharedFlow
import retrofit2.Response

class UserDataRepository(
    private val userDataRemoteSource: IUserDataRemoteSource,
    private val wishlistManager: IWishlistManager,
    private val cartManager: ICartManager,
) :
    IUserDataRepository {


    override val wishlist: SharedFlow<List<ProductSample>> = wishlistManager.wishlist
    override val cart: SharedFlow<List<ProductSample>> = cartManager.cart

    /**
     * Address Functions
     */

    override suspend fun getAddresses(
        userID: Long
    ): Response<AddressesResponse> =
        userDataRemoteSource
            .getAddresses(
                userID = userID
            )

    override suspend fun addAddress(
        userID: Long,
        address: Address
    ): Response<NewAddressResponse> =
        userDataRemoteSource.addAddress(
            userID = userID,
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

    override suspend fun getWishlistItems() =
        wishlistManager.getWishlistItems()

    override suspend fun getCartItems() =
        cartManager.getCartItems()


}