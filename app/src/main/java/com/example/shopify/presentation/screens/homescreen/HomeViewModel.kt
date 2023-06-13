package com.example.shopify.presentation.screens.homescreen

import android.util.Log
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
import kotlin.math.log

class HomeViewModel( val repository: IProductRepository):ViewModel() {

    private var _brandsList: MutableStateFlow<UiState> = MutableStateFlow(UiState.Loading)
    val brandList: StateFlow<UiState> = _brandsList

    private var _randomList: MutableStateFlow<UiState> = MutableStateFlow(UiState.Loading)
    val randomList: StateFlow<UiState> = _randomList

    init{
        getBrands()
        getRandomProducts()

    }
    private fun getBrands() {
        viewModelScope.launch(Dispatchers.IO) {
            val response = repository.getBrands()
            withContext(Dispatchers.Main) {
                response
                    .catch {
                        _brandsList.value = UiState.Error(it)

                    }
                    .collect {
                        Log.i("menna","getbrands")
                        _brandsList.value = UiState.Success(it)
                        Log.i("TAG", "getBrands: =======================>")

                    }

            }
        }
    }

    private fun getRandomProducts() {
        viewModelScope.launch(Dispatchers.IO) {
            val response = repository.getRandomproducts()
            withContext(Dispatchers.Main) {
                response
                    .catch {
                        _randomList.value = UiState.Error(it)
                    }
                    .collect {
                        _randomList.value = UiState.Success(it)

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