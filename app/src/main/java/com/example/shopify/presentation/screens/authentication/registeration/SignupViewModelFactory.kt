package com.example.shopify.presentation.screens.authentication.registeration

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.shopify.data.repositories.authentication.IAuthRepository
import com.example.shopify.presentation.screens.authentication.login.LoginViewModel

class SignupViewModelFactory(private val authRepository: IAuthRepository) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(SignupViewModel::class.java)) {
            SignupViewModel(authRepository) as T
        } else {
            throw IllegalArgumentException("ViewModel class is not found!")
        }
    }
}