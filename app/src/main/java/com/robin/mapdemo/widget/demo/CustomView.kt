package com.robin.mapdemo.widget.demo

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import com.robin.jetpackmvvm.util.LogUtils

class CustomView(context: Context, attrs: AttributeSet) : View(context, attrs) {

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        LogUtils.debugInfo("custom", "CustomView onTouchEvent")
        return super.onTouchEvent(event)
    }

    override fun dispatchTouchEvent(event: MotionEvent?): Boolean {
        event?.run {
            LogUtils.debugInfo("custom", "CustomView dispatchTouchEvent action:$this")

        }
        return true
    }
}