package com.robin.mapdemo.ui.amap

import android.graphics.Color
import android.location.Location
import android.os.Bundle
import android.view.View
import com.amap.api.location.AMapLocation
import com.amap.api.maps.AMap
import com.amap.api.maps.CameraUpdateFactory
import com.amap.api.maps.MapView
import com.amap.api.maps.model.BitmapDescriptor
import com.amap.api.maps.model.BitmapDescriptorFactory
import com.amap.api.maps.model.LatLng
import com.amap.api.maps.model.MyLocationStyle
import com.blankj.utilcode.util.ConvertUtils
import com.blankj.utilcode.util.LogUtils
import com.blankj.utilcode.util.ToastUtils
import com.robin.commonUi.customview.actionbar.TitleBar
import com.robin.commonUi.util.ResUtils
import com.robin.mapdemo.R
import com.robin.mapdemo.app.amap.AmapLifecycleObserver
import com.robin.mapdemo.app.amap.AmapLocationUtil
import com.robin.mapdemo.app.base.BaseFragment
import com.robin.mapdemo.databinding.FragmentAmapBinding
import java.util.*

class AMapFragment : BaseFragment<AMapViewModel, FragmentAmapBinding>(),
    AmapLocationUtil.onCallBackListener {
    private lateinit var mAMap: AMap
    private val amapLocationUtil by lazy { AmapLocationUtil(mActivity) }
    override fun layoutId() = R.layout.fragment_amap

    override fun initView(savedInstanceState: Bundle?) {
        val actionsList = TitleBar.ActionList()
        actionsList.add(object : TitleBar.TextAction("More") {
            override fun performAction(view: View?) {
                ToastUtils.showShort("Guide")
            }
        })
        mDataBinding.titleBar.addActions(actionsList)
        val sha1 = mViewModel.sha1(mActivity)
        LogUtils.d("robinTest  sha1:$sha1")
        //必须得调用MapView.onCreate(savedInstanceState)
        mDataBinding.mapView.onCreate(savedInstanceState)
        mAMap = mDataBinding.mapView.map
        mAMap.uiSettings.isTiltGesturesEnabled = false
        mAMap.uiSettings.isRotateGesturesEnabled = false
        mAMap.uiSettings.isMyLocationButtonEnabled = false
        mAMap.uiSettings.isZoomControlsEnabled = false
        mAMap.uiSettings.isZoomGesturesEnabled = true
        amapLocationUtil.initLocation()
        amapLocationUtil.setOnCallBackListener(this)
        lifecycle.addObserver(AmapLifecycleObserver(null, mDataBinding.mapView))
        mAMap.setOnMapLoadedListener {
            LogUtils.d("robinTest", "Amap Loaded")
        }
        showMyLocation()
    }

    private fun showMyLocation() {
        val myLocationStyle = MyLocationStyle()
        myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATE)
        myLocationStyle.interval(5000) //设置连续定位模式下的定位间隔，只在连续定位模式下生效，单次定位模式下不会生效。单位为毫秒。
        myLocationStyle.showMyLocation(true)
        myLocationStyle.radiusFillColor(Color.parseColor("#660DCBED"))
        myLocationStyle.strokeWidth(ConvertUtils.dp2px(2f).toFloat())
        myLocationStyle.strokeColor(Color.parseColor("#77006688"))
        myLocationStyle.myLocationIcon(BitmapDescriptorFactory.fromResource(R.drawable.ic_pokemon_location))
        mAMap.myLocationStyle = myLocationStyle //设置定位蓝点的Style

        mAMap.isMyLocationEnabled = true // 设置为true表示启动显示定位蓝点，false表示隐藏定位蓝点并不进行定位，默认是false。

        mAMap.moveCamera(CameraUpdateFactory.zoomTo(15f))
        mAMap.setOnMyLocationChangeListener { location: Location? ->
            run {
                location?.let {
                    mViewModel.mLatLng = LatLng(it.latitude, it.longitude)
                }

            }
        }
    }

    override fun onCallBack(
        longitude: Double,
        latitude: Double,
        location: AMapLocation?,
        isSucdess: Boolean,
        address: String?
    ) {
        LogUtils.d("robinTest location:${location}")
    }

    override fun lazyLoadData() {
        super.lazyLoadData()
        amapLocationUtil.startLocation()
    }
}