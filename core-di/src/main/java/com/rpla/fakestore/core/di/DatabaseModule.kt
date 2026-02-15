package com.rpla.fakestore.core.di

import android.content.Context
import androidx.room.Room
import com.rpla.fakestore.core.database.FakeStoreDatabase
import com.rpla.fakestore.core.database.dao.FavoriteProductDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    private const val DB_NAME = "fakestore.db"

    @Provides
    @Singleton
    fun providerFakeStoreDatabase(
        @ApplicationContext context: Context,
    ): FakeStoreDatabase =
        Room
            .databaseBuilder(
                context,
                FakeStoreDatabase::class.java,
                DB_NAME,
            ).fallbackToDestructiveMigration()
            .build()

    @Provides
    fun provideFavoriteProductDao(database: FakeStoreDatabase): FavoriteProductDao = database.favoriteProductDao
}
