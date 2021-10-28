package com.robin.mapdemo.widget.demo

import android.animation.Animator
import android.animation.ObjectAnimator
import android.content.Context
import android.content.res.TypedArray
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.util.Log
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import com.robin.jetpackmvvm.util.LogUtils
import com.robin.mapdemo.R

class PhotoView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet,
    defStyleAttr: Int = 0
) :
    View(context, attrs, defStyleAttr) {
    private var mBitmap: Bitmap? = null
    private var offsetWidth = 0f
    private var offsetHeight = 0f

    // 缩放前图片比例
    private var smallScale = 0f

    // 缩放后图片
    private var bigScale = 0f

    // 当前比例
    private var currentScale = 0f
        set(value) {
            field = value
            invalidate()
        }
    private var moveOffsetX = 0f
    private var moveOffsetY = 0f

    // 缩放倍数
    private val ZOOM_SCALE = 1.5f
    private val mPaint by lazy { Paint(Paint.ANTI_ALIAS_FLAG) }
    private val mPhotoGestureListener by lazy { PhotoGestureListener() }
    private lateinit var gestureDetector: GestureDetector

    init {
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.PhotoView)
        val drawable: Drawable? = typedArray.getDrawable(R.styleable.PhotoView_pv_img)
        mBitmap = if (drawable == null)
            BitmapFactory.decodeResource(context.resources, R.drawable.error)
        else
            toBitmap(drawable, 600, 600)
        typedArray.recycle()
        gestureDetector = GestureDetector(context, mPhotoGestureListener)
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        //将图片移动到中心
        offsetWidth = (width / 2 - mBitmap!!.width / 2).toFloat()
        offsetHeight = (height / 2 - mBitmap!!.height / 2).toFloat()
        // view比例
        val viewScale = width.toFloat() / height.toFloat()

        // 图片比例
        val bitScale = mBitmap!!.width.toFloat() / mBitmap!!.height.toFloat()
        LogUtils.debugInfo("robinTest", "bitScale:$bitScale --viewScale:$viewScale")


        // 如果图片比例大于view比例
        if (bitScale > viewScale) {
            // 横向图片
            smallScale = width.toFloat() / mBitmap!!.width
            bigScale = height.toFloat() / mBitmap!!.height * ZOOM_SCALE
        } else {
            // 纵向图片
            smallScale = height.toFloat() / mBitmap!!.height
            bigScale = width.toFloat() / mBitmap!!.width * ZOOM_SCALE
        }

        // 当前缩放比例 = 缩放前的比例
        currentScale = smallScale
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        canvas?.let {
            it.translate(moveOffsetX, moveOffsetY)
            it.scale(currentScale, currentScale, (width / 2).toFloat(), (height / 2).toFloat())
            it.drawBitmap(mBitmap!!, offsetWidth, offsetHeight, mPaint)
        }
    }

    private fun toBitmap(drawable: Drawable, width: Int, height: Int): Bitmap {
        val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        drawable.setBounds(0, 0, canvas.width, canvas.height)
        drawable.draw(canvas)
        return bitmap
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        val result = gestureDetector.onTouchEvent(event)
        return result
    }

    inner class PhotoGestureListener : GestureDetector.SimpleOnGestureListener() {

        // 延时触发 [100ms] -- 常用与水波纹等效果
        override fun onShowPress(e: MotionEvent?) {
            super.onShowPress(e)
            Log.i("robinTest", "延时触发 onShowPress")
        }

        // 单击情况 : 抬起[ACTION_UP]时候触发
        // 双击情况 : 第二次抬起[ACTION_POINTER_UP]时候触发
        override fun onSingleTapUp(e: MotionEvent?): Boolean {
            Log.i("robinTest", "抬起了 onSingleTapUp")
            return super.onSingleTapUp(e)
        }

        // 滑动时候触发 类似 ACTION_MOVE 事件
        override fun onScroll(
            e1: MotionEvent?,
            e2: MotionEvent?,
            distanceX: Float,
            distanceY: Float,
        ): Boolean {
            Log.i("robinTest", "滑动了  onScroll distanceX:$distanceX ---distanceY:$distanceY")
            if (currentScale == bigScale) {
                moveOffsetX -= distanceX
                moveOffsetY -= distanceY
                invalidate()
            }

            // distanceX
            //      向左滑动 正数
            //      向右滑动 负数
            // distanceY
            //      向上滑动 正数
            //      向下滑动 负数


            invalidate()
//            }

            return super.onScroll(e1, e2, distanceX, distanceY)
        }

        // 长按时触发 [300ms]
        override fun onLongPress(e: MotionEvent?) {
            super.onLongPress(e)
            Log.i("robinTest", "长按了 onLongPress")
        }

        // 按下 这里必须返回true 因为所有事件都是由按下出发的
        override fun onDown(e: MotionEvent?): Boolean {
//            return super.onDown(e)
            return true
        }

        // 滑翔/飞翔 [惯性滑动]
        override fun onFling(
            e1: MotionEvent?,
            e2: MotionEvent?,
            velocityX: Float,
            velocityY: Float,
        ): Boolean {
            Log.i("robinTest", "惯性滑动 onFling")
            return super.onFling(e1, e2, velocityX, velocityY)
        }


        // 单击时触发 双击时不触发
        override fun onSingleTapConfirmed(e: MotionEvent?): Boolean {
            Log.i("robinTest", "单击了 onSingleTapConfirmed")
            return super.onSingleTapConfirmed(e)
        }

        // 双击 -- 第二次按下时候触发 (40ms - 300ms) [小于40ms是为了防止抖动]
        override fun onDoubleTap(e: MotionEvent): Boolean {
            if (currentScale == smallScale) {
                scaleAnimation(smallScale, bigScale).start()
            } else {
                scaleAnimation(bigScale, smallScale).start()
            }


            return super.onDoubleTap(e)
        }

        // 双击 第二次的事件处理 DOWN MOVE UP 都会执行到这里
        override fun onDoubleTapEvent(e: MotionEvent?): Boolean {
            Log.i("robinTest", "双击执行了 onDoubleTapEvent")
            return super.onDoubleTapEvent(e)
        }
    }

    fun scaleAnimation(start: Float = smallScale, end: Float = bigScale): ObjectAnimator = let {
        ObjectAnimator.ofFloat(this, "currentScale", start, end).also {
            // 默认时间是 300ms
            it.duration = 1000
            it.addListener(object : Animator.AnimatorListener {
                override fun onAnimationStart(animation: Animator?) {

                }

                override fun onAnimationEnd(animation: Animator?) {
                    LogUtils.debugInfo("robinTest onAnimationEnd currentScale:$currentScale")
                }

                override fun onAnimationCancel(animation: Animator?) {

                }

                override fun onAnimationRepeat(animation: Animator?) {

                }

            })

        }
    }
}