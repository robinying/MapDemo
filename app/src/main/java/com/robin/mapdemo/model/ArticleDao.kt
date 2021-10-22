package com.robin.mapdemo.model

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface ArticleDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveArticles(vararg articles: Article)

    @Query("SELECT * FROM Article")
    fun getArticles(): Flow<List<Article>>
}