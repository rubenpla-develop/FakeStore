package com.rpla.feature_products.data.mapper

import com.rpla.core_domain.entity.ErrorResult
import com.rpla.core_domain.entity.ResultBundle
import com.rpla.core_network.exception.RemoteException
import javax.inject.Inject

class ErrorMapper @Inject constructor() {

    fun <T> mapRemoteErrorRecord(e: RemoteException): ResultBundle<T> {
        val error: ErrorResult = when (e) {
            is RemoteException.ClientError -> ErrorResult.ClientError
            is RemoteException.ServerError -> ErrorResult.ServerError
            is RemoteException.NoNetworkError -> ErrorResult.NetworkError
            else -> ErrorResult.GenericError
        }

        return ResultBundle(null, error)
    }

    fun <T> mapUnexpectedThrowableErrorResult(t: Throwable): ResultBundle<T> {
        val error: ErrorResult = ErrorResult.GenericError
        return ResultBundle(null, error)
    }
}
