package com.robin.mapdemo.ui.test

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.blankj.utilcode.util.ToastUtils
import com.robin.jetpackmvvm.base.viewmodel.BaseViewModel
import com.robin.jetpackmvvm.data.model.ApiResponse
import com.robin.jetpackmvvm.ext.request
import com.robin.jetpackmvvm.network.BaseResponse
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class TestVBViewModel : BaseViewModel() {

     val data = MutableLiveData<String>()

    private suspend fun getData(): ApiResponse<String> {
        delay(3000)

        return ApiResponse(100, "success", "ready data")
    }

    fun test() {
        request({ getData() }, {
            data.postValue(it)
            ToastUtils.showShort("Get Data $it")
        }, {


        }, true)

    }

}