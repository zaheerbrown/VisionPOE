package com.example.vision.data

import retrofit2.http.GET

interface ProductApi {
    @GET("products/category/mens-shirts")
    suspend fun mensClothing(): ProductResponse
}
