package com.robin.mapdemo.di

import com.robin.mapdemo.data.WeatherRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped
import javax.inject.Singleton

@Module
@InstallIn(ViewModelComponent::class)
object HttpModule {
    @ViewModelScoped
    @Provides
    fun provideWeatherRepository(): WeatherRepository {
        return WeatherRepository()
    }
}