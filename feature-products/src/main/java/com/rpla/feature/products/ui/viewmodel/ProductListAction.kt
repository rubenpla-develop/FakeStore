package com.rpla.feature.products.ui.viewmodel

import com.rpla.core.ui.view.viewmodel.ViewAction

sealed class ProductListAction : ViewAction {
    data object LoadProducts : ProductListAction()
}
