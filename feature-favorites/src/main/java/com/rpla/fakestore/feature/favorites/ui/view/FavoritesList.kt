package com.rpla.fakestore.feature.favorites.ui.view

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.rpla.fakestore.core.ui.view.FavoriteIconMode
import com.rpla.fakestore.core.ui.view.LoadingItem
import com.rpla.fakestore.core.ui.view.ProductGrid
import com.rpla.fakestore.feature.favorites.ui.viewmodel.FavoritesListIntent
import com.rpla.fakestore.feature.favorites.ui.viewmodel.FavoritesListState
import com.rpla.fakestore.feature.favorites.ui.viewmodel.FavoritesListViewModel

@Composable
fun FavoritesRoute(
    paddingValues: PaddingValues,
    viewModel: FavoritesListViewModel = hiltViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.dispatchIntent(FavoritesListIntent.FavoritesList)
    }

    FavoritesList(
        uiState = state,
        paddingValues = paddingValues,
        onUnpin = { id -> viewModel.dispatchIntent(FavoritesListIntent.RemoveFavorite(id)) },
    )
}

@Composable
fun FavoritesList(
    uiState: FavoritesListState,
    paddingValues: PaddingValues,
    onUnpin: (Int) -> Unit,
) {
    when (uiState) {
        FavoritesListState.InitialState,
        FavoritesListState.LoadingState,
        -> {
            Box(
                modifier =
                    Modifier
                        .fillMaxSize()
                        .padding(paddingValues),
                contentAlignment = Alignment.Center,
            ) {
                LoadingItem()
            }
        }

        is FavoritesListState.ErrorState -> {
            Box(
                modifier =
                    Modifier
                        .fillMaxSize()
                        .padding(paddingValues),
                contentAlignment = Alignment.Center,
            ) {
                Text(text = uiState.message, style = MaterialTheme.typography.bodyMedium)
            }
        }

        is FavoritesListState.FavoritesListData -> {
            if (uiState.items.isEmpty()) {
                Box(
                    modifier =
                        Modifier
                            .fillMaxSize()
                            .padding(paddingValues),
                    contentAlignment = Alignment.Center,
                ) {
                    Text("No favorites yet", style = MaterialTheme.typography.bodyMedium)
                }
                return
            }

            ProductGrid(
                items = uiState.items,
                modifier = Modifier.fillMaxSize(),
                favoriteIconMode = FavoriteIconMode.RemoveOnly,
                onItemClick = {},
                onFavoriteClick = onUnpin,
            )
        }
    }
}
