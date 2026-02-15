package com.rpla.fakestore.core.domain.repository

import com.rpla.fakestore.core.domain.entity.ResultBundle
import com.rpla.fakestore.core.model.Product
import kotlinx.coroutines.flow.Flow

interface FavoritesRepository {
    // Favorites screen
    fun observeFavorites(): Flow<List<Product>>

    // Products list screen
    fun observeFavoriteIds(): Flow<Set<Int>>

    suspend fun addFavorite(product: Product): ResultBundle<Unit>

    suspend fun removeFavorite(productId: Int): ResultBundle<Unit>

    suspend fun toggleFavorite(product: Product): ResultBundle<Unit>
}
