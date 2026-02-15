package com.rpla.feature.products.ui.viewmodel

import com.rpla.core.ui.view.viewmodel.ViewIntent

sealed class ProductListIntent : ViewIntent {
    data object ProductList : ProductListIntent()
}
