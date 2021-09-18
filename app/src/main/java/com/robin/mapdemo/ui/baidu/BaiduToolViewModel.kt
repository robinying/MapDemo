package com.robin.mapdemo.ui.baidu

import com.baidu.mapapi.model.LatLng
import com.robin.jetpackmvvm.base.viewmodel.BaseViewModel

class BaiduToolViewModel:BaseViewModel() {
    var stLatLng: LatLng? = null
    var edLatLng: LatLng? = null

    //设置起始点，还是终点
    var setType = 0
}