package com.rpla.fakestore.feature.products.ui.viewmodel

import androidx.lifecycle.viewModelScope
import com.rpla.fakestore.core.coroutines.dispatcher.IODispatcher
import com.rpla.fakestore.core.ui.view.viewmodel.BaseViewModel
import com.rpla.fakestore.feature.products.domain.GetProductListRequest
import com.rpla.fakestore.feature.products.domain.usecase.GetProductsListUseCase
import com.rpla.fakestore.feature.products.ui.mapper.toUiListItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class ProductsListViewModel
    @Inject
    constructor(
        private val getProductsListUseCase: GetProductsListUseCase,
        @IODispatcher private val dispatcher: CoroutineDispatcher,
    ) : BaseViewModel<ProductListIntent, ProductListAction, ProductListState>() {
        override fun createInitialState(): ProductListState = ProductListState.InitialState

        override fun handleAction(action: ProductListAction) {
            when (action) {
                ProductListAction.LoadProducts -> loadProducts()
            }
        }

        override fun mapIntentToAction(intent: ProductListIntent): ProductListAction =
            when (intent) {
                ProductListIntent.ProductList -> ProductListAction.LoadProducts
            }

        private fun loadProducts() {
            viewModelScope.launch {
                setState(ProductListState.LoadingState)

                val result =
                    withContext(dispatcher) {
                        getProductsListUseCase(GetProductListRequest())
                    }

                val data = result.data

                if (data != null) {
                    setState(ProductListState.ProductsListData(data.map { it.toUiListItem() }))
                } else {
                    val message = result.error ?: "Unknown error"
                    setState(ProductListState.ErrorState(message.toString()))
                }
            }
        }
    }
