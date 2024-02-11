package com.example.olisapp.model

import com.example.olisapp.Product

data class Item (
    val id: Long,
    val product: Product,
    val quantity: Long
)