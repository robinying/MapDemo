package com.robin.mapdemo.app.amap

import android.content.Context
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import com.amap.api.maps.MapView

class AmapLifecycleObserver(
    private val amapLocationUtil: AmapLocationUtil?,
    private val mMapView: MapView?
) : DefaultLifecycleObserver {
    override fun onCreate(owner: LifecycleOwner) {
        super.onCreate(owner)
    }

    override fun onStart(owner: LifecycleOwner) {
        super.onStart(owner)
    }

    override fun onResume(owner: LifecycleOwner) {
        super.onResume(owner)
        mMapView?.onResume()
    }

    override fun onPause(owner: LifecycleOwner) {
        super.onPause(owner)
        mMapView?.onPause()
    }

    override fun onStop(owner: LifecycleOwner) {
        super.onStop(owner)

    }

    override fun onDestroy(owner: LifecycleOwner) {
        super.onDestroy(owner)
        amapLocationUtil?.destroyLocation()
        mMapView?.onDestroy()

    }
}