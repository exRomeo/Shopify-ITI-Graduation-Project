package com.example.shopify.presentation.screens.brands

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.shopify.core.helpers.UiState
import com.example.shopify.data.repositories.product.IProductRepository
import com.example.shopify.presentation.screens.homescreen.HomeViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class BrandsViewModel( val repository: IProductRepository):ViewModel() {
    private var _products: MutableStateFlow<UiState> = MutableStateFlow(UiState.Loading)
    val brandList: StateFlow<UiState> = _products
    var id:Long = 0

    init{
        getSpecificBrandProducts()
    }

     fun getSpecificBrandProducts() {
        viewModelScope.launch(Dispatchers.IO) {
            val response = repository.getSpecificBrandProducts(id)
            withContext(Dispatchers.Main) {
                response
                    .catch {
                        _products.value = UiState.Error(it)
                    }
                    .collect {
                        Log.i("menna", "getproducts")
                        _products.value = UiState.Success(it)

                    }

            }
        }
    }

}


    class BrandsViewModelFactory(private val repository: IProductRepository) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return if (modelClass.isAssignableFrom(BrandsViewModel::class.java))
                BrandsViewModel(repository) as T else throw IllegalArgumentException("View Model Class Not Found !!!")
        }
    }
