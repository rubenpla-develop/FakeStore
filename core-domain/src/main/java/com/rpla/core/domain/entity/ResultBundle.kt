package com.rpla.core.domain.entity

data class ResultBundle<out R>(
    val data: R?,
    val error: ErrorResult?,
)
