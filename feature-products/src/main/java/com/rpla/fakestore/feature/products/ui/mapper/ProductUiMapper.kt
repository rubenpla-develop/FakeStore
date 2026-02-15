package com.rpla.fakestore.feature.products.ui.mapper

import com.rpla.fakestore.core.model.Product
import com.rpla.fakestore.core.ui.model.UiListItem
import java.util.Locale

fun Product.toUiListItem(isFavoriteOverride: Boolean? = null): UiListItem =
    UiListItem(
        id = id,
        title = title,
        price = String.format(Locale.US, "%.2f €", price),
        category = category,
        imageUrl = imageUrl,
        isFavorite = isFavoriteOverride ?: isFavorite,
    )
