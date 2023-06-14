package com.example.shopify.presentation.screens.settingsscreen

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.shopify.core.helpers.UserScreenUISState
import com.example.shopify.data.models.ProductSample
import com.example.shopify.data.models.address.Address
import com.example.shopify.data.models.draftorder.DraftOrderBody
import com.example.shopify.data.models.draftorder.LineItem
import com.example.shopify.data.repositories.user.IUserDataRepository
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

const val USER_ID = 7103607996722
const val WISHLIST_DRAFT_ORDER_ID = 1121329742130
const val CART_DRAFT_ORDER = 1121338360114

class SettingsViewModel(private val userDataRepository: IUserDataRepository) : ViewModel() {

    private var _settingsState: MutableStateFlow<UserScreenUISState> =
        MutableStateFlow(UserScreenUISState.LoggedIn)
    val settingsState = _settingsState.asStateFlow()

    init {
        getAddresses()
        getWishlistItems()
        getCartItems()
    }

    private var _snackbarMessage: MutableSharedFlow<String> = MutableSharedFlow()
    val snackbarMessage = _snackbarMessage.asSharedFlow()

    /**
     * Address Functions
     */

    private var _addresses: MutableStateFlow<List<Address>> = MutableStateFlow(
        listOf()
    )
    val addresses = _addresses.asStateFlow()

    private fun getAddresses() {
        viewModelScope.launch {
            val response = userDataRepository.getAddresses(USER_ID)
            if (response.isSuccessful && response.body() != null)
                _addresses.value = response.body()!!.addresses
        }
    }

    fun updateAddress(address: Address) {
        viewModelScope.launch {
            val response = userDataRepository.updateAddress(address)
            _snackbarMessage.emit(if (response.isSuccessful) "Address has been updated" else "Failed to updated address")
            getAddresses()
        }

    }

    fun addAddress(address: Address) {
        viewModelScope.launch {
            Log.i(TAG, "addAddress: Called")
            val response = userDataRepository.addAddress(USER_ID, address)
            _snackbarMessage.emit(if (response.isSuccessful) "Address has been added" else "Failed to add address")
            Log.i(TAG, "addAddress: ${response.isSuccessful}")
            Log.i(TAG, "addAddress: ${address.toString()}")
            getAddresses()
        }
    }

    fun removeAddress(address: Address) {
        viewModelScope.launch {
            val response = userDataRepository.removeAddress(address)
            _snackbarMessage.emit(if (response.isSuccessful) "Address has been removed" else "Failed to remove address")
            getAddresses()
        }
    }


    /**
     * Orders Functions
     */

    private var _orders: MutableStateFlow<List<ProductSample>> = MutableStateFlow(
        listOf()
    )
    val orders = _orders.asStateFlow()


    fun updateOrderItem(product: ProductSample) {
        val index = _orders.value.indexOfFirst { it.id == product.id }
        if (index >= 0) {
            val arr = _orders.value.toMutableList()
            arr[index] = product
            _orders.value = arr
        }
    }

    fun addOrderItem(product: ProductSample) {
        product.id = _orders.value.size.toLong()
        val arr = _orders.value.toMutableList()
        arr.add(product)
        _orders.value = arr
    }

    fun removeOrderItem(product: ProductSample) {
        val arr = _orders.value.toMutableList()
        arr.remove(product)
        _orders.value = arr
    }


    /**
     * Wishlist Functions
     */

    private var _wishlist: MutableStateFlow<List<ProductSample>> = MutableStateFlow(listOf())
    val wishlist = _wishlist.asStateFlow()
    private lateinit var wishlistDraftOrder: DraftOrderBody
    fun getWishlistItems() {
        Log.i(TAG, "getWishlistItems: <<<<<<<<<CALLED!")
        viewModelScope.launch {
            val response =
                userDataRepository.getDraftOrder(WISHLIST_DRAFT_ORDER_ID)
            if (response.isSuccessful && response.body() != null) {
                wishlistDraftOrder = response.body()!!
                val draftOrderItems = wishlistDraftOrder.draftOrder.lineItems
                _wishlist.value = getProductsFromLineItems(draftOrderItems)
            }
        }
    }

    private suspend fun getProductsFromLineItems(lineItems: List<LineItem>): List<ProductSample> {
        val lineItemsCopy = ArrayList(lineItems)
        return lineItemsCopy.mapNotNull { lineItem ->
            val productResponse = userDataRepository.getProductByID(lineItem.productID)
            productResponse.body()?.product
        }
    }

    fun addWishlistItem(product: ProductSample) {
        if (!::wishlistDraftOrder.isInitialized)
            getWishlistItems()

        addToWishlistDraftOrder(product)
        updateWishlist()
        getWishlistItems()

    }

    fun updateWishlist() {
        viewModelScope.launch {
            userDataRepository.updateDraftOrder(
                draftOrderID = wishlistDraftOrder.draftOrder.id,
                draftOrderBody = DraftOrderBody(wishlistDraftOrder.draftOrder)
            )
        }
    }

    private fun addToWishlistDraftOrder(product: ProductSample) {
        if (!::wishlistDraftOrder.isInitialized)
            getWishlistItems()
        wishlistDraftOrder.draftOrder.lineItems.add(
            element = LineItem(
                variantID = product.variants[0].id,
                productID = product.id,
                title = product.title,
                quantity = 1,
                name = product.title,
                price = product.variants[0].price ?: "0.00"
            )
        )
    }

    fun removeWishlistItem(product: ProductSample?) {
        product?.let { item ->
            val index: Int =
                wishlistDraftOrder
                    .draftOrder
                    .lineItems
                    .indexOfFirst {
                        it.productID == item.id
                    }
            Log.i(TAG, "removeWishlistItem: $index")
            wishlistDraftOrder
                .draftOrder
                .lineItems
                .removeAt(index)
            viewModelScope.launch {
                userDataRepository.updateDraftOrder(
                    draftOrderID = wishlistDraftOrder.draftOrder.id,
                    draftOrderBody = wishlistDraftOrder
                )
                getWishlistItems()
                _snackbarMessage.emit("Product Removed")
            }
        }

    }


    fun hasWishlist() {

    }

    fun createWishlist() {

    }


    /**
     * Cart Functions
     */

    private var _cart: MutableStateFlow<List<ProductSample>> = MutableStateFlow(
        listOf()
    )
    val cart = _cart.asStateFlow()
    lateinit var cartDraftOrder: DraftOrderBody
    fun getCartItemCount(product: ProductSample): Long {
        var index = cart.value.indexOf(product)
        while (index > cartDraftOrder.draftOrder.lineItems.size) {
            index--
        }
        return cartDraftOrder.draftOrder.lineItems[index].quantity
    }


    fun increaseCartItemCount(product: ProductSample) {
        if (getCartItemCount(product) < 10) {
            cartDraftOrder.draftOrder.lineItems[cart.value.indexOf(product)].quantity++
            updateCart()
        } else {
            viewModelScope.launch {
                _snackbarMessage.emit("Limited to 10 Items")
            }
        }

    }

    fun decreaseCartItemCount(product: ProductSample) {
        cartDraftOrder.draftOrder.lineItems[cart.value.indexOf(product)].quantity--
        updateCart()
    }

    fun getCartItems() {
        viewModelScope.launch {
            val response =
                userDataRepository.getDraftOrder(CART_DRAFT_ORDER)
            if (response.isSuccessful && response.body() != null) {
                cartDraftOrder = response.body()!!
                val draftOrderItems = cartDraftOrder.draftOrder.lineItems
                _cart.value = getProductsFromLineItems(draftOrderItems)
            }
        }
    }


    fun updateCart() {
        viewModelScope.launch {
            userDataRepository.updateDraftOrder(
                draftOrderID = cartDraftOrder.draftOrder.id,
                draftOrderBody = DraftOrderBody(cartDraftOrder.draftOrder)
            )
        }
    }

    fun addCartItem(product: ProductSample) {
        if (!::cartDraftOrder.isInitialized)
            getCartItems()

        addToCartDraftOrder(product)
        updateCart()
        getCartItems()

    }

    private fun addToCartDraftOrder(product: ProductSample) {
        if (!::cartDraftOrder.isInitialized)
            getCartItems()
        cartDraftOrder.draftOrder.lineItems.add(
            element = LineItem(
                variantID = product.variants[0].id,
                productID = product.id,
                title = product.title,
                quantity = 1,
                name = product.title,
                price = product.variants[0].price ?: "0.00"
            )
        )
    }

    fun removeCart(product: ProductSample?) {
        product?.let { item ->
            val index: Int =
                cartDraftOrder
                    .draftOrder
                    .lineItems
                    .indexOfFirst {
                        it.productID == item.id
                    }
            cartDraftOrder
                .draftOrder
                .lineItems
                .removeAt(index)
            viewModelScope.launch {
                userDataRepository.updateDraftOrder(
                    draftOrderID = cartDraftOrder.draftOrder.id,
                    draftOrderBody = cartDraftOrder
                )
                Log.i(TAG, "removeCart: ${cartDraftOrder.draftOrder.id} ")
                getCartItems()
                _snackbarMessage.emit("Product Removed")
            }
        }
    }
}


class SettingsViewModelFactory(private val userDataRepository: IUserDataRepository) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(SettingsViewModel::class.java))
            SettingsViewModel(userDataRepository) as T
        else
            throw Exception("ViewModel Not Found")
    }
}
