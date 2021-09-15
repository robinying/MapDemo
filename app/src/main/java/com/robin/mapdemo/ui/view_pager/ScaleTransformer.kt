package com.robin.mapdemo.ui.view_pager

import android.icu.util.UniversalTimeScale
import android.view.View
import androidx.viewpager.widget.ViewPager
import com.robin.mapdemo.R


class ScaleTransformer : ViewPager.PageTransformer {

    private val MAX_SCALE = 1.00f
    private val MIN_SCALE = 0.75f
    override fun transformPage(page: View, position: Float) {
        if (position < 1) {
            val scaleFactor: Float = MIN_SCALE + (1 - Math.abs(position)) * (MAX_SCALE - MIN_SCALE)
            page.scaleX = scaleFactor
            page.scaleY = scaleFactor
        } else {
            page.scaleX = MIN_SCALE
            page.scaleY = MIN_SCALE
        }
    }
}