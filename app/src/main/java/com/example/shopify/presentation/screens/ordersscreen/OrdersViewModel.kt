package com.example.shopify.presentation.screens.ordersscreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.shopify.core.helpers.UserScreenUISState
import com.example.shopify.core.utils.ConnectionUtil
import com.example.shopify.data.repositories.orders.OrdersRepository
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class OrdersViewModel(private val ordersRepository: OrdersRepository) : ViewModel() {
    private var _snackbarMessage: MutableSharedFlow<Int> = MutableSharedFlow()
    val snackbarMessage = _snackbarMessage.asSharedFlow()

    private var _state: MutableStateFlow<UserScreenUISState> =
        MutableStateFlow(UserScreenUISState.Loading)
    val state = _state.asStateFlow()

    init {
        getOrders()
    }

    fun getOrders() {
        if (ConnectionUtil.isConnected()) {
            viewModelScope.launch {
                ordersRepository.getOrders()
                ordersRepository.orders.collect {
                    if (it.isNotEmpty())
                        _state.value = UserScreenUISState.Success(it)
                    else
                        _state.value = UserScreenUISState.NoData

                }
            }
        } else
            _state.value = UserScreenUISState.NotConnected
    }

    fun cancelOrder(orderID: Long) {
        viewModelScope.launch {
            ordersRepository.deleteOrder(orderID)
            _state.value = UserScreenUISState.NoData
        }
    }
}

class OrdersViewModelFactory(private val ordersRepository: OrdersRepository) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(OrdersViewModel::class.java))
            OrdersViewModel(ordersRepository) as T
        else throw Exception("View Model Not Found!")
    }
}