package com.robin.mapdemo.ui.baidu

import android.os.Bundle
import com.baidu.mapapi.map.BaiduMap
import com.baidu.mapapi.map.MapView
import com.robin.jetpackmvvm.base.viewmodel.BaseViewModel
import com.robin.mapdemo.R
import com.robin.mapdemo.app.badiu.BaiduLifecycleObserver
import com.robin.mapdemo.app.base.BaseFragment
import com.robin.mapdemo.databinding.FragmentBaiduRouteBinding

class BaiduRouteFragment : BaseFragment<BaseViewModel, FragmentBaiduRouteBinding>() {
    private lateinit var mMapView: MapView
    private lateinit var mBaiduMap: BaiduMap
    override fun layoutId(): Int {
        return R.layout.fragment_baidu_route
    }

    override fun initView(savedInstanceState: Bundle?) {
        mMapView = mDataBinding.mapView
        mBaiduMap = mMapView.map
        lifecycle.addObserver(BaiduLifecycleObserver(mMapView))
    }
}