package com.rpla.feature_products.data.remote

import com.rpla.feature_products.data.remote.dto.ProductDto
import retrofit2.http.GET

interface ProductsService {
    @GET("products")
    suspend fun getProducts(): List<ProductDto>
}