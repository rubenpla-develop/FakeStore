package com.rpla.fakestore.core.data.favorites.common

import com.rpla.fakestore.core.domain.entity.ErrorResult
import com.rpla.fakestore.core.domain.entity.ResultBundle

internal fun <T> Result<T>.toResultBundle(errorMapper: (Throwable) -> ErrorResult = { ErrorResult.GenericError }): ResultBundle<T> =
    fold(
        onSuccess = { successValue ->
            ResultBundle(
                data = successValue,
                error = null,
            )
        },
        onFailure = { throwable ->
            ResultBundle(
                data = null,
                error = errorMapper(throwable),
            )
        },
    )
