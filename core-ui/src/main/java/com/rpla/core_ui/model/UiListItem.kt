package com.rpla.core_ui.model

data class UiListItem(
    val id: Int,
    val title: String,
    val price: String,
    val category: String,
    val imageUrl: String,
    val isFavorite: Boolean,
)
