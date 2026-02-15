package com.rpla.fakestore.feature.products.ui.viewmodel

import androidx.lifecycle.viewModelScope
import com.rpla.fakestore.core.coroutines.dispatcher.IODispatcher
import com.rpla.fakestore.core.domain.usecase.favorites.ObserveFavoriteIdsUseCase
import com.rpla.fakestore.core.domain.usecase.favorites.ToggleFavoriteUseCase
import com.rpla.fakestore.core.model.Product
import com.rpla.fakestore.core.ui.view.viewmodel.BaseViewModel
import com.rpla.fakestore.feature.products.domain.GetProductListRequest
import com.rpla.fakestore.feature.products.domain.usecase.GetProductsListUseCase
import com.rpla.fakestore.feature.products.ui.mapper.toUiListItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class ProductsListViewModel
    @Inject
    constructor(
        private val getProductsListUseCase: GetProductsListUseCase,
        private val observeFavoriteIdsUseCase: ObserveFavoriteIdsUseCase,
        private val toggleFavoriteUseCase: ToggleFavoriteUseCase,
        @IODispatcher private val dispatcher: CoroutineDispatcher,
    ) : BaseViewModel<ProductListIntent, ProductListAction, ProductListState>() {
        private var favoriteIds: Set<Int> = emptySet()
        private var productsCache: List<Product> = emptyList()
        private var observeFavoritesJob: Job? = null
        private val pendingFavoriteToggles = mutableSetOf<Int>()

        override fun createInitialState(): ProductListState = ProductListState.InitialState

        override fun handleAction(action: ProductListAction) {
            when (action) {
                ProductListAction.LoadProducts -> {
                    observeFavoriteIdsIfNeeded()
                    loadProducts()
                }
                is ProductListAction.ToggleFavorite -> toggleFavorite(action.productId)
            }
        }

        override fun mapIntentToAction(intent: ProductListIntent): ProductListAction =
            when (intent) {
                ProductListIntent.ProductList -> ProductListAction.LoadProducts
                is ProductListIntent.ToggleFavorite -> ProductListAction.ToggleFavorite(intent.productId)
            }

        private fun observeFavoriteIdsIfNeeded() {
            if (observeFavoritesJob != null) return

            observeFavoritesJob =
                viewModelScope.launch {
                    observeFavoriteIdsUseCase()
                        .collectLatest { ids ->
                            favoriteIds = ids
                            refreshFavoriteFlagsInUi()
                        }
                }
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
                    productsCache = data

                    val uiItems =
                        data.map { product ->
                            product.toUiListItem(isFavoriteOverride = favoriteIds.contains(product.id))
                        }

                    setState(ProductListState.ProductsListData(uiItems))
                } else {
                    val message = result.error ?: "Unknown error"
                    setState(ProductListState.ErrorState(message.toString()))
                }
            }
        }

        private fun toggleFavorite(productId: Int) {
            if (!pendingFavoriteToggles.add(productId)) return

            val current = state.value
            if (current !is ProductListState.ProductsListData) {
                pendingFavoriteToggles.remove(productId)
                return
            }

            val index = current.items.indexOfFirst { it.id == productId }
            if (index == -1) {
                pendingFavoriteToggles.remove(productId)
                return
            }

            val before = current.items[index]
            val after = before.copy(isFavorite = !before.isFavorite)

            // Refresh immediatly
            val optimisticItems = current.items.toMutableList().apply { set(index, after) }
            setState(ProductListState.ProductsListData(optimisticItems))

            // executes toggle in background(room process)
            val product = productsCache.firstOrNull { it.id == productId }

            if (product == null) {
                revertFavoriteUi(productId, before.isFavorite)
                pendingFavoriteToggles.remove(productId)
                return
            }

            viewModelScope.launch {
                val result = withContext(dispatcher) { toggleFavoriteUseCase(product) }

                pendingFavoriteToggles.remove(productId)

                // revert if fail
                if (result.error != null) {
                    revertFavoriteUi(productId, before.isFavorite)
                    // todo snackbar error??
                }
            }
        }

        private fun revertFavoriteUi(
            productId: Int,
            isFavorite: Boolean,
        ) {
            val current = state.value
            if (current is ProductListState.ProductsListData) {
                val index = current.items.indexOfFirst { it.id == productId }
                if (index != -1) {
                    val reverted = current.items[index].copy(isFavorite = isFavorite)
                    val newItems = current.items.toMutableList().apply { set(index, reverted) }
                    setState(ProductListState.ProductsListData(newItems))
                }
            }
        }

        private fun refreshFavoriteFlagsInUi() {
            val current = state.value
            if (current is ProductListState.ProductsListData) {
                val updated =
                    current.items.map { item ->
                        if (pendingFavoriteToggles.contains(item.id)) {
                            item
                        } else {
                            item.copy(isFavorite = favoriteIds.contains(item.id))
                        }
                    }

                setState(ProductListState.ProductsListData(updated))
            }
        }
    }
