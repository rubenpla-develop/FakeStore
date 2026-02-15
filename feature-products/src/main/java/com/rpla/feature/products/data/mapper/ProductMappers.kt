package com.rpla.feature.products.data.mapper

import com.rpla.core.model.Product
import com.rpla.feature.products.data.remote.dto.ProductDto

fun ProductDto.toModel(): Product =
    Product(
        id = id,
        title = title,
        price = price,
        description = description,
        category = category,
        imageUrl = image,
        isFavorite = false, // temporary until we implement favorites
    )
