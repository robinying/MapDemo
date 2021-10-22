package com.robin.mapdemo.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import java.util.*

@Entity
data class Article(
    @PrimaryKey
    val id: Long,
    val author: String,
    val title: String,
    val desc: String,
    val url: String,
    val likes: Int,
    @ColumnInfo(name = "updateDate")
    @TypeConverters(DateTypeConverter::class)
    val date: Date,
)