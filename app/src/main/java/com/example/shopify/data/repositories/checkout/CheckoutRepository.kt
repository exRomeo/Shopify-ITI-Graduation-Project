package com.example.shopify.data.repositories.checkout

import com.example.shopify.data.managers.address.IAddressManager
import com.example.shopify.data.managers.cart.ICartManager
import com.example.shopify.data.managers.orders.IOrdersManager
import com.example.shopify.data.models.ProductSample
import com.example.shopify.data.models.address.Address
import com.example.shopify.data.models.currency.CurrencyResponse
import com.example.shopify.data.models.draftorder.LineItem
import com.example.shopify.data.models.order.OrderBody
import com.example.shopify.data.repositories.cart.remote.ICurrencyRemote
import kotlinx.coroutines.flow.SharedFlow
import retrofit2.Response

class CheckoutRepository(
    private val cartManager: ICartManager,
    private val ordersManager: IOrdersManager,
    private val currencyRemote: ICurrencyRemote,
    private val addressManager: IAddressManager
) : ICheckoutRepository {

    override val addresses: SharedFlow<List<Address>> = addressManager.addresses

    override suspend fun updateCart() =
        cartManager.getCartItems()

    override suspend fun getCart(): List<LineItem> = cartManager.getLineItems()

    override fun getCartItemCount(product: ProductSample): Long =
        cartManager.getCartItemCount(product = product)

    override suspend fun makeOrder(orderBody: OrderBody) = ordersManager.makeOrder(orderBody)

    override suspend fun exchangeRate(
        to: String,
        from: String,
        amount: String
    ): Response<CurrencyResponse> =
        currencyRemote.exchangeRate(to = to, from = from, amount = amount)

    override suspend fun getCustomerAddresses() = addressManager.getAddresses()
}