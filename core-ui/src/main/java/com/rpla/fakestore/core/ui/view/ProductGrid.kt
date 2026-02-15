package com.rpla.fakestore.core.ui.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import com.rpla.fakestore.core.ui.model.UiListItem
import com.rpla.fakestore.core.ui.view.testtags.TestTags.PRODUCT_GRID

@Composable
fun ProductGrid(
    items: List<UiListItem>,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(bottom = 12.dp),
    columns: Int = 2,
    favoriteIconMode: FavoriteIconMode,
    onItemClick: (Int) -> Unit = {},
    onFavoriteClick: (Int) -> Unit,
) {
    LazyVerticalGrid(
        modifier = modifier.testTag(PRODUCT_GRID),
        columns = GridCells.Fixed(columns),
        contentPadding = contentPadding,
        verticalArrangement = Arrangement.spacedBy(2.dp),
        horizontalArrangement = Arrangement.spacedBy(2.dp),
    ) {
        items(items) { item ->
            ProductItem(
                product = item,
                favoriteIconMode = favoriteIconMode,
                onItemClick = { onItemClick(item.id) },
                onFavoriteIconClicked = onFavoriteClick,
            )
        }
    }
}
