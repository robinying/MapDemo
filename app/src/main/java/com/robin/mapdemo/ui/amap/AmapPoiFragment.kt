package com.robin.mapdemo.ui.amap

import android.graphics.BitmapFactory
import android.os.Bundle
import android.os.Handler
import android.text.Editable
import android.text.TextWatcher
import com.amap.api.maps.AMap
import com.amap.api.maps.CameraUpdateFactory
import com.amap.api.maps.model.*
import com.amap.api.services.core.PoiItem
import com.amap.api.services.poisearch.PoiResult
import com.amap.api.services.poisearch.PoiSearch
import com.blankj.utilcode.util.CollectionUtils
import com.blankj.utilcode.util.LogUtils
import com.blankj.utilcode.util.ToastUtils
import com.robin.jetpackmvvm.base.viewmodel.BaseViewModel
import com.robin.mapdemo.app.amap.AmapLifecycleObserver
import com.robin.mapdemo.app.base.BaseFragment
import com.robin.mapdemo.databinding.FragmentAmapPoiBinding
import android.view.animation.LinearInterpolator
import android.widget.ArrayAdapter
import com.amap.api.maps.model.animation.ScaleAnimation
import com.amap.api.services.help.InputtipsQuery
import com.amap.api.services.help.Inputtips
import com.amap.api.services.help.Tip
import com.amap.api.maps.utils.SpatialRelationUtil
import com.amap.api.maps.utils.overlay.MovingPointOverlay
import com.robin.mapdemo.R


class AmapPoiFragment : BaseFragment<BaseViewModel, FragmentAmapPoiBinding>() {
    private lateinit var mAMap: AMap
    private lateinit var mPoiSearch: PoiSearch
    private lateinit var mHandler: Handler
    override fun layoutId(): Int {
        return com.robin.mapdemo.R.layout.fragment_amap_poi
    }

    override fun initView(savedInstanceState: Bundle?) {
        mDataBinding.let { binding ->
            binding.mapView.onCreate(savedInstanceState)
            mAMap = binding.mapView.map
            lifecycle.addObserver(AmapLifecycleObserver(null, binding.mapView))
        }
        LogUtils.d("robinTest  AmapPoiFragment initView")
        mHandler = Handler()
        mAMap.isTrafficEnabled = true
        mAMap.uiSettings.isTiltGesturesEnabled = false
        mAMap.uiSettings.isRotateGesturesEnabled = false
        mAMap.uiSettings.isMyLocationButtonEnabled = false
        mAMap.uiSettings.isZoomControlsEnabled = false
        mAMap.uiSettings.isZoomGesturesEnabled = true
        mAMap.setOnMapLoadedListener {
            LogUtils.d("robinTest", "Amap Poi Loaded")
        }
        val query = PoiSearch.Query("医院", "", "宁波")
        query.pageSize = 20
        query.pageNum = 0
        mPoiSearch = PoiSearch(mActivity, query)
        mPoiSearch.setOnPoiSearchListener(object : PoiSearch.OnPoiSearchListener {
            override fun onPoiSearched(poiResult: PoiResult?, rCode: Int) {
                //LogUtils.d("robinTest rCode:$rCode --size" + poiResult?.pois?.size)
                if (rCode == 1000) {
                    if (CollectionUtils.isNotEmpty(poiResult?.pois)) {
                        val markerOptionsList: ArrayList<MarkerOptions> = arrayListOf()
                        val pointList: ArrayList<LatLng> = arrayListOf()
                        for (poiItem in poiResult!!.pois) {
                            val latLng =
                                LatLng(poiItem.latLonPoint.latitude, poiItem.latLonPoint.longitude)
                            val markerOptions = MarkerOptions()
                                .position(latLng)
                                .title(poiItem.title)
                                .snippet(poiItem.snippet)
                                .icon(
                                    BitmapDescriptorFactory.fromBitmap(
                                        BitmapFactory.decodeResource(
                                            resources,
                                            com.robin.mapdemo.R.drawable.ic_hospital
                                        )
                                    )
                                )
                            markerOptionsList.add(markerOptions)
                            pointList.add(latLng)
                        }
                        mAMap.addMarkers(markerOptionsList, true)
                        moveMap(pointList)
                    }
                }

            }

            override fun onPoiItemSearched(poiItem: PoiItem?, rCode: Int) {

            }
        })

        mAMap.setOnMarkerClickListener {
            rotatePoint(it)
            ToastUtils.showShort(it.snippet)
            // marker 对象被点击时回调的接口
            // 返回 true 则表示接口已响应事件，否则返回false
            return@setOnMarkerClickListener false
        }
        mDataBinding.etPosition.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }

            override fun afterTextChanged(s: Editable?) {
                s?.let {
                    val position = it.toString()
                    val inputquery = InputtipsQuery(position, "宁波")
                    inputquery.cityLimit = true
                    val inputTips = Inputtips(mActivity, inputquery)
                    inputTips.setInputtipsListener(object : Inputtips.InputtipsListener {
                        override fun onGetInputtips(tipList: MutableList<Tip>?, rCode: Int) {
                            if (rCode == 1000 && CollectionUtils.isNotEmpty(tipList)) {
                                val nameList: ArrayList<String> = arrayListOf()
                                for (tip in tipList!!) {
                                    nameList.add(tip.name)
                                }
                                LogUtils.d("robinTest nameList:" + nameList.size)
                                val adapter = ArrayAdapter(
                                    mActivity,
                                    android.R.layout.simple_dropdown_item_1line,
                                    nameList
                                )
                                mDataBinding.etPosition.setAdapter(adapter)
                            }

                        }
                    })
                    inputTips.requestInputtipsAsyn()
                }

            }
        })
    }

    override fun lazyLoadData() {
        super.lazyLoadData()
        mPoiSearch.searchPOIAsyn()
    }

    override fun onDestroy() {
        super.onDestroy()
        mHandler.removeCallbacksAndMessages(null)
    }

    private fun moveMap(pointList: ArrayList<LatLng>) {
        val builder = LatLngBounds.Builder()
        for (point in pointList) {
            builder.include(point)
        }
        val bounds = builder.build()
        mAMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, 20))
    }

    private fun rotatePoint(marker: Marker) {
//        val animation: Animation =
//            RotateAnimation(marker.rotateAngle, marker.rotateAngle + 360, 0f, 0f, 0f)
        val animation = ScaleAnimation(0f, 1f, 0f, 1f)
        val duration = 500L
        animation.setDuration(duration)
        animation.setInterpolator(LinearInterpolator())

        marker.setAnimation(animation)
        marker.startAnimation()
    }

    //点平滑移动
    private fun smoothMove(pointList: ArrayList<LatLng>, marker: Marker) {
        val points: MutableList<LatLng> = pointList
        val bounds = LatLngBounds(points[0], points[points.size - 2])
        mAMap.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, 50))

        val movingPointOverlay = MovingPointOverlay(mAMap, marker)

        movingPointOverlay.setPoints(points)

        // 设置滑动的总时间
        movingPointOverlay.setTotalDuration(40)
        // 开始滑动
        movingPointOverlay.startSmoothMove()
    }

    private fun addMarker(latLng: LatLng){
        val markerOptions = MarkerOptions()
            .position(latLng)
            .title("")
            .snippet("")
            .icon(
                BitmapDescriptorFactory.fromBitmap(
                    BitmapFactory.decodeResource(
                        resources,
                        R.drawable.ic_hospital
                    )
                )
            )
        mAMap.addMarker(markerOptions)
    }
}