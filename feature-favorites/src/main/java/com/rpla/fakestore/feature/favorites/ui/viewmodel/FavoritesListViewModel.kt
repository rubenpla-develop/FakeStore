package com.rpla.fakestore.feature.favorites.ui.viewmodel

import androidx.lifecycle.viewModelScope
import com.rpla.fakestore.core.coroutines.dispatcher.IODispatcher
import com.rpla.fakestore.core.domain.usecase.favorites.ObserveFavoritesUseCase
import com.rpla.fakestore.core.domain.usecase.favorites.RemoveFavoriteUseCase
import com.rpla.fakestore.core.ui.view.viewmodel.BaseViewModel
import com.rpla.fakestore.feature.favorites.ui.mapper.toUiListItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class FavoritesListViewModel
    @Inject
    constructor(
        private val observeFavoritesUseCase: ObserveFavoritesUseCase,
        private val removeFavoriteUseCase: RemoveFavoriteUseCase,
        @IODispatcher private val dispatcher: CoroutineDispatcher,
    ) : BaseViewModel<FavoritesListIntent, FavoritesListAction, FavoritesListState>() {
        private var observeJob: Job? = null

        override fun createInitialState(): FavoritesListState = FavoritesListState.InitialState

        override fun mapIntentToAction(intent: FavoritesListIntent): FavoritesListAction =
            when (intent) {
                FavoritesListIntent.FavoritesList -> FavoritesListAction.ObserveFavorites
                is FavoritesListIntent.RemoveFavorite -> FavoritesListAction.RemoveFavorite(intent.productId)
            }

        override fun handleAction(action: FavoritesListAction) {
            when (action) {
                FavoritesListAction.ObserveFavorites -> observeFavorites()
                is FavoritesListAction.RemoveFavorite -> removeFavorite(action.productId)
            }
        }

        private fun observeFavorites() {
            // Avoid duplicate collector & refreshes/recomposes
            if (observeJob != null) return

            setState(FavoritesListState.LoadingState)

            observeJob =
                viewModelScope.launch {
                    observeFavoritesUseCase()
                        .catch { throwable ->
                            setState(FavoritesListState.ErrorState(throwable.message ?: "Unknown error"))
                        }.collect { products ->
                            val items = products.map { it.toUiListItem(forceFavorite = true) }
                            setState(FavoritesListState.FavoritesListData(items))
                        }
                }
        }

        private fun removeFavorite(productId: Int) {
            viewModelScope.launch {
                withContext(dispatcher) {
                    // remove-only: en Favorites siempre es "unpin"
                    removeFavoriteUseCase(productId)
                }
                // flow will observe changes and refresh list
            }
        }
    }
