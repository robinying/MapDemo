package com.robin.mapdemo.ui.baidu

import android.os.Bundle
import com.baidu.mapapi.map.*

import com.robin.mapdemo.R
import com.robin.mapdemo.app.badiu.BaiduLifecycleObserver
import com.robin.mapdemo.app.base.BaseFragment
import com.robin.mapdemo.databinding.FragmentBaiduBinding

class BaiduFragment : BaseFragment<BaiduViewModel, FragmentBaiduBinding>() {
    private lateinit var mapView: MapView
    private lateinit var mBaiduMap: BaiduMap
    override fun layoutId() = R.layout.fragment_baidu

    override fun initView(savedInstanceState: Bundle?) {
        val builder = MapStatus.Builder()
        builder.overlook(-20f)
            .zoom(19f)
        val options = BaiduMapOptions()
        options.mapStatus(builder.build())
            .compassEnabled(false)
            .zoomControlsEnabled(false)
            .rotateGesturesEnabled(false)
        mapView = MapView(mActivity, options)
        mDataBinding.flMap.addView(mapView)
        mBaiduMap = mapView.map
        mBaiduMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build()))
        lifecycle.addObserver(BaiduLifecycleObserver(mapView))
    }
}