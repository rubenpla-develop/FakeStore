package com.rpla.fakestore.feature.products.domain.usecase

import com.rpla.fakestore.core.domain.base.BaseUseCase
import com.rpla.fakestore.core.domain.entity.ResultBundle
import com.rpla.fakestore.core.model.Product
import com.rpla.fakestore.feature.products.domain.GetProductListRequest
import com.rpla.fakestore.feature.products.domain.repository.ProductsRepository
import javax.inject.Inject

class GetProductsListUseCase
    @Inject
    constructor(
        private val productsRepository: ProductsRepository,
    ) : BaseUseCase<GetProductListRequest, List<Product>>() {
        override suspend fun run(request: GetProductListRequest): ResultBundle<List<Product>> = productsRepository.getProducts()
    }
