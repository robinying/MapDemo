package com.robin.mapdemo.ui.amap

import android.os.Bundle
import com.amap.api.maps.AMap
import com.amap.api.services.weather.LocalWeatherForecastResult
import com.amap.api.services.weather.LocalWeatherLiveResult
import com.amap.api.services.weather.WeatherSearch
import com.amap.api.services.weather.WeatherSearchQuery
import com.blankj.utilcode.util.GsonUtils
import com.blankj.utilcode.util.LogUtils
import com.robin.jetpackmvvm.base.viewmodel.BaseViewModel
import com.robin.mapdemo.R
import com.robin.mapdemo.app.amap.AmapLifecycleObserver
import com.robin.mapdemo.app.base.BaseFragment

import com.robin.mapdemo.databinding.FragmentAmapWeatherBinding

class AmapWeatherFragment: BaseFragment<BaseViewModel, FragmentAmapWeatherBinding>(),WeatherSearch.OnWeatherSearchListener {
    private lateinit var mAMap: AMap

    private lateinit var mWeatherSearch: WeatherSearch
    override fun layoutId(): Int {
        return R.layout.fragment_amap_weather
    }

    override fun initView(savedInstanceState: Bundle?) {
        mDataBinding.let { binding ->
            binding.mapView.onCreate(savedInstanceState)
            mAMap = binding.mapView.map
            lifecycle.addObserver(AmapLifecycleObserver(null, binding.mapView))
        }
        mWeatherSearch = WeatherSearch(mActivity)
        mAMap.isTrafficEnabled = true
        mAMap.uiSettings.isTiltGesturesEnabled = false
        mAMap.uiSettings.isRotateGesturesEnabled = false
        mAMap.uiSettings.isMyLocationButtonEnabled = false
        mAMap.uiSettings.isZoomControlsEnabled = false
        mAMap.uiSettings.isZoomGesturesEnabled = true
        mAMap.setOnMapLoadedListener {
            LogUtils.d("robinTest", "Amap Weather Loaded")
        }
        mWeatherSearch.setOnWeatherSearchListener(this)
    }

    override fun lazyLoadData() {
        super.lazyLoadData()
        val query = WeatherSearchQuery("宁波",WeatherSearchQuery.WEATHER_TYPE_LIVE)
        mWeatherSearch.query =query
        mWeatherSearch.searchWeatherAsyn()
    }

    override fun onWeatherLiveSearched(localWeatherLiveResult: LocalWeatherLiveResult?, rCode: Int) {
        if(rCode ==1000){
            localWeatherLiveResult?.let {
                val weatherLive = it.liveResult
                LogUtils.d("robinTest liveResult:${GsonUtils.toJson(weatherLive)}")
            }
        }

    }

    override fun onWeatherForecastSearched(localWeatherForecastResult: LocalWeatherForecastResult?, rCode: Int) {

    }
}