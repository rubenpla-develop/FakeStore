package com.rpla.core_domain.entity

data class ResultBundle<out R>(
    val data: R?,
    val error: ErrorResult?,
)