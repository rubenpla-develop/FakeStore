package com.rpla.fakestore.feature.favorites.ui.viewmodel

sealed interface FavoritesListUiEvent {
    data object ShowRemoveFavoriteError : FavoritesListUiEvent
}
