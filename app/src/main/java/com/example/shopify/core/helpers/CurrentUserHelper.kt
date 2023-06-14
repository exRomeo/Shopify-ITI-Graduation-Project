package com.example.shopify.core.helpers

import android.util.Log
import com.example.shopify.data.repositories.authentication.IAuthRepository
import com.example.shopify.presentation.screens.settingsscreen.TAG

const val USER_ID = 7121192321330
const val WISHLIST_DRAFT_ORDER_ID = 1121329742130
const val CART_DRAFT_ORDER = 1121338360114

object CurrentUserHelper {

    private lateinit var authRepo: IAuthRepository
    var wishlistDraftID: Long = -1
    var cartDraftID: Long = -1
    var customerID: Long = -1L

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
            Log.i(TAG, "GOT IDZZZZ: $customerID, $cartDraftID, $wishlistDraftID")
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
        return customerID != -1L
    }

    fun hasWishlist(): Boolean {
        return wishlistDraftID != -1L
    }

    fun hasCart(): Boolean {
        return cartDraftID != -1L
    }
}