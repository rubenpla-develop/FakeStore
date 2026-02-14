package com.rpla.feature_products.domain.repository

import com.rpla.core_domain.entity.ResultBundle
import com.rpla.core_model.Product

interface ProductsRepository {
    suspend fun getProducts(): ResultBundle<List<Product>>
}
