package com.example.olisapp.screen.order

import androidx.lifecycle.ViewModel
import com.example.olisapp.Product
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class OrderViewModel : ViewModel() {

    private val _productsLoadingState = MutableStateFlow<ProductsLoadingState>(ProductsLoadingState.Loading)
    val productsLoadingState: StateFlow<ProductsLoadingState> get() = _productsLoadingState

    init {
        getProducts()
    }

    private fun getProducts() {
        CoroutineScope(Dispatchers.IO).launch {
            _productsLoadingState.value = ProductsLoadingState.Loading

            val databaseReference = FirebaseDatabase.getInstance().reference.child("products")
            databaseReference.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    val products = mutableListOf<Product>()
                    for (productSnapshot in dataSnapshot.children) {
                        val id = productSnapshot.child("id").getValue(Long::class.java)
                        val name = productSnapshot.child("name").getValue(String::class.java)
                        val price = productSnapshot.child("price").getValue(Double::class.java)
                        val product = Product(id = id, name = name.orEmpty(), price = price ?: 0.0)
                        products.add(product)
                    }
                    _productsLoadingState.value = ProductsLoadingState.Success(products)
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    _productsLoadingState.value = ProductsLoadingState.Error(databaseError.message)
                }
            })
        }
    }
}

sealed class ProductsLoadingState {
    object Loading : ProductsLoadingState()
    data class Success(val products: List<Product>) : ProductsLoadingState()
    data class Error(val errorMessage: String) : ProductsLoadingState()
}