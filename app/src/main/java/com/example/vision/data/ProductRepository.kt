package com.example.vision.data

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ProductRepository {
    private val api: ProductApi = Retrofit.Builder()
        .baseUrl("https://dummyjson.com/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(ProductApi::class.java)

    suspend fun loadProducts(): List<Product> = api.mensClothing().products

    fun fallbackProducts() = listOf(
        Product(101, "VISION Performance Tee", "Lightweight training tee built for movement and everyday comfort.", "Training Wear", 449.99, ""),
        Product(102, "VISION Elite Hoodie", "Warm premium hoodie for recovery days and casual wear.", "Casual", 899.99, ""),
        Product(103, "VISION Match Shorts", "Breathable football shorts with a flexible athletic fit.", "Football", 549.99, ""),
        Product(104, "VISION Court Tracksuit", "Modern tracksuit designed for warm-ups, travel and off-court style.", "Basketball", 1299.99, ""),
        Product(105, "VISION Run Top", "Quick-dry running top for high intensity sessions.", "Running", 599.99, "")
    )
}
