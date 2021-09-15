package com.robin.mapdemo.ui.view_pager

import android.util.Log
import android.view.View
import androidx.annotation.NonNull
import androidx.viewpager.widget.ViewPager
import com.blankj.utilcode.util.LogUtils

class StackPageTransformer(private val viewPager: ViewPager) : ViewPager.PageTransformer {
    private val SCALE_VALUE = 1f

    //View 之间的偏移量
    private val DEVIATION = 60f

    //旋转
    private val ROTATION = 60f

    //图片是否叠加【默认不叠加】
    private val isStack = false
    override fun transformPage(@NonNull view: View, position: Float) {
        Log.i("robinTest", position.toString() + "")

        /*
         * 当不滑动状态下:
         *      position = -1 左侧View
         *      position = 0 当前View
         *      position = 1 右侧View
         *
         * 当滑动状态下:
         *  向左滑动: [ position < 0 && position > -1]
         *    左侧View      position < -1
         *    当前View    0 ~ -1
         *    右侧View   1 ~ 0
         *
         * 向右滑动:[position > 0 && position < 1 ]
         *   左侧View  -1 < position < 0
         *   当前View  0 ~ 1
         *   右侧View  position > 1
         */
        val pageWidth = viewPager.width
        LogUtils.d("robinTest,pageWidth:$pageWidth")

        //隐藏左侧侧的view
        if (position <= -1.0f) {
            view.visibility = View.GONE
        } else {
            view.visibility = View.VISIBLE
        }

        //当前View和右侧的View [让右侧View和当前View叠加起来]
        if (position >= 0) {
            val translationX: Float
            //这里不要晕! 改变isStack来看看效果吧!!
            translationX = if (isStack) {
                DEVIATION - pageWidth * position
            } else {
                (DEVIATION - pageWidth) * position
            }
            Log.i("robinTest", translationX.toString() + "")
            view.translationX = translationX
        }

        //当前view
        if (position == 0f) {
            view.scaleX = SCALE_VALUE
            view.scaleY = SCALE_VALUE
        } else {
            //左侧已经隐藏了，所以这里值的是右侧View的偏移量
            val scaleFactor = Math.min(SCALE_VALUE - position * 0.1f, SCALE_VALUE)
            view.scaleX = scaleFactor
            view.scaleY = scaleFactor
        }

        //向左滑动
        if (position < 0 && position > -1) {
            //旋转
            view.rotation = ROTATION * position
            view.alpha = 1 - Math.abs(position)
        } else {
            //透明度 其他状态不设置透明度
            view.alpha = 1f
        }

        //向右滑动
        if (position > 0 && position < 1) {
            view.rotation = 0f
        }
    }
}