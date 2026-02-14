package com.rpla.feature_products.ui.mapper

import com.rpla.core_model.Product
import com.rpla.core_ui.model.UiListItem
import java.util.Locale

fun Product.toUiListItem(): UiListItem = UiListItem(
    id = id,
    title = title,
    price = String.format(Locale.US, "%.2f €", price),
    category = category,
    imageUrl = imageUrl,
    isFavorite = isFavorite,
)
