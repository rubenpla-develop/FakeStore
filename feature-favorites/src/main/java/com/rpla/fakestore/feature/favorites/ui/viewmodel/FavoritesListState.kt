package com.rpla.fakestore.feature.favorites.ui.viewmodel

import com.rpla.fakestore.core.ui.model.UiListItem
import com.rpla.fakestore.core.ui.view.viewmodel.ViewState

sealed interface FavoritesListState : ViewState {
    data object InitialState : FavoritesListState
    data object LoadingState : FavoritesListState
    data class ErrorState(val message: String) : FavoritesListState
    data class FavoritesListData(val items: List<UiListItem>) : FavoritesListState
}
