package com.rpla.fakestore.core.domain.usecase.favorites

import com.rpla.fakestore.core.domain.base.BaseUseCase
import com.rpla.fakestore.core.domain.entity.ResultBundle
import com.rpla.fakestore.core.domain.repository.FavoritesRepository
import com.rpla.fakestore.core.model.Product
import javax.inject.Inject

class AddFavoritesUseCase
    @Inject
    constructor(
        private val favoritesRepository: FavoritesRepository,
    ) : BaseUseCase<Product, Unit>() {
        override suspend fun run(request: Product): ResultBundle<Unit> = favoritesRepository.addFavorite(request)
    }
