package com.rpla.fakestore.core.di

import com.rpla.fakestore.core.data.favorites.repository.FavoritesRepositoryImpl
import com.rpla.fakestore.core.domain.repository.FavoritesRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import jakarta.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class DataModule {
    @Binds
    @Singleton
    abstract fun bindFavoritesRepository(impl: FavoritesRepositoryImpl): FavoritesRepository
}
