package com.example.shopify.core.helpers

import android.util.Log
import com.example.shopify.data.repositories.authentication.IAuthRepository

/*
    should be used for testing :D
const val USER_ID = 7121192321330
const val WISHLIST_DRAFT_ORDER_ID = 1121329742130
const val CART_DRAFT_ORDER = 1121338360114
*/

object CurrentUserHelper {

    private lateinit var authRepo: IAuthRepository
    var wishlistDraftID: Long = -1
    var cartDraftID: Long = -1
    var customerID: Long = -1L
    var customerName: String = ""
    suspend fun initialize(authRepository: IAuthRepository) {
        authRepo = authRepository
        getUserIDs()
    }

    private suspend fun getUserIDs() {
        if (::authRepo.isInitialized) {
            val ids = authRepo.retrieveCustomerIDs()
            customerID = ids.customer_id!!
            cartDraftID = ids.card_id!!
            wishlistDraftID = ids.wishlist_id!!

            Log.i(
                "CurrentUser",
                "Current User Data: Customer id -> $customerID,Cart Draft ID -> $cartDraftID, Wishlist Draft ID -> $wishlistDraftID"
            )
        }
    }

    fun updateListID(listType: KeyFirebase, newID: Long) {
        if (::authRepo.isInitialized) {
            authRepo.updateCustomerID(listType, newID)
            when (listType) {
                KeyFirebase.wishlist_id -> wishlistDraftID = newID
                KeyFirebase.card_id -> cartDraftID = newID
                KeyFirebase.customer_id -> customerID = newID
                else -> {}
            }
        }
    }

    fun isLoggedIn(): Boolean {
        if (!::authRepo.isInitialized)
            return false
        return authRepo.checkedLoggedIn()
    }

    fun hasWishlist(): Boolean {
        return wishlistDraftID != -1L
    }

    fun hasCart(): Boolean {
        return cartDraftID != -1L
    }

    suspend fun logout() {
        authRepo.signOutFirebase()
        customerID = -1
        cartDraftID = -1
        wishlistDraftID = -1
        customerName = ""
    }
}