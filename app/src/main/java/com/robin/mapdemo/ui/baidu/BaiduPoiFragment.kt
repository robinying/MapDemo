package com.robin.mapdemo.ui.baidu

import android.graphics.Color
import android.os.Bundle
import com.baidu.mapapi.map.*
import com.baidu.mapapi.model.LatLng
import com.baidu.mapapi.search.core.SearchResult
import com.baidu.mapapi.search.poi.*
import com.robin.jetpackmvvm.base.viewmodel.BaseViewModel
import com.robin.mapdemo.R
import com.robin.mapdemo.app.badiu.BaiduLifecycleObserver
import com.robin.mapdemo.app.base.BaseFragment
import com.robin.mapdemo.databinding.FragmentBaiduPoiBinding
import com.baidu.mapapi.search.poi.PoiCitySearchOption
import com.blankj.utilcode.util.GsonUtils
import com.blankj.utilcode.util.LogUtils
import com.baidu.mapapi.search.poi.PoiDetailSearchOption
import com.robin.mapdemo.app.badiu.BaiduMapUtil
import com.baidu.mapapi.map.OverlayOptions
import com.baidu.mapapi.search.busline.BusLineSearch
import com.baidu.mapapi.search.core.PoiInfo
import com.robin.commonUi.util.ResUtils
import com.baidu.mapapi.search.busline.BusLineSearchOption
import com.robin.mapdemo.widget.overlayutil.BusLineOverlay
import com.baidu.mapapi.search.weather.WeatherDataType

import com.baidu.mapapi.search.weather.WeatherSearchOption


class BaiduPoiFragment : BaseFragment<BaseViewModel, FragmentBaiduPoiBinding>() {
    private lateinit var mMapView: MapView
    private lateinit var mBaiduMap: BaiduMap
    private lateinit var mPoiSearch: PoiSearch
    private lateinit var mBusLineSearch: BusLineSearch
    private val markerBitmap by lazy(LazyThreadSafetyMode.NONE) {
        BitmapDescriptorFactory.fromResource(
            R.drawable.ic_delicious
        )
    }

    override fun layoutId(): Int {
        return R.layout.fragment_baidu_poi
    }

    override fun initView(savedInstanceState: Bundle?) {
        mMapView = mDataBinding.mapView
        mBaiduMap = mMapView.map
        lifecycle.addObserver(BaiduLifecycleObserver(mMapView))
        mPoiSearch = PoiSearch.newInstance()
        mBusLineSearch = BusLineSearch.newInstance()
        mPoiSearch.setOnGetPoiSearchResultListener(object : OnGetPoiSearchResultListener {
            override fun onGetPoiResult(poiResult: PoiResult?) {
                poiResult?.let {
                    if (it.allPoi.isNotEmpty()) {
                        //val poi = it.allPoi.first()
                        for (poi in it.allPoi) {
                            LogUtils.d("robinTest,poi type:${poi.type}")
                            if (poi.name.contains("157路")) {
                                mBusLineSearch.searchBusLine(
                                    BusLineSearchOption()
                                        .city("宁波")
                                        .uid(poi.uid)
                                )
                                break
                            } else {
                                mPoiSearch.searchPoiDetail(
                                    PoiDetailSearchOption()
                                        .poiUids(poi.uid)
                                )
                            }
                        }
                    }
                    LogUtils.d("robinTest, poiResult size:${it.allPoi.size}")

                }
            }

            override fun onGetPoiDetailResult(p0: PoiDetailResult?) {

            }

            override fun onGetPoiDetailResult(poiDetailSearchResult: PoiDetailSearchResult?) {
                poiDetailSearchResult?.let {
                    LogUtils.d("robinTest ,detailResult:${GsonUtils.toJson(it)}")
                    if (it.error == SearchResult.ERRORNO.NO_ERROR) {
                        val latLng = it.poiDetailInfoList.first().location
                        BaiduMapUtil.animateMapStatus(mBaiduMap, latLng, 13f)
                        addMarker(latLng)
                        addText(latLng, it.poiDetailInfoList.first().name)
                    }

                }

            }

            override fun onGetPoiIndoorResult(poiIndoorResult: PoiIndoorResult?) {

            }
        })
        mBusLineSearch.setOnGetBusLineSearchResultListener {
            if (it == null || it.error != SearchResult.ERRORNO.NO_ERROR) {
                return@setOnGetBusLineSearchResultListener
            }
            LogUtils.d("robinTest busResult:${GsonUtils.toJson(it)}")
            val busLineOverlay = BusLineOverlay(mBaiduMap)
            busLineOverlay.setData(it)
            busLineOverlay.addToMap()
            busLineOverlay.zoomToSpan()

        }
    }

    override fun lazyLoadData() {
        super.lazyLoadData()
//        mPoiSearch.searchInCity(
//            PoiCitySearchOption()
//                .city("宁波") //必填
//                .keyword("美食") //必填
//                .pageNum(0)
//                .pageCapacity(10)
//        )
        searchBus()
    }

    override fun onDestroy() {
        super.onDestroy()
        mPoiSearch.destroy()
        mBusLineSearch.destroy()
        markerBitmap.recycle()
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
        mBaiduMap.addOverlay(markerOptions) as Marker
    }

    private fun addText(latLng: LatLng, title: String) {
        val mTextOptions: OverlayOptions = TextOptions()
            .text(title) //文字内容
            .bgColor(Color.parseColor("#AAFFFF00")) //背景色
            .fontSize(18) //字号
            .fontColor(ResUtils.getColor(R.color.colorBlack333)) //文字颜色
            .rotate(20f) //旋转角度
            .position(latLng)

        //在地图上显示文字覆盖物
        val mText = mBaiduMap.addOverlay(mTextOptions)
    }

    private fun searchBus() {
        //TODO 目前搜索公交路径返回type为null
        mPoiSearch.searchInCity(
            PoiCitySearchOption()
                .city("宁波")
                .tag("bus")
                .keyword("157")
                .scope(2)
        )
    }


}