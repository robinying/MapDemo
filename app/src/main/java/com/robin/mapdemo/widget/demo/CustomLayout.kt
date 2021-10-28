package com.robin.mapdemo.widget.demo

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.ViewConfiguration
import android.widget.RelativeLayout
import androidx.constraintlayout.widget.ConstraintLayout
import com.robin.jetpackmvvm.util.LogUtils
import java.lang.Math.abs
import java.lang.Math.sqrt

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


    /*
    * public boolean dispatchTouchEvent(MotionEvent event){

        boolean consume;
        if (evevt.getAction==MotionEvent.ACTION_DOWN||mFirstTarget!=null ){
            if (!requstdisallow){
                consume =onInterceptTouchEvent(event);
            }
        }
        if (consume){
            //不再将事件分发给子View，执行自己的onTouchEvent事件
        }else {
             //如果这时候child.dispatchTouchEvent(event)为true，那么mFirstTarget将被赋值不为null;
             //下次move事件来的时候回走进onInterceptTouchEvent(event)
            return child.dispatchTouchEvent(event) ;
        }
        //执行自己的OnTouch事件（指的是冒泡处理，在不拦截的时候，事件会优先分发给子View，
        //子View若不处理，会交给父View的onTouchEvent处理,如果拦截了，就会走自己的onTouchEvent(event)）

        return onTouchEvent(event);
        //......冒泡......//
        //......冒泡......//
        //......冒泡......//
    }
    你不拦截事件，自己的ontouchevent事件也会被执行，因为只要你的子View不消耗事件，事件就会被冒泡处理。
    你拦截了事件，事件将不分发给子View，同时，自己的OnTouchEvent事件会被执行。
    * */
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
                    "abs x:$dx --abs y:$dy  ---move distance:" + sqrt((dx * dx + dy * dy).toDouble())
                )
                intercept = (dy > dx) && sqrt((dx * dx + dy * dy).toDouble()) > mTouchSlop
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