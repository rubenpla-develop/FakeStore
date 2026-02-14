package com.rpla.feature_products.data.remote

import com.rpla.feature_products.data.remote.dto.ProductDto

interface ProductsService {
    @GET("products")
    suspend fun getProducts(): List<ProductDto>
}