package com.robin.mapdemo.widget.demo

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import android.view.animation.RotateAnimation
import com.robin.commonUi.util.ResUtils
import com.robin.mapdemo.R

class CameraTestView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private val mPaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val mCamera = Camera()
    private val mMatrix = Matrix()

    val anim:ValueAnimator = ValueAnimator.ofFloat(0f,360f).apply {
        interpolator = null
        repeatCount = 5
        duration = 800

        addUpdateListener {
            invalidate()
        }
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        val width = width.toFloat()
        val height = height.toFloat()
        canvas?.let {
            it.drawColor(ResUtils.getColor(R.color.colorPrimary))
            mPaint.color = Color.RED
            mMatrix.reset()
            mCamera.save()
//            mCamera.translate(90f, -60f, 30f)
//            mCamera.rotateX(anim.animatedValue as Float)
//            mCamera.rotateY(anim.animatedValue as Float)
            mCamera.rotateZ(anim.animatedValue as Float)
            mCamera.getMatrix(mMatrix)
            mCamera.restore()

            mMatrix.preTranslate(-width / 2, -height / 2)
            mMatrix.postTranslate(width / 2, height / 2)
            it.concat(mMatrix)
            it.drawRect(RectF(width / 3, height / 3, width * 2 / 3, height * 2 / 3), mPaint)

        }
    }
}