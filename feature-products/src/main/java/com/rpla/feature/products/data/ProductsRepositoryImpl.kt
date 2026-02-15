package com.rpla.feature.products.data

import com.rpla.core.domain.entity.ResultBundle
import com.rpla.core.model.Product
import com.rpla.core.network.exception.RemoteException
import com.rpla.core.network.retrofit.safeApiCall
import com.rpla.feature.products.data.mapper.ErrorMapper
import com.rpla.feature.products.data.mapper.toModel
import com.rpla.feature.products.data.remote.ProductsService
import com.rpla.feature.products.domain.repository.ProductsRepository
import javax.inject.Inject

class ProductsRepositoryImpl
    @Inject
    constructor(
        private val productsService: ProductsService,
        private val errorMapper: ErrorMapper,
    ) : ProductsRepository {
        override suspend fun getProducts(): ResultBundle<List<Product>> =
            try {
                val products = safeApiCall { productsService.getProducts() }
                ResultBundle(data = products.map { it.toModel() }, error = null)
            } catch (e: RemoteException) {
                errorMapper.mapRemoteErrorRecord(e)
            } catch (t: Throwable) {
                errorMapper.mapUnexpectedThrowableErrorResult(t)
            }
    }
