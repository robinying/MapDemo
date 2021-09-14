package com.robin.mapdemo.ui.baidu

import com.baidu.mapapi.map.Marker
import com.baidu.mapapi.model.LatLng
import com.robin.jetpackmvvm.base.viewmodel.BaseViewModel

class BaiduViewModel:BaseViewModel() {
    var latLng:LatLng?=null
    val markerList:MutableList<Marker> = arrayListOf()
}