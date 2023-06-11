package com.example.shopify.presentation.screens.authentication.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.shopify.data.repositories.authentication.IAuthRepository

class LoginViewModelFactory (private val authRepository: IAuthRepository):ViewModelProvider.Factory{
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if(modelClass.isAssignableFrom(LoginViewModel::class.java)) {
            LoginViewModel(authRepository) as T
        }else{
            throw IllegalArgumentException("ViewModel class is not found!")
        }
    }
}