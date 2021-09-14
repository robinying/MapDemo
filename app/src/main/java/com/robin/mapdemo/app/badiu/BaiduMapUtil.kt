package com.robin.mapdemo.app.badiu

import android.content.Context
import android.graphics.Color
import android.util.Log
import com.baidu.mapapi.map.*
import com.baidu.mapapi.model.LatLng
import com.baidu.mapapi.model.LatLngBounds
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream

object BaiduMapUtil {
    /**
     * 绘制历史轨迹
     */
    fun drawHistoryTrack(
        baiduMap: BaiduMap,
        points: List<LatLng?>?,
        customTexture: BitmapDescriptor? = null
    ): Overlay? {
        // 绘制新覆盖物前，清空之前的覆盖物
        //baiduMap.clear()
        var polylineOverlay: Overlay? = null
        if (points == null || points.isEmpty()) {
            return polylineOverlay
        }
//        val startPoint: LatLng? = points[0]
//        val endPoint: LatLng? = points[points.size - 1]
//
//
//        // 添加起点图标
//        val startOptions: OverlayOptions = MarkerOptions()
//            .position(startPoint).icon(BitmapUtil.bmStart)
//            .zIndex(9).draggable(true)
//        // 添加终点图标
//        val endOptions: OverlayOptions = MarkerOptions().position(endPoint)
//            .icon(bmEnd).zIndex(9).draggable(true)

        // 添加路线（轨迹）
        val polylineOptions: OverlayOptions = if (customTexture != null) {
            PolylineOptions().width(10)
                .color(Color.BLUE).customTexture(customTexture).points(points).dottedLine(true)
        } else {
            PolylineOptions().width(10)
                .color(Color.BLUE).points(points)
        }
        polylineOverlay = baiduMap.addOverlay(polylineOptions)
        animateMapStatus(baiduMap, points)
        return polylineOverlay
    }

    fun animateMapStatus(baiduMap: BaiduMap, points: List<LatLng?>?) {
        if (null == points || points.isEmpty()) {
            return
        }
        val builder = LatLngBounds.Builder()
        for (point in points) {
            builder.include(point)
        }
        val msUpdate = MapStatusUpdateFactory.newLatLngBounds(builder.build())
        baiduMap.animateMapStatus(msUpdate)
    }

    fun animateMapStatus(baiduMap: BaiduMap, point: LatLng, zoom: Float) {
        val builder = MapStatus.Builder()
        var mapStatus: MapStatus = builder.target(point).zoom(zoom).build()
        baiduMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(mapStatus))
    }

    private fun setMapStatus(baiduMap: BaiduMap, point: LatLng, zoom: Float) {
        val builder = MapStatus.Builder()
        var mapStatus: MapStatus = builder.target(point).zoom(zoom).build()
        baiduMap.setMapStatus(MapStatusUpdateFactory.newMapStatus(mapStatus))
    }

    fun refresh(baiduMap: BaiduMap) {
        val mapCenter: LatLng = baiduMap.mapStatus.target
        val mapZoom: Float = baiduMap.mapStatus.zoom - 1.0f
        setMapStatus(baiduMap, mapCenter, mapZoom)
    }


    fun getCustomStyleFilePath(
        context: Context,
        customStyleFileName: String
    ): String? {
        var outputStream: FileOutputStream? = null
        var inputStream: InputStream? = null
        var parentPath: String? = null
        try {
            inputStream = context.assets.open("customConfigdir/$customStyleFileName")
            val buffer = ByteArray(inputStream.available())
            inputStream.read(buffer)
            parentPath = context.filesDir.absolutePath
            val customStyleFile = File("$parentPath/$customStyleFileName")
            if (customStyleFile.exists()) {
                customStyleFile.delete()
            }
            customStyleFile.createNewFile()
            outputStream = FileOutputStream(customStyleFile)
            outputStream.write(buffer)
        } catch (e: IOException) {
            Log.e("BaiduMap", "Copy custom style file failed", e)
        } finally {
            try {
                inputStream?.close()
                outputStream?.close()
            } catch (e: IOException) {
                Log.e("BaiduMap", "Close stream failed", e)
                return null
            }
        }
        return "$parentPath/$customStyleFileName"
    }

//    /**
//     * 将地图坐标转换轨迹坐标
//     *
//     * @param latLng
//     *
//     * @return
//     */
//    fun convertMap2Trace(latLng: LatLng): com.baidu.trace.model.LatLng? {
//        return com.baidu.trace.model.LatLng(latLng.latitude, latLng.longitude)
//    }
//
//    /**
//     * 将轨迹坐标对象转换为地图坐标对象
//     *
//     * @param traceLatLng
//     *
//     * @return
//     */
//    fun convertTrace2Map(traceLatLng: com.baidu.trace.model.LatLng): LatLng? {
//        return LatLng(traceLatLng.latitude, traceLatLng.longitude)
//    }
}