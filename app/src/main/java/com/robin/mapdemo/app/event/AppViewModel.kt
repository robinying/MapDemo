package com.robin.mapdemo.app.event

import com.robin.jetpackmvvm.base.viewmodel.BaseViewModel
import com.robin.jetpackmvvm.callback.livedata.UnPeekLiveData
import com.robin.jetpackmvvm.util.AnimSettingUtil

class AppViewModel : BaseViewModel() {

    //App 列表动画
    var appAnimation = UnPeekLiveData<Int>()

    init {
        //初始化列表动画
        appAnimation.value = AnimSettingUtil.getListMode()
    }
}