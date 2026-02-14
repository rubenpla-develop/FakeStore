package com.rpla.core_domain.entity

sealed class ErrorResult {
    data object ClientError : ErrorResult()
    data object ServerError : ErrorResult()
    data object NetworkError : ErrorResult()
    data object GenericError : ErrorResult()
}
