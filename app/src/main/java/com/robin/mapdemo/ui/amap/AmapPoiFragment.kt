package com.robin.mapdemo.ui.amap

import android.os.Bundle
import com.amap.api.maps.AMap
import com.amap.api.maps.MapView
import com.blankj.utilcode.util.LogUtils
import com.robin.jetpackmvvm.base.viewmodel.BaseViewModel
import com.robin.mapdemo.R
import com.robin.mapdemo.app.amap.AmapLifecycleObserver
import com.robin.mapdemo.app.base.BaseFragment
import com.robin.mapdemo.databinding.FragmentAmapPoiBinding

class AmapPoiFragment : BaseFragment<BaseViewModel, FragmentAmapPoiBinding>() {
    private lateinit var mAMap: AMap
    override fun layoutId(): Int {
        return R.layout.fragment_amap_poi
    }

    override fun initView(savedInstanceState: Bundle?) {
        mDataBinding.let { binding ->
            binding.mapView.onCreate(savedInstanceState)
            mAMap = binding.mapView.map
            lifecycle.addObserver(AmapLifecycleObserver(null, binding.mapView))
        }
        LogUtils.d("robinTest  AmapPoiFragment initView")
        mAMap.isTrafficEnabled = true
        mAMap.uiSettings.isTiltGesturesEnabled = false
        mAMap.uiSettings.isRotateGesturesEnabled = false
        mAMap.uiSettings.isMyLocationButtonEnabled = false
        mAMap.uiSettings.isZoomControlsEnabled = false
        mAMap.uiSettings.isZoomGesturesEnabled = true
        mAMap.setOnMapLoadedListener {
            LogUtils.d("robinTest", "Amap Poi Loaded")
        }

    }
}