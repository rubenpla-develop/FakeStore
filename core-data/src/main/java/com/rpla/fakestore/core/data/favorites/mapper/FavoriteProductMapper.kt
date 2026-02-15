package com.rpla.fakestore.core.data.favorites.mapper

import com.rpla.fakestore.core.database.entity.FavoriteProductEntity
import com.rpla.fakestore.core.model.Product

internal fun FavoriteProductEntity.toDomain(): Product =
    Product(
        id = id,
        title = title,
        price = price,
        description = description,
        category = category,
        imageUrl = imageUrl,
    )

internal fun Product.toEntity(): FavoriteProductEntity =
    FavoriteProductEntity(
        id = id,
        title = title,
        price = price,
        description = description,
        category = category,
        imageUrl = imageUrl,
    )
