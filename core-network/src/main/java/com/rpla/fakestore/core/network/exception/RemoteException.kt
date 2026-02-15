package com.rpla.fakestore.core.network.exception

import java.io.IOException

sealed class RemoteException(
    message: String,
) : IOException(message) {
    class ClientError(
        message: String,
    ) : RemoteException(message)

    class ServerError(
        message: String,
    ) : RemoteException(message)

    class NoNetworkError(
        message: String,
    ) : RemoteException(message)

    class GenericError(
        message: String,
    ) : RemoteException(message)
}
