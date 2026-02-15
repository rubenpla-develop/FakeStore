package com.rpla.feature.products.ui.mapper

import com.rpla.core.model.Product
import com.rpla.core.ui.model.UiListItem
import java.util.Locale

fun Product.toUiListItem(): UiListItem =
    UiListItem(
        id = id,
        title = title,
        price = String.format(Locale.US, "%.2f €", price),
        category = category,
        imageUrl = imageUrl,
        isFavorite = isFavorite,
    )
