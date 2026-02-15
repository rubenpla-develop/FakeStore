package com.rpla.feature.products.domain.usecase

import com.rpla.core.domain.base.BaseUseCase
import com.rpla.core.domain.entity.ResultBundle
import com.rpla.core.model.Product
import com.rpla.feature.products.domain.GetProductListRequest
import com.rpla.feature.products.domain.repository.ProductsRepository
import javax.inject.Inject

class GetProductsListUseCase
    @Inject
    constructor(
        private val productsRepository: ProductsRepository,
    ) : BaseUseCase<GetProductListRequest, List<Product>>() {
        override suspend fun run(request: GetProductListRequest): ResultBundle<List<Product>> = productsRepository.getProducts()
    }
