package com.rpla.fakestore.core.di

import com.rpla.fakestore.core.coroutines.dispatcher.IODispatcher
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.Dispatchers
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppModule {
    @Provides
    @Singleton
    @IODispatcher
    fun providesDispatcher() = Dispatchers.IO
}
