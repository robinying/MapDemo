package com.robin.mapdemo.model

import androidx.room.TypeConverter
import java.text.SimpleDateFormat
import java.util.*

class DateTypeConverter {


    @TypeConverter
    fun fromString(value: String?): Date? {
        return SimpleDateFormat("yyyy-MM-dd HH-mm-ss", Locale.US).parse(value)
    }

    @TypeConverter
    fun dateToString(date: Date?): String? {
        return SimpleDateFormat("yyyy-MM-dd HH-mm-ss", Locale.US).format(date)
    }

}