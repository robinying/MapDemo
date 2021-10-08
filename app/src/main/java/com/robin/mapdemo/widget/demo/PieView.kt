package com.robin.mapdemo.widget.demo

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import android.view.animation.LinearInterpolator
import android.view.animation.RotateAnimation
import com.robin.commonUi.util.ResUtils
import com.robin.mapdemo.R

class PieView(context: Context, attributeSet: AttributeSet) : View(context, attributeSet) {

    private val mPaint = Paint(Paint.ANTI_ALIAS_FLAG)

    private var mCurAngle = 0f

    private var mWidth: Int = 0
    private var mHeight: Int = 0

    private val colors = arrayOf(Color.BLACK, Color.GREEN, Color.BLUE, Color.YELLOW, Color.CYAN)


    private val camera = Camera()
    private val rotateMatrix = Matrix()
    val anim: ValueAnimator = ValueAnimator.ofFloat(0f, -360f).apply {
        // Z 轴是逆时针，取负数，得到顺时针的旋转
        interpolator = LinearInterpolator()
        repeatCount = 5
        duration = 800

        addUpdateListener {
            invalidate()
        }
    }

    init {
        mPaint.style = Paint.Style.FILL
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        mWidth = w
        mHeight = h
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        canvas?.let {
            it.drawColor(ResUtils.getColor(R.color.colorPrimary))
            rotateMatrix.reset()
            camera.save()
            //z轴转动
            camera.rotateZ(anim.animatedValue as Float)
            camera.getMatrix(rotateMatrix)
            camera.restore()

            val halfW = mWidth / 2f
            val halfH = mHeight / 2f

            rotateMatrix.preTranslate(-halfW, -halfH)
            rotateMatrix.postTranslate(halfW, halfH)
            it.concat(rotateMatrix)
            val rectF = RectF(0f, 0f, mWidth.toFloat(), mHeight.toFloat())
            for (i in 0..4) {
                mPaint.color = colors[i]
                it.drawArc(rectF, mCurAngle, 30f, true, mPaint)
                mCurAngle += 30f
            }
        }
    }
}