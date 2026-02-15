package com.rpla.fakestore.feature.products.ui.viewmodel

import com.rpla.fakestore.core.ui.model.UiListItem
import com.rpla.fakestore.core.ui.view.viewmodel.ViewState

sealed class ProductListState : ViewState {
    data object InitialState : ProductListState()

    data object LoadingState : ProductListState()

    data class ProductsListData(
        val items: List<UiListItem>,
    ) : ProductListState()

    data class ErrorState(
        val message: String,
    ) : ProductListState()
}
