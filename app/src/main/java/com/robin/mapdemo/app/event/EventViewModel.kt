package com.robin.mapdemo.app.event

import com.robin.jetpackmvvm.base.viewmodel.BaseViewModel
import com.robin.jetpackmvvm.callback.livedata.event.EventLiveData

/**
 * 作者　: hegaojian
 * 时间　: 2019/5/2
 * 描述　:APP全局的ViewModel，可以在这里发送全局通知替代EventBus，LiveDataBus等
 */
class EventViewModel : BaseViewModel() {


    //添加TODO通知
    val todoEvent = EventLiveData<Boolean>()



}