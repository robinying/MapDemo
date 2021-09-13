package com.robin.commonUi.customview

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.util.Log
import androidx.constraintlayout.widget.ConstraintLayout
import com.robin.commonUi.R

class CustomXORLayout(context: Context, attrs: AttributeSet) : ConstraintLayout(context, attrs) {
    //private val bgPaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val scanPaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val strikePaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private var radius: Float
    private var strikeSize: Float
    private var strikeColor: Int
    private var scanMarginTop: Float

    init {
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.CustomXORLayout)
        radius = typedArray.getDimension(R.styleable.CustomXORLayout_cam_scan_radius, 300f)
        strikeSize = typedArray.getDimension(R.styleable.CustomXORLayout_cam_scan_strike_size, 10f)
        strikeColor =
            typedArray.getColor(R.styleable.CustomXORLayout_cam_scan_strike_color, Color.BLUE)
        scanMarginTop =
            typedArray.getDimension(R.styleable.CustomXORLayout_cam_scan_margin_top, 30f)
        typedArray.recycle()
        strikePaint.style = Paint.Style.STROKE
        strikePaint.color = strikeColor
        strikePaint.strokeWidth = strikeSize
        scanPaint.style = Paint.Style.FILL
        scanPaint.xfermode = PorterDuffXfermode(PorterDuff.Mode.XOR)

    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        canvas?.let {
            val cy = radius + scanMarginTop
            it.drawCircle(width.toFloat() / 2, cy, radius, scanPaint)
            it.drawCircle(width.toFloat() / 2, cy, radius, strikePaint)
        }
    }


}