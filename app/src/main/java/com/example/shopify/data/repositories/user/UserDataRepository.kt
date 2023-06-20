package com.example.shopify.data.repositories.user

import com.example.shopify.data.managers.address.IAddressManager
import com.example.shopify.data.managers.cart.ICartManager
import com.example.shopify.data.managers.wishlist.IWishlistManager
import com.example.shopify.data.models.ProductSample
import com.example.shopify.data.models.address.Address
import kotlinx.coroutines.flow.SharedFlow

class UserDataRepository(
    private val addressManager: IAddressManager,
    private val wishlistManager: IWishlistManager,
    private val cartManager: ICartManager
) :
    IUserDataRepository {
    override val wishlist: SharedFlow<List<ProductSample>> = wishlistManager.wishlist
    override val cart: SharedFlow<List<ProductSample>> = cartManager.cart
    override val address: SharedFlow<List<Address>> = addressManager.addresses
        override suspend fun getAddresses() =
        addressManager
            .getAddresses()

    override suspend fun addAddress(address: Address) =
        addressManager.addAddress(
            address = address
        )

    override suspend fun updateAddress(address: Address) =
        addressManager.updateAddress(
            address = address
        )

    override suspend fun removeAddress(address: Address) =
        addressManager.removeAddress(
            address = address
        )

    override suspend fun getWishlistItems() =
        wishlistManager.getWishlistItems()

    override suspend fun getCartItems() =
        cartManager.getCartItems()
}