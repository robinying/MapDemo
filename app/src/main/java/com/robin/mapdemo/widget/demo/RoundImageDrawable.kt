package com.robin.mapdemo.widget.demo

import android.graphics.*
import android.graphics.drawable.Drawable

class RoundImageDrawable(bitmap: Bitmap) : Drawable() {
    private val mPaint: Paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val mBitmap = bitmap
    private var rectF: RectF? = null
    var rx = 20f
    var ry = 20f

    init {
        val bitmapShader = BitmapShader(
            bitmap, Shader.TileMode.CLAMP,
            Shader.TileMode.CLAMP
        )
        mPaint.shader = bitmapShader
    }

    override fun setBounds(left: Int, top: Int, right: Int, bottom: Int) {
        super.setBounds(left, top, right, bottom)
        rectF = RectF(left.toFloat(), top.toFloat(), right.toFloat(), bottom.toFloat())
    }

    override fun draw(canvas: Canvas) {
        rectF?.let { canvas.drawRoundRect(it, rx, ry, mPaint) }
    }

    override fun setAlpha(alpha: Int) {
        mPaint.alpha = alpha
    }

    override fun setColorFilter(colorFilter: ColorFilter?) {
        mPaint.colorFilter = colorFilter
    }

    override fun getOpacity(): Int {
        return PixelFormat.TRANSLUCENT
    }

    override fun getIntrinsicWidth(): Int {
        return mBitmap.width
    }

    override fun getIntrinsicHeight(): Int {
        return mBitmap.height
    }


}