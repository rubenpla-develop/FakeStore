package com.rpla.fakestore.core.network.retrofit

import com.rpla.fakestore.core.network.exception.RemoteException
import retrofit2.HttpException
import java.io.IOException
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

suspend inline fun <T> safeApiCall(crossinline block: suspend () -> T): T =
    try {
        block()
    } catch (e: HttpException) {
        val code = e.code()
        throw if (code in 400..499) {
            RemoteException.ClientError(e.message())
        } else {
            RemoteException.ServerError(e.message())
        }
    } catch (e: UnknownHostException) {
        throw RemoteException.NoNetworkError(e.message.orEmpty())
    } catch (e: SocketTimeoutException) {
        throw RemoteException.NoNetworkError(e.message.orEmpty())
    } catch (e: ConnectException) {
        throw RemoteException.NoNetworkError(e.message.orEmpty())
    } catch (e: IOException) {
        throw RemoteException.NoNetworkError(e.message.orEmpty())
    } catch (t: Throwable) {
        throw RemoteException.GenericError(t.message.orEmpty())
    }
