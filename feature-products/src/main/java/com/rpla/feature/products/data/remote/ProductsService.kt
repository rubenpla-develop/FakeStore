package com.rpla.feature.products.data.remote

import com.rpla.feature.products.data.remote.dto.ProductDto
import retrofit2.http.GET

interface ProductsService {
    @GET("products")
    suspend fun getProducts(): List<ProductDto>
}
