package com.example.shopify.presentation.screens.settingsscreen

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.shopify.core.helpers.CurrentUserHelper
import com.example.shopify.core.helpers.KeyFirebase
import com.example.shopify.core.helpers.UserScreenUISState
import com.example.shopify.data.models.ProductSample
import com.example.shopify.data.models.address.Address
import com.example.shopify.data.models.draftorder.DraftOrder
import com.example.shopify.data.models.draftorder.DraftOrderBody
import com.example.shopify.data.models.draftorder.LineItem
import com.example.shopify.data.repositories.user.IUserDataRepository
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch


class SettingsViewModel(private val userDataRepository: IUserDataRepository) : ViewModel() {

    private var _settingsState: MutableStateFlow<UserScreenUISState> =
        MutableStateFlow(UserScreenUISState.Loading)
    val settingsState = _settingsState.asStateFlow()

    init {
        if (CurrentUserHelper.isLoggedIn()) {
            getAddresses()
            getWishlistItems()
            getCartItems()
            _settingsState.value = UserScreenUISState.LoggedIn
        } else {
            _settingsState.value = UserScreenUISState.NotLoggedIn
        }
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
            val response = userDataRepository.getAddresses(CurrentUserHelper.customerID)
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
            val response = userDataRepository.addAddress(CurrentUserHelper.customerID, address)
            _snackbarMessage.emit(if (response.isSuccessful) "Address has been added" else "Failed to add address")
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
    private fun getWishlistItems() {
        if (CurrentUserHelper.hasWishlist())
            viewModelScope.launch {
                val response =
                    userDataRepository.getDraftOrder(CurrentUserHelper.wishlistDraftID)
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
        viewModelScope.launch {
            if (CurrentUserHelper.hasWishlist())
                addToWishlistDraftOrder(product)
            else
                createWishlist(product)
            updateWishlist()
            getWishlistItems()
        }

    }

    private fun updateWishlist() {
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

    fun removeWishlistItem(product: ProductSample) {
        if (wishlistDraftOrder.draftOrder.lineItems.size > 1) {
            val index: Int =
                wishlistDraftOrder
                    .draftOrder
                    .lineItems
                    .indexOfFirst {
                        it.productID == product.id
                    }
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
        } else {
            deleteDraftOrder(
                draftOrder = wishlistDraftOrder.draftOrder,
                draftOrderType = KeyFirebase.wishlist_id
            )
            _wishlist.value = listOf()
        }
    }

    private fun deleteDraftOrder(draftOrder: DraftOrder, draftOrderType: KeyFirebase) {
        viewModelScope.launch {
            userDataRepository.deleteDraftOrder(draftOrder.id)
            wishlistDraftOrder.draftOrder.lineItems = mutableListOf()
            CurrentUserHelper.updateListID(listType = draftOrderType, -1)
        }
    }

    private suspend fun createWishlist(product: ProductSample) {
        wishlistDraftOrder = DraftOrderBody(
            DraftOrder(
                id = 0L,
                note = ">wishlist<",
                lineItems = mutableListOf(
                    LineItem(
                        productID = product.id,
                        variantID = product.variants[0].id,
                        title = product.title,
                        name = product.variants[0].title ?: "",
                        price = product.variants[0].price ?: "",
                        quantity = 1L
                    )
                ),
                totalPrice = ""
            )
        )
        val response = userDataRepository
            .createDraftOrder(wishlistDraftOrder)

        CurrentUserHelper.updateListID(
            listType = KeyFirebase.wishlist_id,
            response.body()?.draftOrder?.id ?: -1L
        )
    }


    /**
     * Cart Functions
     */

    private var _cart: MutableStateFlow<List<ProductSample>> = MutableStateFlow(listOf())
    val cart = _cart.asStateFlow()
    private lateinit var cartDraftOrder: DraftOrderBody
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

    private fun getCartItems() {
        if (CurrentUserHelper.hasCart())
            viewModelScope.launch {
                val response =
                    userDataRepository.getDraftOrder(CurrentUserHelper.cartDraftID)
                if (response.isSuccessful && response.body() != null) {
                    cartDraftOrder = response.body()!!
                    val draftOrderItems = cartDraftOrder.draftOrder.lineItems
                    _cart.value = getProductsFromLineItems(draftOrderItems)
                }
            }
    }


    fun addCartItem(product: ProductSample) {
        if (!::cartDraftOrder.isInitialized)
            getCartItems()
        viewModelScope.launch {
            if (CurrentUserHelper.hasCart())
                addToCartDraftOrder(product)
            else
                createCart(product)
            updateCart()
            getCartItems()
        }
    }

    private fun updateCart() {
        viewModelScope.launch {
            userDataRepository.updateDraftOrder(
                draftOrderID = cartDraftOrder.draftOrder.id,
                draftOrderBody = DraftOrderBody(cartDraftOrder.draftOrder)
            )
        }
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

    fun removeCart(product: ProductSample) {
        if (cartDraftOrder.draftOrder.lineItems.size > 1) {
            val index: Int =
                cartDraftOrder
                    .draftOrder
                    .lineItems
                    .indexOfFirst {
                        it.productID == product.id
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
                getCartItems()
                _snackbarMessage.emit("Product Removed")
            }
        } else {
            deleteDraftOrder(
                draftOrder = cartDraftOrder.draftOrder,
                draftOrderType = KeyFirebase.card_id
            )
            _cart.value = listOf()
        }

    }

    private suspend fun createCart(product: ProductSample) {
        cartDraftOrder = DraftOrderBody(
            DraftOrder(
                id = 0L,
                note = ">Cart<",
                lineItems = mutableListOf(
                    LineItem(
                        productID = product.id,
                        variantID = product.variants[0].id,
                        title = product.title,
                        name = product.variants[0].title ?: "",
                        price = product.variants[0].price ?: "",
                        quantity = 1L
                    )
                ),
                totalPrice = ""
            )
        )
        val response = userDataRepository
            .createDraftOrder(cartDraftOrder)

        CurrentUserHelper.updateListID(
            listType = KeyFirebase.card_id,
            response.body()?.draftOrder?.id ?: -1L
        )
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
