package com.robin.mapdemo.ui.baidu

import android.os.Bundle
import com.baidu.location.BDAbstractLocationListener
import com.baidu.location.BDLocation
import com.baidu.location.LocationClient
import com.baidu.location.LocationClientOption

import com.baidu.mapapi.map.BaiduMap
import com.baidu.mapapi.map.MapPoi
import com.baidu.mapapi.map.MapView
import com.baidu.mapapi.map.MyLocationData
import com.baidu.mapapi.model.LatLng
import com.baidu.mapapi.utils.DistanceUtil
import com.blankj.utilcode.util.GsonUtils
import com.blankj.utilcode.util.LogUtils
import com.blankj.utilcode.util.ToastUtils
import com.robin.jetpackmvvm.base.viewmodel.BaseViewModel
import com.robin.mapdemo.R
import com.robin.mapdemo.app.badiu.BaiduLifecycleObserver
import com.robin.mapdemo.app.badiu.BaiduMapUtil
import com.robin.mapdemo.app.base.BaseFragment
import com.robin.mapdemo.databinding.FragmentBaiduToolBinding

class BaiduToolFragment : BaseFragment<BaiduToolViewModel, FragmentBaiduToolBinding>(),
    BaiduMap.OnMapClickListener {
    private lateinit var mMapView: MapView
    private lateinit var mBaiduMap: BaiduMap
    private lateinit var mLocationClient: LocationClient
    override fun layoutId(): Int {
        return R.layout.fragment_baidu_tool
    }

    override fun initView(savedInstanceState: Bundle?) {
        mMapView = mDataBinding.mapView
        mBaiduMap = mMapView.map
        lifecycle.addObserver(BaiduLifecycleObserver(mMapView))
        mBaiduMap.setOnMapClickListener(this)
    }

    override fun lazyLoadData() {
        super.lazyLoadData()
        startLocation()
    }

    override fun onDestroy() {
        super.onDestroy()
        stopLocation()
    }

    private fun startLocation() {
        mLocationClient = LocationClient(mActivity)


        //通过LocationClientOption设置LocationClient相关参数
        val option = LocationClientOption()
        option.isOpenGps = true // 打开gps
        option.setCoorType("bd09ll") // 设置坐标类型
        option.setScanSpan(30000)
        //可选，设置是否需要地址信息，默认不需要
        option.setIsNeedAddress(true)
        //可选，设置是否需要地址描述
        option.setIsNeedLocationDescribe(true)
        //可选，默认false，设置是否当gps有效时按照1S1次频率输出GPS结果
        option.isLocationNotify = true
        //可选，默认false，设置是否需要位置语义化结果，可以在BDLocation.getLocationDescribe里得到，结果类似于“在北京天安门附近”
        option.setIsNeedLocationDescribe(true)
        //可选，默认false，设置定位时是否需要海拔信息，默认不需要，除基础定位版本都可用
        option.setIsNeedAltitude(false)


        //设置locationClientOption
        mLocationClient.locOption = option

        //注册LocationListener监听器
        mLocationClient.registerLocationListener(object : BDAbstractLocationListener() {
            override fun onReceiveLocation(bdLocation: BDLocation?) {
                LogUtils.d("BaiduMap bdLocation:$bdLocation")
                bdLocation?.let {
                    val locData = MyLocationData.Builder()
                        .accuracy(it.radius) // 此处设置开发者获取到的方向信息，顺时针0-360
                        .direction(it.direction)
                        .latitude(it.latitude)
                        .longitude(it.longitude).build()
                    mBaiduMap.setMyLocationData(locData)
                    val latLng = LatLng(it.latitude, it.longitude)
                    BaiduMapUtil.animateMapStatus(mBaiduMap, latLng, 12f)
                    stopLocation()
                }
            }
        })
        //开启地图定位图层
        mLocationClient.start()
    }

    private fun stopLocation() {
        mLocationClient.stop()
    }

    override fun onMapClick(latLng: LatLng?) {
        latLng?.let {
            ToastUtils.showShort("Click position ${GsonUtils.toJson(it)}")
        }

    }

    override fun onMapPoiClick(mapPoi: MapPoi?) {
        mapPoi?.let {

            if (mViewModel.setType == 0) {
                mViewModel.stLatLng = it.position
                mViewModel.setType = 1
                ToastUtils.showShort("Click select start MapPoi ${it.name}")
            } else {
                mViewModel.edLatLng = it.position
                mViewModel.setType = 0
                val distance =
                    DistanceUtil.getDistance(mViewModel.stLatLng!!, mViewModel.edLatLng!!)
                ToastUtils.showShort("Click select end MapPoi ${it.name} --distance :$distance")
            }
        }
    }
}