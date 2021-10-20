package com.robin.mapdemo.data

import com.blankj.utilcode.util.LogUtils
import com.chad.library.adapter.base.loadmore.LoadMoreStatus
import com.robin.jetpackmvvm.state.ResultState
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class WeatherRepository @Inject constructor() {

    fun fetchWeatherForecast(location: String) = flow {
        emit(ResultState.Loading("网络加载中..."))
        LogUtils.d("robinTest", "current Thread name:" + Thread.currentThread().name)
        // Fake api call
        delay(1000)
        // Send a random fake weather forecast data
        LogUtils.d("robinTest", "current Thread name:" + Thread.currentThread().name)
        emit(ResultState.Success(location + (0..20).random()))
    }

    /**
     * This method is used to get data stream of fake weather
     * forecast data in real time with 1000 ms delay
     */
    fun fetchWeatherForecastRealTime(): Flow<ResultState<Int>> = flow {
        // Fake data stream
        while (true) {
            delay(1000)
            // Send a random fake weather forecast data
            emit(ResultState.Success((0..20).random()))
        }
    }

    /**
     * This method is used to get data stream of fake weather
     * forecast data in real time from another fake data source
     * with 500 ms delay
     */
    fun fetchWeatherForecastRealTimeOtherDataSource() = flow {
        emit(ResultState.Loading("加载中"))
        // Fake data stream
        while (true) {
            delay(500)
            // Send a random fake weather forecast data
            emit(ResultState.Success((0..20).random()))
        }
    }
}