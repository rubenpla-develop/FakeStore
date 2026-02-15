package com.rpla.fakestore.feature.favorites.domain.usecase

import com.rpla.fakestore.core.domain.base.BaseUseCase
import com.rpla.fakestore.core.domain.entity.ResultBundle
import com.rpla.fakestore.core.domain.repository.FavoritesRepository
import javax.inject.Inject

class RemoveFavoriteUseCase
    @Inject
    constructor(
        private val favoritesRepository: FavoritesRepository,
    ) : BaseUseCase<Int, Unit>() {
        override suspend fun run(request: Int): ResultBundle<Unit> = favoritesRepository.removeFavorite(request)
    }
