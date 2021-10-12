package com.robin.mapdemo.widget.demo

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Camera
import android.graphics.Canvas
import android.graphics.Matrix
import android.opengl.ETC1.getHeight

import android.opengl.ETC1.getWidth
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.animation.BounceInterpolator
import android.widget.FrameLayout
import androidx.annotation.NonNull
import androidx.annotation.Nullable


class TouchRotateLayout(
    @NonNull context: Context,
    @Nullable attrs: AttributeSet?,
    defStyleAttr: Int = 0
) :
    FrameLayout(context, attrs, defStyleAttr) {
    private val mCamera: Camera = Camera()
    private val mMatrix: Matrix = Matrix()
    private var mRotateX = 0f
    private var mRotateY = 0f
    private lateinit var recoverAnimator: ValueAnimator
    private var mUpRotateX = 0f
    private var mUpRotateY = 0f

    constructor(@NonNull context: Context) : this(context, null) {}
    constructor(@NonNull context: Context, @Nullable attrs: AttributeSet?) : this(
        context,
        attrs,
        0
    ) {
    }

    private fun init() {
        recoverAnimator = ValueAnimator.ofFloat(1f, 0f)
        recoverAnimator.duration = 500
        recoverAnimator.interpolator = BounceInterpolator()
        recoverAnimator.addUpdateListener { valueAnimator ->
            val percent = valueAnimator.animatedValue as Float
            mRotateX = mUpRotateX * percent
            mRotateY = mUpRotateY * percent
            postInvalidate()
        }
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                //如果在触摸的时候上一个复原动画还没执行完毕则先将动画取消
                if (recoverAnimator != null) {
                    recoverAnimator.cancel()
                }
                calcRotateXY(event.x, event.y, false)
            }
            MotionEvent.ACTION_MOVE -> calcRotateXY(event.x, event.y, false)
            MotionEvent.ACTION_UP -> {
                calcRotateXY(event.x, event.y, true)
                //手指抬起的时候进行复原
                recover()
            }
        }
        return true
    }

    /**
     * 复原角度
     */
    private fun recover() {
        recoverAnimator.start()
    }

    /**
     * 根据传来的触摸点来计算旋转角度
     * @param x
     * @param y
     */
    private fun calcRotateXY(x: Float, y: Float, isUp: Boolean) {
        //算出整个控件宽高的一半
        val halfWidth = getWidth() / 2
        val halfHeight = getHeight() / 2

        //计算X轴的旋转角度
        val percentX = (x - halfWidth) / halfWidth
        mRotateX = (if (percentX > 1f) 1f else Math.max(percentX, -1f)) * MAX_DEGREES

        //计算Y轴的旋转角度
        val percentY = (y - halfHeight) / halfHeight
        mRotateY = (if (percentY > 1f) 1f else Math.max(percentY, -1f)) * MAX_DEGREES

        //当手指抬起的时候记录这个时候的X,Y轴的旋转角度，以便做复原动画
        if (isUp) {
            mUpRotateX = mRotateX
            mUpRotateY = mRotateY
        }
        //重绘
        postInvalidate()
    }

    override fun dispatchDraw(canvas: Canvas) {
        //重置Matrix
        mMatrix.reset()
        //保存canvas和camera的状态
        canvas.save()
        mCamera.save()

        //围绕XY轴旋转相应角度
        mCamera.rotateX(mRotateX)
        mCamera.rotateY(mRotateY)
        mCamera.getMatrix(mMatrix)
        //将原点移动到中心位置
        mMatrix.preTranslate(-getWidth() / 2f, -getHeight() / 2f)
        mMatrix.postTranslate(getWidth() / 2f, getHeight() / 2f)

        //操作完并且获取了Matrix后要恢复camera的状态
        mCamera.restore()

        //将操作camera产生的Matrix赋给canvas
        canvas.setMatrix(mMatrix)
        super.dispatchDraw(canvas)

        //绘制完之后要恢复canvas的状态
        canvas.restore()
    }

    companion object {
        private const val MAX_DEGREES = 30f //最大旋转角度为30度
    }

    init {
        init()
    }
}