package com.rpla.fakestore.feature.products.domain.repository

import com.rpla.fakestore.core.domain.entity.ResultBundle
import com.rpla.fakestore.core.model.Product

interface ProductsRepository {
    suspend fun getProducts(): ResultBundle<List<Product>>
}
