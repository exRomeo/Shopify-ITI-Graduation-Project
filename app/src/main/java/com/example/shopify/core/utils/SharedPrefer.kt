package com.example.shopify.core.utils

import android.content.Context
import android.content.SharedPreferences

object SharedPreference {
    private const val CUSTOMER_ID = "ID"
    private const val DISCOUNT_PERCENTAGE = "discountPercentage"
    private const val  HAS_COMPLETED_ONBOARDING ="hasCompletedOnBoarding"
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

    var SharedPreferences.discountPercentage
        get() = getString(DISCOUNT_PERCENTAGE, "")
        set(value) {
            editMe {
                it.putString(DISCOUNT_PERCENTAGE, value)
            }
        }


    var SharedPreferences.hasCompletedOnBoarding
        get() = getBoolean(HAS_COMPLETED_ONBOARDING, false)
        set(value) {
            editMe {
                it.putBoolean(HAS_COMPLETED_ONBOARDING, value)
            }
        }
}