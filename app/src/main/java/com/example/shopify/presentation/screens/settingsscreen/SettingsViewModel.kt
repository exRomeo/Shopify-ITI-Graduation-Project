package com.example.shopify.presentation.screens.settingsscreen

import androidx.lifecycle.ViewModel
import com.example.shopify.data.models.Address
import com.example.shopify.data.models.Product
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class SettingsViewModel() : ViewModel() {
    private var _addresses: MutableStateFlow<List<Address>> = MutableStateFlow(
        listOf(
            Address(
                id = 0,
                phoneNumber = "+20 155 6003 397",
                firstName = "Ramy",
                lastName = "Ashraf",
                fullAddress = "2 ammar bn yasser st. Al-Agami, Alexandria"
            )
        )
    )
    val addresses = _addresses.asStateFlow()


    private var _orders: MutableStateFlow<List<Product>> = MutableStateFlow(
        listOf(
            Product(
                5,
                "Nice Shoes",
                99.99,
                "https://cdn.shopify.com/s/files/1/0771/5900/6514/products/9f190cba7218c819c81566bca6298c6a.jpg?v=1685528902",
                "26/06/2023",
                1
            )
        )
    )
    val orders = _orders.asStateFlow()


    private var _wishlist: MutableStateFlow<List<Product>> = MutableStateFlow(
        listOf(
            Product(
                5,
                "Nice Shoes",
                99.99,
                "https://cdn.shopify.com/s/files/1/0771/5900/6514/products/9f190cba7218c819c81566bca6298c6a.jpg?v=1685528902",
                "26/06/2023",
                1
            )
        )
    )
    val wishlist = _wishlist.asStateFlow()

    private var _cart: MutableStateFlow<List<Product>> = MutableStateFlow(
        listOf(
            Product(
                5,
                "Nice Shoes",
                99.99,
                "https://cdn.shopify.com/s/files/1/0771/5900/6514/products/9f190cba7218c819c81566bca6298c6a.jpg?v=1685528902",
                "26/06/2023",
                1
            )
        )
    )
    val cart = _cart.asStateFlow()

    /**
     * Address Functions
     */

    fun updateAddress(address: Address) {
        val index = _addresses.value.indexOfFirst { it.id == address.id }
        if (index >= 0) {
            val arr = _addresses.value.toMutableList()
            arr[index] = address
            _addresses.value = arr
        }
    }

    fun addAddress(address: Address) {
        address.id = _addresses.value.size
        val arr = _addresses.value.toMutableList()
        arr.add(address)
        _addresses.value = arr
    }

    fun removeAddress(address: Address) {
        val arr = _addresses.value.toMutableList()
        arr.remove(address)
        _addresses.value = arr
    }


    /**
     * Orders Functions
     */
    fun updateOrderItem(product: Product) {
        val index = _orders.value.indexOfFirst { it.id == product.id }
        if (index >= 0) {
            val arr = _orders.value.toMutableList()
            arr[index] = product
            _orders.value = arr
        }
    }

    fun addOrderItem(product: Product) {
        product.id = _orders.value.size
        val arr = _orders.value.toMutableList()
        arr.add(product)
        _orders.value = arr
    }

    fun removeOrderItem(product: Product) {
        val arr = _orders.value.toMutableList()
        arr.remove(product)
        _orders.value = arr
    }


    /**
     * Wishlist Functions
     */
    fun updateWishlistItem(product: Product) {
        val index = _wishlist.value.indexOfFirst { it.id == product.id }
        if (index >= 0) {
            val arr = _wishlist.value.toMutableList()
            arr[index] = product
            _wishlist.value = arr
        }
    }

    fun addWishlistItem(product: Product) {
        product.id = _wishlist.value.size
        val arr = _wishlist.value.toMutableList()
        arr.add(product)
        _wishlist.value = arr
    }

    fun removeWishlistItem(product: Product) {
        val arr = _wishlist.value.toMutableList()
        arr.remove(product)
        _wishlist.value = arr
    }


    /**
     * Cart Functions
     */
    fun updateCart(product: Product) {
        val index = _cart.value.indexOfFirst { it.id == product.id }
        if (index >= 0) {
            val arr = _cart.value.toMutableList()
            arr[index] = product
            _cart.value = arr
        }
    }

    fun addCart(product: Product) {
        product.id = _cart.value.size
        val arr = _cart.value.toMutableList()
        arr.add(product)
        _cart.value = arr
    }

    fun removeCart(product: Product) {
        val arr = _cart.value.toMutableList()
        arr.remove(product)
        _cart.value = arr
    }
}

