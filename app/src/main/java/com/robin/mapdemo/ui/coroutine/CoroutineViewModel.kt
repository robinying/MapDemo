package com.robin.mapdemo.ui.coroutine

import androidx.lifecycle.asLiveData
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import com.robin.jetpackmvvm.base.viewmodel.BaseViewModel
import com.robin.jetpackmvvm.state.ResultState
import com.robin.mapdemo.data.WeatherRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CoroutineViewModel @Inject constructor(var weatherRepository: WeatherRepository) :
    BaseViewModel() {

    var curLocation: String = ""

    /*
    *两种方式
    * */
    fun getWeather() = weatherRepository.fetchWeatherForecast(curLocation)
        .asLiveData(viewModelScope.coroutineContext + Dispatchers.IO)

    /*
    *两种方式
    * */
    fun getWeatherTwo() = liveData(Dispatchers.IO) {
        weatherRepository.fetchWeatherForecast(curLocation).onStart {

        }.collect {
            emit(it)
        }
    }

    fun getWeatherRealTime() = weatherRepository.fetchWeatherForecastRealTime()
        .asLiveData(viewModelScope.coroutineContext + Dispatchers.IO)

    fun startTimer() = flow {
        while (true) {
            delay(3000)
            emit(1)

        }
    }.asLiveData(viewModelScope.coroutineContext + Dispatchers.IO)


}