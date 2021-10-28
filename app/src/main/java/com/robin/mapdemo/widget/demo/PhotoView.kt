package com.robin.mapdemo.widget.demo

import android.content.Context
import android.content.res.TypedArray
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.View
import com.robin.mapdemo.R

class PhotoView(context: Context, attrs: AttributeSet, defStyleAttr: Int = 0) :
    View(context, attrs, defStyleAttr) {
    private var mBitmap: Bitmap? = null
    private val mPaint by lazy { Paint(Paint.ANTI_ALIAS_FLAG) }

    init {
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.PhotoView)
        val drawable: Drawable? = typedArray.getDrawable(R.styleable.PhotoView_pv_img)
        mBitmap = if (drawable == null)
            BitmapFactory.decodeResource(context.resources, R.drawable.error)
        else
            toBitmap(drawable, 800, 800)
        typedArray.recycle()
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        canvas?.let {
            it.drawBitmap(mBitmap!!, 0f, 0f, mPaint)
        }
    }

    private fun toBitmap(drawable: Drawable, width: Int, height: Int): Bitmap {
        val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        drawable.setBounds(0, 0, canvas.width, canvas.height)
        drawable.draw(canvas)
        return bitmap
    }
}