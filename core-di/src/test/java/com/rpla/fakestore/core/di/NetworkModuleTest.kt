package com.rpla.fakestore.core.di

import okhttp3.logging.HttpLoggingInterceptor
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class NetworkModuleTest {
    @Test
    fun `logging level is BODY in debug builds`() {
        assertEquals(HttpLoggingInterceptor.Level.BODY, NetworkModule.loggingLevel(true))
    }

    @Test
    fun `logging level is NONE in release builds`() {
        assertEquals(HttpLoggingInterceptor.Level.NONE, NetworkModule.loggingLevel(false))
    }
}
