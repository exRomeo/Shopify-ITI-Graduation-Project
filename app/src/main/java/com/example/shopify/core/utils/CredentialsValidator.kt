package com.example.shopify.core.utils

import com.example.shopify.core.utils.Constant.Companion.EMAIL_PATTERN
import com.example.shopify.core.utils.Constant.Companion.PASSWORD_PATTERN
import com.example.shopify.core.utils.Constant.Companion.PHONE_PATTERN


object CredentialsValidator{
    fun isValidEmail(email: String): Boolean {
        return EMAIL_PATTERN.matches(email)
    }

    fun isValidPassword(password: String): Boolean {
        return PASSWORD_PATTERN.matches(password)
    }
    fun isPhoneNumberValid(phoneNumber: String): Boolean {
        return PHONE_PATTERN.matches(phoneNumber)
    }
}