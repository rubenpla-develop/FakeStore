package com.rpla.fakestore.core.network.mapper

import com.rpla.fakestore.core.domain.entity.ErrorResult
import com.rpla.fakestore.core.domain.entity.ResultBundle
import com.rpla.fakestore.core.network.exception.RemoteException
import javax.inject.Inject

class ErrorMapper
    @Inject
    constructor() {
        fun <T> mapRemoteErrorRecord(e: RemoteException): ResultBundle<T> {
            val error: ErrorResult =
                when (e) {
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
