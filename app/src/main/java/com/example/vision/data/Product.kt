package com.example.vision.data

data class Product(
    val id: Int,
    val title: String,
    val description: String,
    val category: String,
    val price: Double,
    val thumbnail: String,
    val images: List<String> = emptyList()
)

data class ProductResponse(val products: List<Product>)
