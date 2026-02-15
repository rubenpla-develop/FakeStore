package com.rpla.fakestore.core.domain.usecase.favorites

import com.rpla.fakestore.core.domain.repository.FavoritesRepository
import com.rpla.fakestore.core.model.Product
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ObserveFavoritesUseCase
    @Inject
    constructor(
        private val favoritesRepository: FavoritesRepository,
    ) {
        operator fun invoke(): Flow<List<Product>> = favoritesRepository.observeFavorites()
    }
