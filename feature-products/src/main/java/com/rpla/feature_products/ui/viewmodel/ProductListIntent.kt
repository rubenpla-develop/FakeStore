package com.rpla.feature_products.ui.viewmodel

import com.rpla.core_ui.view.viewmodel.ViewIntent

sealed class ProductListIntent : ViewIntent {
    data object ProductList : ProductListIntent()
}
