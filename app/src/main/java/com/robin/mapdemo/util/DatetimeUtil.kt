package com.robin.mapdemo.util

import android.annotation.SuppressLint
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

object DatetimeUtil {

    val SIMPLE_DATE_PATTERN = "yyyyMMdd"
    val DATE_PATTERN_YEAR_MONTH = "yyyy-MM"
    val DATE_PATTERN = "yyyy-MM-dd"
    var DATE_PATTERN_SS = "yyyy-MM-dd HH:mm:ss"
    var DATE_PATTERN_MM = "yyyy-MM-dd HH:mm"
    var DATE_PATTERN_HOUR_MM = "HH:mm"
    val DATA_CN_PATTERN = "yyyy年MM月dd日 HH:mm"

    /**
     * 获取现在时刻
     */
    val now: Date
        get() = Date(Date().time)

    /**
     * 获取现在时刻
     */
    val nows: Date
        get() = formatDate(DATE_PATTERN, now)

    /**
     * 获取现在中文时刻
     */
    val nowCN: Date
        get() = formatDate(DATA_CN_PATTERN, now)


    /**
     * Date to Strin
     */
    @SuppressLint("SimpleDateFormat")
    fun formatDate(date: Date?, formatStyle: String): String {
        return if (date != null) {
            val sdf = SimpleDateFormat(formatStyle)
            sdf.format(date)
        } else {
            ""
        }

    }

    /**
     * Date to String
     */
    @SuppressLint("SimpleDateFormat")
    fun formatDate(date: Long, formatStyle: String): String {
        val sdf = SimpleDateFormat(formatStyle)
        return sdf.format(Date(date))

    }

    fun formatDate(formatStyle: String, formatStr: String): Date {
        val format = SimpleDateFormat(formatStyle, Locale.CHINA)
        return try {
            val date = Date()
            date.time = format.parse(formatStr).time
            date
        } catch (e: Exception) {
            println(e.message)
            nows
        }
    }

    fun formatDateCN(formatStyle: String, timStamp: Long?): String {
        val sdf = SimpleDateFormat(formatStyle, Locale.CHINA)
        return try {
            sdf.format(timStamp)
        } catch (e: Exception) {
            println(e.message)
            sdf.format(nowCN)
        }
    }

    /**
     * Date to Date
     */
    @SuppressLint("SimpleDateFormat")
    fun formatDate(formatStyle: String, date: Date?): Date {
        if (date != null) {
            val sdf = SimpleDateFormat(formatStyle)
            val formatDate = sdf.format(date)
            try {
                return sdf.parse(formatDate)
            } catch (e: ParseException) {
                e.printStackTrace()
                return Date()
            }

        } else {
            return Date()
        }
    }

    /**
     * 将时间戳转换为时间
     */
    fun stampToDate(s: String): Date {
        val lt = s.toLong()
        return Date(lt)
    }

    /**
     * 获得指定时间的日期
     */
    fun getCustomTime(dateStr: String): Date {
        return formatDate(DATE_PATTERN, dateStr)
    }

    /**
     * 获得指定时间中文
     */
    fun getCustomCNTime(timeStamp: Long): String {
        return formatDateCN(DATA_CN_PATTERN, timeStamp)
    }

    fun getNowDateCNStr(): String {
        val sdf = SimpleDateFormat(DATA_CN_PATTERN, Locale.CHINA)
        return sdf.format(nowCN)
    }

    fun getNowDateStr(): String {
        val sdf = SimpleDateFormat(DATE_PATTERN, Locale.CHINA)
        return sdf.format(now)
    }

    fun getDateStr(date: Date): String {
        val sdf = SimpleDateFormat(DATE_PATTERN, Locale.CHINA)
        return sdf.format(date)
    }

    fun getNowDateMinStr(): String {
        val sdf = SimpleDateFormat(DATE_PATTERN_MM, Locale.CHINA)
        return sdf.format(now)
    }

    fun getNowMinStr(): String {
        val sdf = SimpleDateFormat(DATE_PATTERN_HOUR_MM, Locale.CHINA)
        return sdf.format(now)
    }

    fun getYesterday(): String {
        val calendar = Calendar.getInstance()
        calendar.time = now
        val day = calendar[Calendar.DATE]
        calendar.set(Calendar.DATE, day - 1)
        return SimpleDateFormat(DATE_PATTERN).format(calendar.time)
    }

    fun getAfterDayStr(day: String): String {
        val calendar = Calendar.getInstance()
        var date: Date? = Date()
        try {
            date = SimpleDateFormat(DATE_PATTERN).parse(day)
        } catch (e: ParseException) {
            e.printStackTrace()
        }
        calendar.time = date
        val day1 = calendar[Calendar.DATE]
        calendar.set(Calendar.DATE, day1 + 1)
        return SimpleDateFormat(DATE_PATTERN).format(calendar.time)
    }

    /**
     * 时间戳 获取年
     */
    fun getYear(time: Long?): Int {
        val sdf = SimpleDateFormat("yyyy-MM-dd")
        var date: Date? = Date()
        val timeString = sdf.format(time)
        try {
            date = sdf.parse(timeString)
        } catch (e: ParseException) {
            e.printStackTrace()
        }
        val calendar = Calendar.getInstance()
        calendar.time = date
        return calendar[Calendar.YEAR]
    }

    /**
     * 时间戳 获取月
     */
    fun getMonth(time: Long?): Int {
        val sdf = SimpleDateFormat("yyyy-MM-dd")
        var date: Date? = Date()
        val timeString = sdf.format(time)
        try {
            date = sdf.parse(timeString)
        } catch (e: ParseException) {
            e.printStackTrace()
        }
        val calendar = Calendar.getInstance()
        calendar.time = date
        return calendar[Calendar.MONTH] + 1
    }

    /**
     * 时间戳 获取天
     */
    fun getDay(time: Long?): Int {
        val sdf = SimpleDateFormat("yyyy-MM-dd")
        var date: Date? = Date()
        val timeString = sdf.format(time)
        try {
            date = sdf.parse(timeString)
        } catch (e: ParseException) {
            e.printStackTrace()
        }
        val calendar = Calendar.getInstance()
        calendar.time = date
        return calendar[Calendar.DAY_OF_MONTH]
    }


    /**
     * 20201103取年
     */
    fun getSimpleFormatYear(day: String?): Int {
        var date: Date? = Date()
        try {
            date = SimpleDateFormat(SIMPLE_DATE_PATTERN).parse(day)
        } catch (e: ParseException) {
            e.printStackTrace()
        }
        val calendar = Calendar.getInstance()
        calendar.time = date
        return calendar[Calendar.YEAR]
    }

    /**
     * 20201103取月
     */
    fun getSimpleFormatMonth(day: String?): Int {
        var date: Date? = Date()
        try {
            date = SimpleDateFormat(SIMPLE_DATE_PATTERN).parse(day)
        } catch (e: ParseException) {
            e.printStackTrace()
        }
        val calendar = Calendar.getInstance()
        calendar.time = date
        return calendar[Calendar.MONTH] + 1
    }

    /**
     * 20201103取日
     */
    fun getSimpleFormatDay(day: String?): Int {
        var date: Date? = Date()
        try {
            date = SimpleDateFormat(SIMPLE_DATE_PATTERN).parse(day)
        } catch (e: ParseException) {
            e.printStackTrace()
        }
        val calendar = Calendar.getInstance()
        calendar.time = date
        return calendar[Calendar.DAY_OF_MONTH]
    }


    fun isToday(date: Date?): Boolean {
        val c1 = Calendar.getInstance()
        c1.time = date
        val year1 = c1[Calendar.YEAR]
        val month1 = c1[Calendar.MONTH] + 1
        val day1 = c1[Calendar.DAY_OF_MONTH]
        val c2 = Calendar.getInstance()
        c2.time = Date()
        val year2 = c2[Calendar.YEAR]
        val month2 = c2[Calendar.MONTH] + 1
        val day2 = c2[Calendar.DAY_OF_MONTH]
        return year1 == year2 && month1 == month2 && day1 == day2
    }

    fun getHour(): Int {
        val calendar = Calendar.getInstance()
        calendar.time = Date()
        return calendar[Calendar.HOUR_OF_DAY]
    }

    fun getMin(): Int {
        val calendar = Calendar.getInstance()
        calendar.time = Date()
        return calendar[Calendar.MINUTE]
    }

    fun getHourByTime(time: Long): Int {
        val calendar = Calendar.getInstance()
        calendar.time = Date(time)
        return calendar[Calendar.HOUR_OF_DAY]
    }

    fun getMinByTime(time: Long): Int {
        val calendar = Calendar.getInstance()
        calendar.time = Date(time)
        return calendar[Calendar.MINUTE]
    }

    fun getDayBeforeCal(day: Int): Calendar {
        val dayBefore = Calendar.getInstance()
        dayBefore.add(Calendar.DAY_OF_MONTH, -day)
        return dayBefore
    }

    fun getNumOfDay(time: Long): Int {
        val delTime = (Calendar.getInstance().time.time - time) / 1000
        return (delTime / (24 * 60 * 60)).toInt()

    }
}