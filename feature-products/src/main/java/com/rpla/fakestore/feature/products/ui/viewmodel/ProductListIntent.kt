package com.rpla.fakestore.feature.products.ui.viewmodel

import com.rpla.fakestore.core.ui.view.viewmodel.ViewIntent

sealed class ProductListIntent : ViewIntent {
    data object ProductList : ProductListIntent()

    data class ToggleFavorite(
        val productId: Int,
    ) : ProductListIntent()
}
