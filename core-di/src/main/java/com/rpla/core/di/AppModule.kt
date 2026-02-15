package com.rpla.core.di

import com.rpla.core.coroutines.dispatcher.IODispatcher
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
