package com.example.shopify.Utilities.apicustomersresetter

import android.util.Log
import com.example.shopify.presentation.screens.settingsscreen.TAG
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path

/**
 * this file was created to clean the api from testing accounts to we can reuse these accounts for registration later on
 * */
interface DeleteShopifyAccounts {

    @DELETE("admin/api/2023-04/customers/{customer_id}.json")
    @Headers("X-Shopify-Access-Token: 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")
    suspend fun deleteAccountByID(@Path("customer_id") customerID: Long)

    @GET("admin/api/2023-04/customers.json")
    @Headers("X-Shopify-Access-Token: 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")
    suspend fun getAllAccounts(): ResponseObject
}

object RetoDeleter {
    private const val BASE_URL = "https://mad43-alex-and-team2.myshopify.com/"
    private val getDeleter: Retrofit =
        Retrofit.Builder().baseUrl(BASE_URL).addConverterFactory(GsonConverterFactory.create())
            .build()
    private val customerDeleter: DeleteShopifyAccounts =
        getDeleter.create(DeleteShopifyAccounts::class.java)

    fun DeleteAccounts() {
        GlobalScope.launch(Dispatchers.IO) {
            val accounts = customerDeleter.getAllAccounts()
            accounts.responseData.forEach {
                Log.i(TAG, "onCreate: Deleting customer: ${it.first_name}, ${it.last_name}")
                customerDeleter.deleteAccountByID(it.id)
            }
        }
    }
}

class ResponseObject(
    val responseData: List<ResponseData>
)

class ResponseData(
    val id: Long,
    val first_name: String,
    val last_name: String
)