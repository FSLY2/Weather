package com.example.weather.di

import com.example.weather.data.repository.ApiProvider
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
class ApiProviderModule {

    @Provides
    @Singleton
    fun provideApiService(): ApiProvider {
        return ApiProvider()
    }
}