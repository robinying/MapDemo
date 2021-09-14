package com.robin.mapdemo.ui.baidu

import android.os.Bundle
import com.baidu.mapapi.map.BaiduMap
import com.baidu.mapapi.map.MapView
import com.baidu.mapapi.search.poi.*
import com.robin.jetpackmvvm.base.viewmodel.BaseViewModel
import com.robin.mapdemo.R
import com.robin.mapdemo.app.badiu.BaiduLifecycleObserver
import com.robin.mapdemo.app.base.BaseFragment
import com.robin.mapdemo.databinding.FragmentBaiduPoiBinding
import com.baidu.mapapi.search.poi.PoiCitySearchOption
import com.blankj.utilcode.util.GsonUtils
import com.blankj.utilcode.util.LogUtils


class BaiduPoiFragment : BaseFragment<BaseViewModel, FragmentBaiduPoiBinding>() {
    private lateinit var mMapView: MapView
    private lateinit var mBaiduMap: BaiduMap
    private lateinit var mPoiSearch:PoiSearch
    override fun layoutId(): Int {
        return R.layout.fragment_baidu_poi
    }

    override fun initView(savedInstanceState: Bundle?) {
        mMapView = mDataBinding.mapView
        mBaiduMap = mMapView.map
        lifecycle.addObserver(BaiduLifecycleObserver(mMapView))
        mPoiSearch = PoiSearch.newInstance()
        mPoiSearch.setOnGetPoiSearchResultListener(object:OnGetPoiSearchResultListener{
            override fun onGetPoiResult(poiResult: PoiResult?) {
                poiResult?.let {
                    LogUtils.d("robinTest, poiResult size:${it.allPoi.size}")

                }
            }

            override fun onGetPoiDetailResult(p0: PoiDetailResult?) {

            }

            override fun onGetPoiDetailResult(poiDetailSearchResult: PoiDetailSearchResult?) {

            }

            override fun onGetPoiIndoorResult(poiIndoorResult: PoiIndoorResult?) {

            }
        })
    }

    override fun lazyLoadData() {
        super.lazyLoadData()
        mPoiSearch.searchInCity(
            PoiCitySearchOption()
                .city("宁波") //必填
                .keyword("美食") //必填
                .pageNum(0)
                .pageCapacity(20)
        )
    }

    override fun onDestroy() {
        super.onDestroy()
        mPoiSearch.destroy()
    }
}