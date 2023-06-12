package com.example.shopify.data.models

import com.example.shopify.core.helpers.AuthenticationResponseState
import com.example.shopify.data.repositories.authentication.IAuthRepository

data class CollectCurrentCustomerData(
    val customerFirebase: CustomerFirebase?,
    val simpleResponse: SimpleResponse?

)
object GetCurrentCustomer {
    suspend fun getCurrentCustomer(authRepository: IAuthRepository): CollectCurrentCustomerData{
        val simpleResponse : SimpleResponse?
        val customerFirebase = authRepository.retrieveCustomerIDs()
        val customerShopify = customerFirebase.customer_id?.let { authRepository.getSingleCustomerFromShopify(it) }
        when(val response =customerShopify){
            is AuthenticationResponseState.Success->{
               simpleResponse =  response.responseBody?.customer
            }
            else ->{
                simpleResponse = SimpleResponse(-1,null,null,null,null)
            }
        }
        return CollectCurrentCustomerData(customerFirebase,simpleResponse)
    }
}