package com.robin.mapdemo.ui.baidu

import android.os.Bundle
import com.baidu.mapapi.map.BaiduMap
import com.baidu.mapapi.map.MapPoi
import com.baidu.mapapi.map.MapView
import com.baidu.mapapi.model.LatLng
import com.baidu.mapapi.search.route.*
import com.blankj.utilcode.util.CollectionUtils
import com.blankj.utilcode.util.GsonUtils
import com.blankj.utilcode.util.LogUtils
import com.blankj.utilcode.util.ToastUtils
import com.robin.jetpackmvvm.base.viewmodel.BaseViewModel
import com.robin.mapdemo.R
import com.robin.mapdemo.app.badiu.BaiduLifecycleObserver
import com.robin.mapdemo.app.base.BaseFragment
import com.robin.mapdemo.databinding.FragmentBaiduRouteBinding
import com.robin.mapdemo.widget.overlayutil.BikingRouteOverlay
import com.robin.mapdemo.widget.overlayutil.WalkingRouteOverlay
import com.baidu.mapapi.bikenavi.adapter.IBEngineInitListener

import com.baidu.mapapi.bikenavi.BikeNavigateHelper





class BaiduRouteFragment : BaseFragment<BaiduRouteViewModel, FragmentBaiduRouteBinding>(),
    BaiduMap.OnMapClickListener {
    private lateinit var mMapView: MapView
    private lateinit var mBaiduMap: BaiduMap
    private lateinit var mSearch: RoutePlanSearch
    override fun layoutId(): Int {
        return R.layout.fragment_baidu_route
    }

    override fun initView(savedInstanceState: Bundle?) {
        mMapView = mDataBinding.mapView
        mBaiduMap = mMapView.map
        lifecycle.addObserver(BaiduLifecycleObserver(mMapView))
        mBaiduMap.setOnMapClickListener(this)
        mSearch = RoutePlanSearch.newInstance()
        mSearch.setOnGetRoutePlanResultListener(object : OnGetRoutePlanResultListener {
            override fun onGetWalkingRouteResult(walkingRouteResult: WalkingRouteResult?) {
                val overlay = WalkingRouteOverlay(mBaiduMap)
                LogUtils.d("robinTest walkingRouteResult:${GsonUtils.toJson(walkingRouteResult)}")
                LogUtils.d("robinTest walkingRoute list:" + walkingRouteResult?.routeLines)
                if (CollectionUtils.isNotEmpty(walkingRouteResult?.routeLines)) {
                    //????????????????????????,(?????????????????????????????????)
                    //???WalkingRouteOverlay????????????????????????
                    overlay.setData(walkingRouteResult!!.routeLines[0])
                    //??????????????????WalkingRouteOverlay
                    overlay.addToMap()
                    overlay.zoomToSpan()
                }
            }

            override fun onGetTransitRouteResult(transitRouteResult: TransitRouteResult?) {

            }

            override fun onGetMassTransitRouteResult(massTransitRouteResult: MassTransitRouteResult?) {

            }

            override fun onGetDrivingRouteResult(drivingRouteResult: DrivingRouteResult?) {

            }

            override fun onGetIndoorRouteResult(indoorRouteResult: IndoorRouteResult?) {

            }

            override fun onGetBikingRouteResult(bikingRouteResult: BikingRouteResult?) {
                val bikingRouteOverlay = BikingRouteOverlay(mBaiduMap)
                if (CollectionUtils.isNotEmpty(bikingRouteResult?.routeLines)) {
                    bikingRouteOverlay.setData(bikingRouteResult!!.routeLines[0])
                    bikingRouteOverlay.addToMap()
                    bikingRouteOverlay.zoomToSpan()
                }

            }

        })
        BikeNavigateHelper.getInstance().initNaviEngine(mActivity, object : IBEngineInitListener {
            override fun engineInitSuccess() {
                //??????????????????????????????????????????
                //routePlanWithParam()
            }

            override fun engineInitFail() {
                //??????????????????????????????????????????
            }
        })
    }

    override fun lazyLoadData() {
        super.lazyLoadData()
    }

    override fun onDestroy() {
        super.onDestroy()
        mSearch.destroy()
    }

    override fun onMapClick(latLng: LatLng?) {
        if (mViewModel.setType == 0) {
            mViewModel.stLatLng = latLng
            mViewModel.setType = 1
            ToastUtils.showShort("???????????????")
        } else {
            mViewModel.edLatLng = latLng
            mViewModel.setType = 0
            ToastUtils.showShort("???????????????")
            //searchWalkRoute(mViewModel.stLatLng, mViewModel.edLatLng)
            searchBikeRoute(mViewModel.stLatLng, mViewModel.edLatLng, 0)
        }

    }

    override fun onMapPoiClick(mapPoi: MapPoi?) {

    }

    private fun searchWalkRoute(start: LatLng?, end: LatLng?) {
        if (start == null || end == null) {
            return
        }
        val stNode = PlanNode.withLocation(start!!)
        val enNode = PlanNode.withLocation(end!!)
        mSearch.walkingSearch(WalkingRoutePlanOption().from(stNode).to(enNode))
    }

    private fun searchBikeRoute(start: LatLng?, end: LatLng?, bikeType: Int) {
        if (start == null || end == null) {
            return
        }
        val stNode = PlanNode.withLocation(start!!)
        val enNode = PlanNode.withLocation(end!!)
        // ridingType  0 ???????????????1 ???????????????
        mSearch.bikingSearch(BikingRoutePlanOption().from(stNode).to(enNode).ridingType(bikeType))
    }
}