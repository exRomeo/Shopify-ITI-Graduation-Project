package com.example.shopify.data.repositories.user

import com.example.shopify.data.managers.address.IAddressManager
import com.example.shopify.data.managers.cart.ICartManager
import com.example.shopify.data.managers.orders.IOrdersManager
import com.example.shopify.data.managers.wishlist.IWishlistManager
import com.example.shopify.data.models.ProductSample
import com.example.shopify.data.models.address.Address
import com.example.shopify.data.models.order.OrderIn
import kotlinx.coroutines.flow.SharedFlow

class UserDataRepository(
    private val addressManager: IAddressManager,
    private val wishlistManager: IWishlistManager,
    private val cartManager: ICartManager,
    private val ordersManager: IOrdersManager
) :
    IUserDataRepository {
    override val wishlist: SharedFlow<List<ProductSample>> = wishlistManager.wishlist
    override val cart: SharedFlow<List<ProductSample>> = cartManager.cart
    override val address: SharedFlow<List<Address>> = addressManager.addresses
    override val orders: SharedFlow<List<OrderIn>> = ordersManager.orders
    override suspend fun getAddresses() =
        addressManager.getAddresses()

    override suspend fun getWishlistItems() =
        wishlistManager.getWishlistItems()

    override suspend fun getCartItems() =
        cartManager.getCartItems()

    override suspend fun getOrders() =
        ordersManager.getOrders()
}