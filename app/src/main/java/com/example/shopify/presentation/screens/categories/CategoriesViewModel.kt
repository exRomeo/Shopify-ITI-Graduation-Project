package com.example.shopify.presentation.screens.categories

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


class CategoriesViewModel(val repository: IProductRepository): ViewModel() {
    private var _productsList: MutableStateFlow<UiState> = MutableStateFlow(UiState.Loading)
    val productsList: StateFlow<UiState> = _productsList
    var id: Long = 449428980018
    var type: String = ""

    init {
        getProductsBySubcategory()
    }

     fun getProductsBySubcategory() {
        viewModelScope.launch(Dispatchers.IO) {
            val response = repository.getProductsBySubcategory(id, type)
            withContext(Dispatchers.Main) {
                response
                    .catch {
                        _productsList.value = UiState.Error(it)

                    }
                    .collect {
                        Log.i("menna", "getproducts")
                        _productsList.value = UiState.Success(it)


                    }

            }
        }
    }
}




class CategoriesViewModelFactory(private val repository: IProductRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(CategoriesViewModel::class.java))
            CategoriesViewModel(repository) as T else throw IllegalArgumentException("View Model Class Not Found !!!")
    }
}

























