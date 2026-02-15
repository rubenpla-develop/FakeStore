package com.rpla.fakestore.feature.favorites.ui.viewmodel

import com.rpla.fakestore.core.ui.view.viewmodel.ViewIntent

sealed interface FavoritesListIntent : ViewIntent {
    data object FavoritesList : FavoritesListIntent

    data class RemoveFavorite(
        val productId: Int,
    ) : FavoritesListIntent
}
