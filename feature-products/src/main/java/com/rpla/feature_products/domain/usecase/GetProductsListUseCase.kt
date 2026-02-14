package com.rpla.feature_products.domain.usecase

import com.rpla.core_domain.base.BaseUseCase
import com.rpla.core_domain.entity.ResultBundle
import com.rpla.core_model.Product
import com.rpla.feature_products.domain.GetProductListRequest
import com.rpla.feature_products.domain.repository.ProductsRepository
import javax.inject.Inject

class GetProductsListUseCase @Inject constructor(private val productsRepository: ProductsRepository)
    : BaseUseCase<GetProductListRequest, List<Product>>() {
    override suspend fun run(request: GetProductListRequest): ResultBundle<List<Product>> {
        return productsRepository.getProducts()
    }
}
