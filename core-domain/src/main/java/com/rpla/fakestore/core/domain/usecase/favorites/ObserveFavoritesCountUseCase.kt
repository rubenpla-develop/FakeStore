package com.rpla.fakestore.core.domain.usecase.favorites

import com.rpla.fakestore.core.domain.repository.FavoritesRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class ObserveFavoritesCountUseCase
    @Inject
    constructor(
        private val favoritesRepository: FavoritesRepository,
    ) {
        operator fun invoke(): Flow<Int> = favoritesRepository.observeFavoriteIds().map { it.size }
    }
