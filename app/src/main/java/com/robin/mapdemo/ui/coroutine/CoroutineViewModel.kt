package com.robin.mapdemo.ui.coroutine

import androidx.lifecycle.asLiveData
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import com.robin.jetpackmvvm.base.viewmodel.BaseViewModel
import com.robin.jetpackmvvm.ext.fire
import com.robin.jetpackmvvm.state.ResultState
import com.robin.jetpackmvvm.util.LogUtils
import com.robin.mapdemo.data.NewsRepository
import com.robin.mapdemo.data.WeatherRepository
import com.robin.mapdemo.model.AppDatabase
import com.robin.mapdemo.model.Article
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

@HiltViewModel
class CoroutineViewModel @Inject constructor(
    private val weatherRepository: WeatherRepository,
    private val newsRepository: NewsRepository,
    private val appDatabase: AppDatabase
) :
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


    fun getNews() =
        newsRepository.getNews().asLiveData(viewModelScope.coroutineContext + Dispatchers.IO)

    fun getNewsTwo() =
        fire {
            getFakeNews()
        }


    suspend fun getFakeNews(): Result<String> {
        delay(2000)
        return Result.success("Fake News")
    }

    fun saveArticleToData() {
        viewModelScope.launch(Dispatchers.IO) {
            kotlin.runCatching {
                LogUtils.debugInfo("robinTest", " start insert")
                val articles = mutableListOf<Article>()
                for (i in 1..100) {
                    articles.add(
                        Article(
                            i.toLong(),
                            "robin",
                            "robin$i",
                            "song$i",
                            "url$i",
                            i,
                            Date()
                        )
                    )
                }
                appDatabase.articleDao().saveArticles(*articles.toTypedArray())
            }.onSuccess {
                LogUtils.debugInfo("robinTest", " end insert")
            }.onFailure {

            }

        }
    }

    fun getArticles() =
        appDatabase.articleDao().getArticles().asLiveData(viewModelScope.coroutineContext + Dispatchers.IO)


}