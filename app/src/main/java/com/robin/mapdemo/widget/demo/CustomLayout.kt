package com.robin.mapdemo.widget.demo

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.ViewConfiguration
import android.widget.RelativeLayout
import androidx.constraintlayout.widget.ConstraintLayout
import com.robin.jetpackmvvm.util.LogUtils
import java.lang.Math.abs

class CustomLayout @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {

//    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
//        LogUtils.debugInfo("custom", "CustomLayout dispatchTouchEvent")
//        return super.dispatchTouchEvent(ev)
//    }

    private var startX = 0f
    private var startY = 0f
    private val mTouchSlop = ViewConfiguration.get(context).scaledTouchSlop


    override fun onInterceptTouchEvent(ev: MotionEvent?): Boolean {
        LogUtils.debugInfo("robinTest", "CustomLayout onInterceptTouchEvent")
        var intercept = false

        when (ev?.action) {
            MotionEvent.ACTION_DOWN -> {
                startX = ev?.x
                startY = ev?.y
                LogUtils.debugInfo(
                    "robinTest",
                    "ACTION_DOWN startX:$startX ---startY:$startY  ---TouchSlop:$mTouchSlop"
                )
            }
            MotionEvent.ACTION_MOVE -> {
                LogUtils.debugInfo(
                    "robinTest",
                    "ACTION_MOVE startX:$startX ---startY:$startY"
                )

                val dx = abs(ev?.x - startX)
                val dy = abs(ev?.y - startY)
                //横向滑动
                LogUtils.debugInfo(
                    "robinTest",
                    "abs x:$dx --abs y:$dy"
                )
                intercept = dx > dy
            }
            MotionEvent.ACTION_UP -> {
                intercept = false
            }
        }
        return intercept
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        LogUtils.debugInfo("robinTest", "CustomLayout onTouchEvent event:$event")
        return true
    }
}