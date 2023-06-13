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
import com.example.shopify.data.repositories.user.remote.retrofitclient.DRAFT_ORDER_ID
import com.example.shopify.data.repositories.user.remote.retrofitclient.USER_ID
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class SettingsViewModel(private val userDataRepository: IUserDataRepository) : ViewModel() {

    init {
        getAddresses()
        getWishlistItems()
    }

    private var _toastMessage: MutableSharedFlow<String> = MutableSharedFlow()
    val toastMessage = _toastMessage.asSharedFlow()

    /**
     * Address Functions
     */

    private var _addresses: MutableStateFlow<UserScreenUISState> = MutableStateFlow(
        UserScreenUISState.Loading
    )
    val addresses = _addresses.asStateFlow()

    private fun getAddresses() {
        viewModelScope.launch {
            val response = userDataRepository.getAddresses(USER_ID)
            _addresses.value = if (response.isSuccessful && response.body() != null)
                UserScreenUISState.Success(response.body()!!.addresses)
            else
                UserScreenUISState.Failure(response.message())
        }
    }

    fun updateAddress(address: Address) {
        viewModelScope.launch {
            val response = userDataRepository.updateAddress(address)
            _toastMessage.emit(if (response.isSuccessful) "Address has been updated" else "Failed to updated address")
            getAddresses()
        }

    }

    fun addAddress(address: Address) {
        viewModelScope.launch {
            Log.i(TAG, "addAddress: Called")
            val response = userDataRepository.addAddress(address)
            _toastMessage.emit(if (response.isSuccessful) "Address has been added" else "Failed to add address")
            Log.i(TAG, "addAddress: ${response.isSuccessful}")
            Log.i(TAG, "addAddress: ${address.toString()}")
            getAddresses()
        }
    }

    fun removeAddress(address: Address) {
        viewModelScope.launch {
            val response = userDataRepository.removeAddress(address)
            _toastMessage.emit(if (response.isSuccessful) "Address has been removed" else "Failed to remove address")
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
        viewModelScope.launch {
            val response =
                userDataRepository.getDraftOrder(DRAFT_ORDER_ID)
            if (response.isSuccessful && response.body() != null) {
                wishlistDraftOrder = response.body()!!
                val draftOrderItems = wishlistDraftOrder.draftOrder.lineItems
                _wishlist.value = getProductsFromLineItems(draftOrderItems)
            }
        }
    }

    private suspend fun getProductsFromLineItems(lineItems: List<LineItem>): MutableList<ProductSample> {
        val items: MutableList<ProductSample> = mutableListOf()
        lineItems.forEach {
            val productResponse = userDataRepository.getProductByID(it.productID)
            if (productResponse.isSuccessful && productResponse.body() != null) {
                items.add(productResponse.body()!!.product)
            }
        }
        return items
    }

    fun addWishlistItem(product: ProductSample) {
        if(!::wishlistDraftOrder.isInitialized)
            getWishlistItems()
        viewModelScope.launch {
            addToDraftOrder(product)
            userDataRepository.updateDraftOrder(
                draftOrderID = wishlistDraftOrder.draftOrder.id,
                draftOrderBody = DraftOrderBody(wishlistDraftOrder.draftOrder)
            )
            getWishlistItems()
        }
    }

    private fun addToDraftOrder(product: ProductSample) {
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

    fun updateCart(product: ProductSample) {
        val index = _cart.value.indexOfFirst { it.id == product.id }
        if (index >= 0) {
            val arr = _cart.value.toMutableList()
            arr[index] = product
            _cart.value = arr
        }
    }

    fun addCart(product: ProductSample) {
        product.id = _cart.value.size.toLong()
        val arr = _cart.value.toMutableList()
        arr.add(product)
        _cart.value = arr
    }

    fun removeCart(product: ProductSample) {
        val arr = _cart.value.toMutableList()
        arr.remove(product)
        _cart.value = arr
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
