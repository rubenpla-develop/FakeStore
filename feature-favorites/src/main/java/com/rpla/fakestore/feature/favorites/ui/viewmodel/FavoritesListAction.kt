package com.rpla.fakestore.feature.favorites.ui.viewmodel

import com.rpla.fakestore.core.ui.view.viewmodel.ViewAction

sealed interface FavoritesListAction : ViewAction {
    data object ObserveFavorites : FavoritesListAction

    data class RemoveFavorite(
        val productId: Int,
    ) : FavoritesListAction
}
