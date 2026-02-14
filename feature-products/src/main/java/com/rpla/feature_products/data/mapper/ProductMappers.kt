package com.rpla.feature_products.data.mapper

import com.rpla.core_model.Product
import com.rpla.feature_products.data.remote.dto.ProductDto

fun ProductDto.toModel(): Product = Product(
    id = id,
    title = title,
    price = price,
    description = description,
    category = category,
    imageUrl = image,
    isFavorite = false, // temporary until we implement favorites
)
