package com.rpla.feature_products.ui.viewmodel

import androidx.lifecycle.viewModelScope
import com.rpla.core.coroutines.dispatcher.IODispatcher
import com.rpla.core_ui.view.viewmodel.BaseViewModel
import com.rpla.feature_products.domain.GetProductListRequest
import com.rpla.feature_products.domain.usecase.GetProductsListUseCase
import com.rpla.feature_products.ui.mapper.toUiListItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class ProductsListViewModel @Inject constructor(
    private val getProductsListUseCase: GetProductsListUseCase,
    @param:IODispatcher private val dispatcher: CoroutineDispatcher,
) : BaseViewModel<ProductListIntent, ProductListAction, ProductListState>() {

    override fun createInitialState(): ProductListState = ProductListState.InitialState

    override fun handleAction(action: ProductListAction) {
        when (action) {
            ProductListAction.LoadProducts -> loadProducts()
        }
    }

    override fun mapIntentToAction(intent: ProductListIntent): ProductListAction {
        return when (intent) {
            ProductListIntent.ProductList -> ProductListAction.LoadProducts
        }
    }

    private fun loadProducts() {
        viewModelScope.launch {
            setState(ProductListState.LoadingState)

            val result = withContext(dispatcher) {
                  getProductsListUseCase(GetProductListRequest())
            }

            val  data = result.data

            if (data != null) {
                setState(ProductListState.ProductsListData(data.map { it.toUiListItem() }))
            } else {
                val message = result.error ?: "Unknown error"
                setState(ProductListState.ErrorState(message.toString()))
            }
        }
    }
}
