package com.robin.mapdemo.ui.baidu

import android.graphics.Color
import android.os.Bundle
import android.view.View
import com.amap.api.maps.AMap
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
import com.baidu.mapapi.search.weather.WeatherDataType
import com.baidu.mapapi.search.weather.WeatherSearch
import com.baidu.mapapi.search.weather.WeatherSearchOption
import com.blankj.utilcode.util.GsonUtils
import com.blankj.utilcode.util.LogUtils
import com.robin.commonUi.customview.actionbar.TitleBar
import com.robin.commonUi.util.ResUtils
import com.robin.jetpackmvvm.ext.nav
import com.robin.jetpackmvvm.ext.navigateAction
import com.robin.jetpackmvvm.ext.view.clickNoRepeat
import com.robin.jetpackmvvm.ext.view.setRoundRectBg
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
    private var mOverLay: Overlay? = null
    private val markerBitmap by lazy(LazyThreadSafetyMode.NONE) {
        BitmapDescriptorFactory.fromResource(
            R.drawable.ic_marker
        )
    }
    private var mMarker: Marker? = null
    private val weatherSearchOption by lazy{ WeatherSearchOption() }
    private lateinit var mWeatherSearch: WeatherSearch
    override fun layoutId() = R.layout.fragment_baidu

    override fun initView(savedInstanceState: Bundle?) {
        mWeatherSearch = WeatherSearch.newInstance()
        mWeatherSearch.setWeatherSearchResultListener {
            LogUtils.d("robinTest,weatherResult:${GsonUtils.toJson(it)}")

        }
        mDataBinding.titleBar.addAction(object : TitleBar.TextAction("Action") {
            override fun performAction(view: View?) {
                nav().navigateAction(R.id.action_baidu_to_view_pager)
            }
        })
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
        mBaiduMap.setOnMapClickListener(object : BaiduMap.OnMapClickListener {
            override fun onMapClick(latLng: LatLng?) {
                latLng?.let {
                    addMarker(it)
                    mMarker?.let { marker ->
                        mViewModel.markerList.add(marker)
                    }
                    //????????????????????????????????????????????????
                    weatherSearchOption.location(latLng)
                    mWeatherSearch.request(weatherSearchOption)
                }
            }

            override fun onMapPoiClick(p0: MapPoi?) {

            }

        })
        weatherSearchOption.weatherDataType(WeatherDataType.WEATHER_DATA_TYPE_ALL)
        //customMyLocation()
        lifecycle.addObserver(BaiduLifecycleObserver(mapView))
        mDistrictSearch = DistrictSearch.newInstance()
        mDistrictSearch.setOnDistrictSearchListener(object : OnGetDistricSearchResultListener {
            override fun onGetDistrictResult(districtResult: DistrictResult?) {
                drawDistrict(districtResult)
            }
        })
        mDataBinding.btClear.setRoundRectBg(ResUtils.getColor(R.color.colorPrimary), 16f)
        mDataBinding.btClear.clickNoRepeat {
            for (marker in mViewModel.markerList) {
                marker.remove()
            }
        }
    }

    override fun createObserver() {
        super.createObserver()
        startDistrictSearch()
        startLocation()
    }

    override fun onDestroy() {
        super.onDestroy()
        mBaiduMap.isMyLocationEnabled = false
        markerBitmap.recycle()
        mWeatherSearch.destroy()
        stopLocation()
    }

    private fun startDistrictSearch() {
        mDistrictSearch.searchDistrict(
            DistrictSearchOption()
                .cityName("?????????")
                .districtName("?????????")
        )
    }

    private fun drawDistrict(districtResult: DistrictResult?) {
        if (districtResult == null) {
            return
        }
        if (districtResult.error == SearchResult.ERRORNO.NO_ERROR) {
            val polyLines = districtResult.getPolylines() ?: return
            for (polyline in polyLines) {
                //????????????
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
    *??????MyLocationConfiguration????????????????????????????????????????????????????????????????????????
    * ??????????????????????????????????????????????????????????????????????????????5?????????
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


        //??????LocationClientOption??????LocationClient????????????
        val option = LocationClientOption()
        option.isOpenGps = true // ??????gps
        option.setCoorType("bd09ll") // ??????????????????
        option.setScanSpan(30000)
        //?????????????????????????????????????????????????????????
        option.setIsNeedAddress(true)
        //???????????????????????????????????????
        option.setIsNeedLocationDescribe(true)
        //???????????????false??????????????????gps???????????????1S1???????????????GPS??????
        option.isLocationNotify = true
        //???????????????false??????????????????????????????????????????????????????BDLocation.getLocationDescribe?????????????????????????????????????????????????????????
        option.setIsNeedLocationDescribe(true)
        //???????????????false?????????????????????????????????????????????????????????????????????????????????????????????
        option.setIsNeedAltitude(false)


        //??????locationClientOption
        mLocationClient.locOption = option

        //??????LocationListener?????????
        mLocationClient.registerLocationListener(object : BDAbstractLocationListener() {
            override fun onReceiveLocation(bdLocation: BDLocation?) {
                LogUtils.d("BaiduMap bdLocation:$bdLocation")
                bdLocation?.let {
                    val locData = MyLocationData.Builder()
                        .accuracy(it.radius) // ?????????????????????????????????????????????????????????0-360
                        .direction(it.direction)
                        .latitude(it.latitude)
                        .longitude(it.longitude).build()
                    mBaiduMap.setMyLocationData(locData)
                    val latLng = LatLng(it.latitude, it.longitude)
                    BaiduMapUtil.animateMapStatus(mBaiduMap, latLng, 13f)
                    updateOverlay(300.0, latLng)
                    stopLocation()
                }
            }
        })
        //????????????????????????
        mLocationClient.start()
    }

    private fun stopLocation() {
        mLocationClient.stop()
    }

    private fun updateOverlay(radius: Double, latLng: LatLng) {
        mOverLay?.let {
            it.remove()
        }
        val overlayOptions = CircleOptions().fillColor(ResUtils.getColor(R.color.fence_fill_color))
            .center(latLng)
            .radius(radius.toInt())
        mOverLay = mBaiduMap.addOverlay(overlayOptions)
    }

    private fun addMarker(latLng: LatLng) {
        if (latLng.latitude == 0.0 || latLng.longitude == 0.0) {
            return
        }
        val markerOptions = MarkerOptions()
            .animateType(MarkerOptions.MarkerAnimateType.grow)
            .position(latLng)
            .icon(markerBitmap)
            .yOffset(10)
        mMarker = mBaiduMap.addOverlay(markerOptions) as Marker
    }

    //?????????????????????????????????
    private fun clearAll() {
        mBaiduMap.clear()
    }

}