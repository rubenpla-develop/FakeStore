package com.rpla.feature_products.di

import com.rpla.feature_products.data.ProductsRepositoryImpl
import com.rpla.feature_products.data.remote.ProductsService
import com.rpla.feature_products.domain.repository.ProductsRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ProductsModule {

    @Provides
    @Singleton
    fun provideProductsService(retrofit: Retrofit): ProductsService =
        retrofit.create(ProductsService::class.java)

    @Module
    @InstallIn(SingletonComponent::class)
    abstract class ProductsBindingsModule {

        @Binds
        @Singleton
        abstract fun bindProductsRepository(
            impl: ProductsRepositoryImpl,
        ): ProductsRepository
    }
}
