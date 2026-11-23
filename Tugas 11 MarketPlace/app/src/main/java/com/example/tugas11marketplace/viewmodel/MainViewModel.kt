package com.example.tugas11marketplace.viewmodel

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tugas11marketplace.model.Product
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {
    private val _productList = mutableStateListOf<Product>()
    val productList: List<Product> = _productList

    init {
        _productList.add(
            Product(name = "Brownies Lumer", price = "15000", description = "Cokelat melimpah.")
        )
        _productList.add(
            Product(name = "Kaos Custom", price = "85000", description = "Bahan adem.")
        )
    }

    fun addProduct(name: String, price: String, description: String, onComplete: () -> Unit) {
        viewModelScope.launch {
            delay(1000)
            val newProduct = Product(name = name, price = price, description = description)
            _productList.add(0, newProduct)
            onComplete()
        }
    }
}