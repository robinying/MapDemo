package com.robin.mapdemo.ui.main

import android.provider.SyncStateContract
import android.util.Log
import androidx.lifecycle.viewModelScope
import com.arcsoft.face.ErrorInfo

import com.arcsoft.face.FaceEngine
import com.arcsoft.face.enums.RuntimeABI
import com.blankj.utilcode.util.LogUtils
import com.blankj.utilcode.util.ToastUtils
import com.robin.jetpackmvvm.base.BaseApp
import com.robin.jetpackmvvm.base.viewmodel.BaseViewModel
import com.robin.mapdemo.BuildConfig
import com.robin.mapdemo.R
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainViewModel : BaseViewModel() {

    fun activeArcFace() {
        //激活虹软引擎之前需要获取到READ_PHONE_STATE
        val runtimeABI = FaceEngine.getRuntimeABI()
        LogUtils.i("subscribe: getRuntimeABI() $runtimeABI")

        viewModelScope.launch {
            runCatching {
                withContext(Dispatchers.IO) {
                    val start = System.currentTimeMillis()
                    FaceEngine.activeOnline(
                        BaseApp.instance,
                        BuildConfig.ARCFACE_APP_ID,
                        BuildConfig.ARCFACE_SDK_KEY
                    )

                }

            }.onSuccess { activeCode ->
                LogUtils.d("robinTest","activeCode:$activeCode")
                if (activeCode == ErrorInfo.MOK) {
                    ToastUtils.showShort("Active success")
                } else if (activeCode == ErrorInfo.MERR_ASF_ALREADY_ACTIVATED) {
                    ToastUtils.showShort("Already Active")
                } else {
                    ToastUtils.showShort("Active fail, error code:$activeCode")
                }
            }.onFailure {


            }


        }
    }
}