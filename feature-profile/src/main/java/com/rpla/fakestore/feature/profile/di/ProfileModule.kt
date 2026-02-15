package com.rpla.fakestore.feature.profile.di

import com.rpla.fakestore.feature.profile.data.ProfileRepositoryImpl
import com.rpla.fakestore.feature.profile.data.remote.ProfileService
import com.rpla.fakestore.feature.profile.domain.repository.ProfileRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ProfileModule {
    @Provides
    @Singleton
    fun provideProfileService(retrofit: Retrofit): ProfileService = retrofit.create(ProfileService::class.java)

    @Module
    @InstallIn(SingletonComponent::class)
    abstract class ProfileBindingsModule {
        @Binds
        @Singleton
        abstract fun bindProfileRepository(impl: ProfileRepositoryImpl): ProfileRepository
    }
}
