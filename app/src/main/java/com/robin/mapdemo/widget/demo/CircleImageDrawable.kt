package com.robin.mapdemo.widget.demo

import android.graphics.*
import android.graphics.drawable.Drawable

class CircleImageDrawable(bitmap: Bitmap) : Drawable() {
    private val mBitmap: Bitmap = bitmap
    private val mPaint: Paint
    private val mWidth: Int

    override fun draw(canvas: Canvas) {
        canvas.drawCircle(
            (mWidth / 2).toFloat(), (mWidth / 2).toFloat(),
            (mWidth / 2).toFloat(), mPaint
        )
    }

    override fun getIntrinsicWidth(): Int {
        return mWidth
    }

    override fun getIntrinsicHeight(): Int {
        return mWidth
    }

    override fun setAlpha(alpha: Int) {
        mPaint.alpha = alpha
    }

    override fun setColorFilter(cf: ColorFilter?) {
        mPaint.colorFilter = cf
    }

    override fun getOpacity(): Int {
        return PixelFormat.TRANSLUCENT
    }

    init {
        val bitmapShader = BitmapShader(
            bitmap, Shader.TileMode.CLAMP,
            Shader.TileMode.CLAMP
        )
        mPaint = Paint()
        mPaint.isAntiAlias = true
        mPaint.shader = bitmapShader
        mWidth = Math.min(mBitmap.width, mBitmap.height)
    }
}