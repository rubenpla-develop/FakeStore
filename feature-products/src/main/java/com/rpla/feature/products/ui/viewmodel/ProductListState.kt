package com.rpla.feature.products.ui.viewmodel

import com.rpla.core.ui.model.UiListItem
import com.rpla.core.ui.view.viewmodel.ViewState

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
