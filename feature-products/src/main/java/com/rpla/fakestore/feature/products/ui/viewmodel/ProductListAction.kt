package com.rpla.fakestore.feature.products.ui.viewmodel

import com.rpla.fakestore.core.ui.view.viewmodel.ViewAction

sealed class ProductListAction : ViewAction {
    data object LoadProducts : ProductListAction()

    data class ToggleFavorite(
        val productId: Int,
    ) : ProductListAction()
}
