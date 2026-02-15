package com.rpla.fakestore.feature.products.data.mapper

import com.rpla.fakestore.core.domain.entity.ErrorResult
import com.rpla.fakestore.core.network.exception.RemoteException
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test

class ErrorMapperTest {
    private val mapper = ErrorMapper()

    @Test
    fun `mapRemoteErrorRecord maps ClientError to ErrorResult ClientError`() {
        val result = mapper.mapRemoteErrorRecord<Any>(RemoteException.ClientError("4xx"))
        result.data shouldBe null
        result.error shouldBe ErrorResult.ClientError
    }

    @Test
    fun `mapRemoteErrorRecord maps ServerError to ErrorResult ServerError`() {
        val result = mapper.mapRemoteErrorRecord<Any>(RemoteException.ServerError("5xx"))
        result.data shouldBe null
        result.error shouldBe ErrorResult.ServerError
    }

    @Test
    fun `mapRemoteErrorRecord maps NoNetworkError to ErrorResult NetworkError`() {
        val result = mapper.mapRemoteErrorRecord<Any>(RemoteException.NoNetworkError("no network"))
        result.data shouldBe null
        result.error shouldBe ErrorResult.NetworkError
    }

    @Test
    fun `mapRemoteErrorRecord maps GenericError to ErrorResult GenericError`() {
        val result = mapper.mapRemoteErrorRecord<Any>(RemoteException.GenericError("generic"))
        result.data shouldBe null
        result.error shouldBe ErrorResult.GenericError
    }

    @Test
    fun `mapUnexpectedThrowableErrorResult always maps to GenericError`() {
        val result = mapper.mapUnexpectedThrowableErrorResult<Any>(IllegalStateException("boom"))
        result.data shouldBe null
        result.error shouldBe ErrorResult.GenericError
    }
}
