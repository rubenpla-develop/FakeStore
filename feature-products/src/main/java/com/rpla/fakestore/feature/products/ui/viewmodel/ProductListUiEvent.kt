package com.rpla.fakestore.feature.products.ui.viewmodel

sealed interface ProductListUiEvent {
    data object ShowFavoriteToggleError : ProductListUiEvent
}
