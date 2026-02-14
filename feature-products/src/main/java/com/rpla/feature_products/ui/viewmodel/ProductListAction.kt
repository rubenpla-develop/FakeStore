package com.rpla.feature_products.ui.viewmodel

import com.rpla.core_ui.view.viewmodel.ViewAction

sealed class ProductListAction : ViewAction {
    data object LoadProducts : ProductListAction()
}
