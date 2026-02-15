package com.rpla.fakestore.feature.favorites.ui.mapper

import com.rpla.fakestore.core.model.Product
import com.rpla.fakestore.core.ui.model.UiListItem
import java.util.Locale

fun Product.toUiListItem(forceFavorite: Boolean = false): UiListItem =
    UiListItem(
        id = id,
        title = title,
        price = String.format(Locale.US, "%.2f €", price),
        category = category,
        imageUrl = imageUrl,
        isFavorite = if (forceFavorite) true else isFavorite,
    )
