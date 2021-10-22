package com.robin.mapdemo.data

import com.blankj.utilcode.util.LogUtils
import com.robin.jetpackmvvm.state.ResultState
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class NewsRepository @Inject constructor() {

    fun getNews() = flow {
        emit(ResultState.Loading("网络加载中..."))

        // Fake api call
        delay(2000)
        // Send a random fake weather forecast data
        emit(ResultState.Success("new News"))
    }

    fun getFakeNews() = flow {
        delay(1500)
        emit(Result.success("Fake News"))

    }
}