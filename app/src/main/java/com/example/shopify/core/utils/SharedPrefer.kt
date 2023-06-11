package com.example.shopify.core.utils

import android.content.Context
import android.content.SharedPreferences

object SharedPreference {
    private const val CUSTOMER_ID = "ID"
    fun customPreference(context: Context, name: String): SharedPreferences =
        context.getSharedPreferences(name, Context.MODE_PRIVATE)

    inline fun SharedPreferences.editMe(operation: (SharedPreferences.Editor) -> Unit) {
        val editMe = edit()
        operation(editMe)
        editMe.apply()
    }

    var SharedPreferences.customerID
        get() = getString(CUSTOMER_ID, "")
        set(value) {
            editMe {
                it.putString(CUSTOMER_ID, value)
            }
        }
}