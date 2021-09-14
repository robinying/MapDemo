package com.robin.mapdemo.ui.baidu

import android.os.Bundle
import com.baidu.location.BDAbstractLocationListener
import com.baidu.location.BDLocation
import com.baidu.location.LocationClient
import com.baidu.location.LocationClientOption
import com.baidu.mapapi.map.*
import com.baidu.mapapi.model.LatLng
import com.baidu.mapapi.search.core.SearchResult
import com.baidu.mapapi.search.district.DistrictResult
import com.baidu.mapapi.search.district.DistrictSearch
import com.baidu.mapapi.search.district.DistrictSearchOption
import com.baidu.mapapi.search.district.OnGetDistricSearchResultListener
import com.blankj.utilcode.util.LogUtils
import com.robin.commonUi.util.ResUtils
import com.robin.mapdemo.R
import com.robin.mapdemo.app.badiu.BaiduLifecycleObserver
import com.robin.mapdemo.app.badiu.BaiduMapUtil
import com.robin.mapdemo.app.base.BaseFragment
import com.robin.mapdemo.databinding.FragmentBaiduBinding


class BaiduFragment : BaseFragment<BaiduViewModel, FragmentBaiduBinding>() {
    private lateinit var mapView: MapView
    private lateinit var mBaiduMap: BaiduMap
    private lateinit var mDistrictSearch: DistrictSearch
    private lateinit var mLocationClient: LocationClient
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
        mBaiduMap.mapType = BaiduMap.MAP_TYPE_NORMAL
        mBaiduMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build()))
        mBaiduMap.isMyLocationEnabled = true
        //customMyLocation()
        lifecycle.addObserver(BaiduLifecycleObserver(mapView))
        mDistrictSearch = DistrictSearch.newInstance()
        mDistrictSearch.setOnDistrictSearchListener(object : OnGetDistricSearchResultListener {
            override fun onGetDistrictResult(districtResult: DistrictResult?) {
                drawDistrict(districtResult)
            }
        })
    }

    override fun createObserver() {
        super.createObserver()
        startDistrictSearch()
        startLocation()
    }

    override fun onDestroy() {
        super.onDestroy()
        mBaiduMap.isMyLocationEnabled = false
        stopLocation()
    }

    private fun startDistrictSearch() {
        mDistrictSearch.searchDistrict(
            DistrictSearchOption()
                .cityName("宁波市")
                .districtName("鄞州区")
        )
    }

    private fun drawDistrict(districtResult: DistrictResult?) {
        if (districtResult == null) {
            return
        }
        if (districtResult.error == SearchResult.ERRORNO.NO_ERROR) {
            val polyLines = districtResult.getPolylines() ?: return
            for (polyline in polyLines) {
                //区域划线
//                val ooPolyline: OverlayOptions =
//                    PolylineOptions().width(5).points(polyline)
//                        .color(ResUtils.getColor(R.color.colorBlack333))
//                mBaiduMap.addOverlay(ooPolyline)

                val ooPolygon: OverlayOptions =
                    PolygonOptions().points(polyline)
                        .stroke(Stroke(5, ResUtils.getColor(R.color.colorPrimary)))
                        .fillColor(0x55E36161)
                mBaiduMap.addOverlay(ooPolygon)
            }
        }
    }

    /*
    *通过MyLocationConfiguration类来构造包括定位的属性，定位模式、是否开启方向、
    * 设置自定义定位图标、精度圈填充颜色以及精度圈边框颜色5个属性
    * */
    private fun customMyLocation() {
        val myLocationConfiguration = MyLocationConfiguration(
            MyLocationConfiguration.LocationMode.NORMAL,
            true,
            BitmapDescriptorFactory.fromResource(R.drawable.ic_pokemon_location)
        )
        mBaiduMap.setMyLocationConfiguration(myLocationConfiguration)
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
                    BaiduMapUtil.animateMapStatus(mBaiduMap, latLng, 13f)
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
}