package com.example.shopify.presentation.screens.addressesscreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.shopify.R
import com.example.shopify.core.helpers.UserScreenUISState
import com.example.shopify.data.models.address.Address
import com.example.shopify.data.repositories.address.IAddressRepository
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AddressesViewModel(private val addressRepository: IAddressRepository) : ViewModel() {

    private var _snackbarMessage: MutableSharedFlow<Int> = MutableSharedFlow()
    val snackbarMessage = _snackbarMessage.asSharedFlow()

    private var _screenState: MutableStateFlow<UserScreenUISState> =
        MutableStateFlow(UserScreenUISState.Loading)
    val screenState = _screenState.asStateFlow()

    private var _addresses: MutableStateFlow<List<Address>> =
        MutableStateFlow(listOf())
    val addresses = _addresses.asStateFlow()

    init {
        getAddresses()
    }

    private fun getAddresses() {
        viewModelScope.launch {
            addressRepository.getAddresses()
            addressRepository.address.collect {
                _addresses.value = it
                _screenState.value = UserScreenUISState.Success(it)
            }
        }
    }


    fun updateAddress(address: Address) {
        viewModelScope.launch {
            addressRepository.updateAddress(address)
            _snackbarMessage.emit(R.string.address_updated)
            getAddresses()
        }
    }

    fun addAddress(address: Address) {
        viewModelScope.launch {

            addressRepository.addAddress(address)
            _snackbarMessage.emit(R.string.address_added)
            getAddresses()
        }
    }

    fun removeAddress(address: Address) {
        viewModelScope.launch {
            addressRepository.removeAddress(address)
            _snackbarMessage.emit(R.string.address_removed)
            getAddresses()
        }
    }
}

class AddressesViewModelFactory(private val addressRepository: IAddressRepository) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(AddressesViewModel::class.java))
            AddressesViewModel(addressRepository) as T
        else
            throw Exception("ViewModel Not Found")
    }
}