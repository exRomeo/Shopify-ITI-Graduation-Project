package com.example.shopify.presentation.screens.homescreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.shopify.core.helpers.UiState
import com.example.shopify.data.repositories.product.IProductRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class HomeViewModel( val repository: IProductRepository):ViewModel() {

    private var _brandsList: MutableStateFlow<UiState> = MutableStateFlow(UiState.Loading)
    val brandList: StateFlow<UiState> = _brandsList

    fun getBrands() {
        viewModelScope.launch(Dispatchers.IO) {
            val response = repository.getBrands()
            withContext(Dispatchers.Main) {
                response
                    .catch {
                        _brandsList.value = UiState.Error(it)
                    }
                    .collect {
                        _brandsList.value = UiState.Success(it)

                    }

            }
        }
    }

}

class HomeViewModelFactory(private val repository: IProductRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(HomeViewModel::class.java))
            HomeViewModel(repository) as T else throw IllegalArgumentException("View Model Class Not Found !!!")
    }
}