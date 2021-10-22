package com.robin.mapdemo.di

import android.app.Application
import androidx.room.Room
import com.robin.mapdemo.model.AppDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
object DataBaseModule {
    @ViewModelScoped
    @Provides
    fun provideAppDatabase(application: Application): AppDatabase {
        return Room.databaseBuilder(application, AppDatabase::class.java, "ArticleDb")
            .fallbackToDestructiveMigration()
            .build()
    }

}