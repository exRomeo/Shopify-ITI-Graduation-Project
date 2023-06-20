package com.example.shopify.data.repositories.cart

import com.example.shopify.data.managers.cart.ICartManager
import com.example.shopify.data.models.ProductSample
import com.example.shopify.data.models.currency.CurrencyResponse
import com.example.shopify.data.repositories.cart.remote.ICurrencyRemote
import kotlinx.coroutines.flow.SharedFlow
import retrofit2.Response

class CartRepository(
    private val cartManager: ICartManager,
    private val currencyRemote: ICurrencyRemote
) : ICartRepository {
    override val cart: SharedFlow<List<ProductSample>> = cartManager.cart

    override fun getCartItemCount(product: ProductSample): Long =
        cartManager.getCartItemCount(product = product)

    override suspend fun increaseCartItemCount(product: ProductSample) =
        cartManager.increaseCartItemCount(product = product)

    override suspend fun decreaseCartItemCount(product: ProductSample) =
        cartManager.decreaseCartItemCount(product = product)

    override suspend fun getCartItems() =
        cartManager.getCartItems()

    override suspend fun addCartItem(productID: Long, variantID: Long, quantity: Long) =
        cartManager.addCartItem(
            productID = productID,
            variantID = variantID,
            quantity = quantity
        )

    override suspend fun removeCart(productID: Long) =
        cartManager.removeCart(productID = productID)


    override suspend fun exchangeRate(
        to: String,
        from: String,
        amount: String
    ): Response<CurrencyResponse> =
        currencyRemote.exchangeRate(to = to, from = from, amount = amount)


}