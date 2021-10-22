package com.robin.mapdemo.model

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters


@Database(
    entities = [Article::class],
    version = 1
)
@TypeConverters(value = [DateTypeConverter::class])
abstract class AppDatabase :RoomDatabase(){

    abstract fun articleDao(): ArticleDao
}