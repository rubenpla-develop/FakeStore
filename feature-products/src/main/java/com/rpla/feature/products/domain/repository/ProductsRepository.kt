package com.rpla.feature.products.domain.repository

import com.rpla.core.domain.entity.ResultBundle
import com.rpla.core.model.Product

interface ProductsRepository {
    suspend fun getProducts(): ResultBundle<List<Product>>
}
