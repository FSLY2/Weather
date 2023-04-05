package com.example.weather.di

import com.example.weather.data.repository.MainRepository
import com.example.weather.data.repository.MainRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class RepositoryModule {

    @Provides
    @Singleton
    fun providesMainRepository(mainRepositoryImpl: MainRepositoryImpl): MainRepository =
        mainRepositoryImpl
}