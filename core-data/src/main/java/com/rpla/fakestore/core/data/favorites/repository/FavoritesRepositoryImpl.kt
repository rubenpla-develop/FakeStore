package com.rpla.fakestore.core.data.favorites.repository

import com.rpla.fakestore.core.data.favorites.common.toResultBundle
import com.rpla.fakestore.core.data.favorites.mapper.toDomain
import com.rpla.fakestore.core.data.favorites.mapper.toEntity
import com.rpla.fakestore.core.database.dao.FavoriteProductDao
import com.rpla.fakestore.core.domain.entity.ErrorResult
import com.rpla.fakestore.core.domain.entity.ResultBundle
import com.rpla.fakestore.core.domain.repository.FavoritesRepository
import com.rpla.fakestore.core.model.Product
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class FavoritesRepositoryImpl
    @Inject
    constructor(
        private val favoriteProductDao: FavoriteProductDao,
    ) : FavoritesRepository {
        override fun observeFavorites(): Flow<List<Product>> =
            favoriteProductDao
                .observeFavorites()
                .map { entities ->
                    entities.map { it.toDomain() }
                }

        override fun observeFavoriteIds(): Flow<Set<Int>> =
            favoriteProductDao
                .observeFavoriteIds()
                .map { ids -> ids.toSet() }

        override suspend fun addFavorite(product: Product): ResultBundle<Unit> =
            runCatching {
                favoriteProductDao.upsertFavorite(product.toEntity())
            }.toResultBundle { ErrorResult.GenericError }

        override suspend fun removeFavorite(productId: Int): ResultBundle<Unit> =
            runCatching {
                favoriteProductDao.deleteFavoriteById(productId)
                Unit
            }.toResultBundle { ErrorResult.GenericError }

        override suspend fun toggleFavorite(product: Product): ResultBundle<Unit> =
            runCatching {
                favoriteProductDao.toggleFavorite(product.toEntity())
                Unit
            }.toResultBundle { ErrorResult.GenericError }
    }
