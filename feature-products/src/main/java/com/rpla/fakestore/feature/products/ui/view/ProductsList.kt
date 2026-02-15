package com.rpla.fakestore.feature.products.ui.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.rpla.fakestore.core.ui.model.UiListItem
import com.rpla.fakestore.core.ui.theme.FakeStoreTheme
import com.rpla.fakestore.core.ui.view.LoadingItem
import com.rpla.fakestore.core.ui.view.ProductItem
import com.rpla.fakestore.feature.products.ui.viewmodel.ProductListIntent
import com.rpla.fakestore.feature.products.ui.viewmodel.ProductListState
import com.rpla.fakestore.feature.products.ui.viewmodel.ProductsListViewModel

@Composable
fun ProductsRoute(
    paddingValues: PaddingValues,
    onFavoriteClicked: (Int) -> Unit,
    viewModel: ProductsListViewModel = hiltViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.dispatchIntent(ProductListIntent.ProductList)
    }

    ProductList(
        uiState = state,
        paddingValues = paddingValues,
        onRetry = { viewModel.dispatchIntent(ProductListIntent.ProductList) },
        onFavoriteClicked = onFavoriteClicked,
    )
}

@Composable
fun ProductList(
    uiState: ProductListState,
    paddingValues: PaddingValues,
    onRetry: () -> Unit,
    onFavoriteClicked: (Int) -> Unit,
) {
    when (uiState) {
        ProductListState.InitialState,
        ProductListState.LoadingState,
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

        is ProductListState.ErrorState -> {
            Box(
                modifier =
                    Modifier
                        .fillMaxSize()
                        .padding(paddingValues),
                contentAlignment = Alignment.Center,
            ) {
                androidx.compose.foundation.layout.Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Text(text = uiState.message, style = MaterialTheme.typography.bodyMedium)
                    Button(onClick = onRetry) { Text("Retry") }
                }
            }
        }

        is ProductListState.ProductsListData -> {
            LazyVerticalGrid(
                modifier =
                    Modifier
                        .fillMaxSize(),
                columns = GridCells.Fixed(2),
                contentPadding = PaddingValues(bottom = 12.dp),
                verticalArrangement = Arrangement.spacedBy(2.dp),
                horizontalArrangement = Arrangement.spacedBy(2.dp),
            ) {
                items(uiState.items) { item ->
                    ProductItem(
                        product = item,
                        onFavoriteIconClicked = onFavoriteClicked,
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun Preview_ProductList_Loading() {
    FakeStoreTheme {
        ProductList(
            uiState = ProductListState.LoadingState,
            paddingValues = PaddingValues(0.dp),
            onRetry = {},
            onFavoriteClicked = {},
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun Preview_ProductList_Error() {
    FakeStoreTheme {
        ProductList(
            uiState = ProductListState.ErrorState("Network error"),
            paddingValues = PaddingValues(0.dp),
            onRetry = {},
            onFavoriteClicked = {},
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun Preview_ProductList_Content() {
    FakeStoreTheme {
        ProductList(
            uiState =
                ProductListState.ProductsListData(
                    items =
                        listOf(
                            UiListItem(
                                id = 1,
                                title = "WD 2TB Elements Portable External Hard Drive - USB 3.0",
                                price = "64.00 €",
                                category = "electronics",
                                imageUrl = "https://via.placeholder.com/300",
                                isFavorite = false,
                            ),
                            UiListItem(
                                id = 2,
                                title = "White Gold Plated Princess",
                                price = "9.99 €",
                                category = "jewelery",
                                imageUrl = "https://via.placeholder.com/300",
                                isFavorite = true,
                            ),
                        ),
                ),
            paddingValues = PaddingValues(0.dp),
            onRetry = {},
            onFavoriteClicked = {},
        )
    }
}
