package com.robin.commonUi.customview

import android.animation.Animator
import android.animation.ValueAnimator
import android.animation.ValueAnimator.AnimatorUpdateListener
import android.content.Context
import android.content.res.Configuration
import android.os.Build
import android.os.Bundle
import android.os.Parcelable
import android.util.AttributeSet
import android.view.animation.Interpolator
import android.widget.FrameLayout
import android.widget.LinearLayout
import androidx.interpolator.view.animation.FastOutSlowInInterpolator
import com.robin.commonUi.R

import com.robin.commonUi.customview.ExpandableLayout.State.Companion.COLLAPSED
import com.robin.commonUi.customview.ExpandableLayout.State.Companion.COLLAPSING
import com.robin.commonUi.customview.ExpandableLayout.State.Companion.EXPANDED
import com.robin.commonUi.customview.ExpandableLayout.State.Companion.EXPANDING

/**
 * 可伸缩的布局
 *
 * @author xuexiang
 * @since 2019-11-22 13:41
 */
class ExpandableLayout @JvmOverloads constructor(context: Context?, attrs: AttributeSet? = null) :
    FrameLayout(context!!, attrs) {
    interface State {
        companion object {
            /**
             * 已收缩
             */
            const val COLLAPSED = 0

            /**
             * 收缩中
             */
            const val COLLAPSING = 1

            /**
             * 伸展中
             */
            const val EXPANDING = 2

            /**
             * 已伸展
             */
            const val EXPANDED = 3
        }
    }

    /**
     * 伸缩动画持续的时间
     */
    var duration = DEFAULT_DURATION
        private set

    /**
     * 视差效果
     */
    var parallax = 0f
        private set

    /**
     * 伸缩比率【Value between 0 (收缩) and 1 (伸展) 】
     */
    private var mExpansion = 0f

    /**
     * 伸缩方向
     */
    var orientation = 0
        private set
    /**
     * Get mExpansion mState
     *
     * @return one of [State]
     */
    /**
     * 伸缩状态
     */
    var state = 0
        private set
    private var mInterpolator: Interpolator = FastOutSlowInInterpolator()
    private var mAnimator: ValueAnimator? = null
    private var mListener: OnExpansionChangedListener? = null
    override fun onSaveInstanceState(): Parcelable? {
        val superState = super.onSaveInstanceState()
        val bundle = Bundle()
        mExpansion = if (isExpanded) 1f else 0.toFloat()
        bundle.putFloat(KEY_EXPANSION, mExpansion)
        bundle.putParcelable(KEY_SUPER_STATE, superState)
        return bundle
    }

    override fun onRestoreInstanceState(parcelable: Parcelable) {
        val bundle = parcelable as Bundle
        mExpansion = bundle.getFloat(KEY_EXPANSION)
        state = if (mExpansion == 1f) EXPANDED else COLLAPSED
        val superState = bundle.getParcelable<Parcelable>(KEY_SUPER_STATE)
        super.onRestoreInstanceState(superState)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val width = measuredWidth
        val height = measuredHeight
        val size = if (orientation == LinearLayout.HORIZONTAL) width else height
        visibility = if (mExpansion == 0f && size == 0) GONE else VISIBLE
        val expansionDelta = size - Math.round(size * mExpansion)
        if (parallax > 0) {
            val parallaxDelta = expansionDelta * parallax
            for (i in 0 until childCount) {
                val child = getChildAt(i)
                if (orientation == HORIZONTAL) {
                    var direction = -1
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1 && layoutDirection == LAYOUT_DIRECTION_RTL) {
                        direction = 1
                    }
                    child.translationX = direction * parallaxDelta
                } else {
                    child.translationY = -parallaxDelta
                }
            }
        }
        if (orientation == HORIZONTAL) {
            setMeasuredDimension(width - expansionDelta, height)
        } else {
            setMeasuredDimension(width, height - expansionDelta)
        }
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        if (mAnimator != null) {
            mAnimator!!.cancel()
        }
        super.onConfigurationChanged(newConfig)
    }

    /**
     * Convenience method - same as calling setExpanded(expanded, true)
     */
    var isExpanded: Boolean
        get() = state == EXPANDING || state == EXPANDED
        set(expand) {
            setExpanded(expand, true)
        }

    /**
     * 切换
     */
    @JvmOverloads
    fun toggle(animate: Boolean = true) {
        if (isExpanded) {
            collapse(animate)
        } else {
            expand(animate)
        }
    }

    /**
     * 伸展
     */
    @JvmOverloads
    fun expand(animate: Boolean = true) {
        setExpanded(true, animate)
    }

    /**
     * 收缩
     */
    @JvmOverloads
    fun collapse(animate: Boolean = true) {
        setExpanded(false, animate)
    }

    fun setExpanded(expand: Boolean, animate: Boolean) {
        if (expand == isExpanded) {
            return
        }
        val targetExpansion = if (expand) 1 else 0
        if (animate) {
            animateSize(targetExpansion)
        } else {
            expansion = targetExpansion.toFloat()
        }
    }

    fun setInterpolator(interpolator: Interpolator): ExpandableLayout {
        mInterpolator = interpolator
        return this
    }

    fun setDuration(duration: Int): ExpandableLayout {
        this.duration = duration
        return this
    }

    // Infer mState from previous value
    var expansion: Float
        get() = mExpansion
        set(expansion) {
            if (mExpansion == expansion) {
                return
            }
            // Infer mState from previous value
            val delta = expansion - mExpansion
            if (expansion == 0f) {
                state = COLLAPSED
            } else if (expansion == 1f) {
                state = EXPANDED
            } else if (delta < 0) {
                state = COLLAPSING
            } else if (delta > 0) {
                state = EXPANDING
            }
            visibility = if (state == COLLAPSED) GONE else VISIBLE
            mExpansion = expansion
            requestLayout()
            if (mListener != null) {
                mListener!!.onExpansionChanged(expansion, state)
            }
        }

    fun setParallax(parallax: Float): ExpandableLayout {
        var parallax = parallax
        parallax = Math.min(1f, Math.max(0f, parallax))
        this.parallax = parallax
        return this
    }

    fun setOrientation(orientation: Int): ExpandableLayout {
        require(!(orientation < 0 || orientation > 1)) { "Orientation must be either 0 (horizontal) or 1 (vertical)" }
        this.orientation = orientation
        return this
    }

    /**
     * 设置伸缩比率变化监听
     * @param listener
     * @return
     */
    fun setOnExpansionChangedListener(listener: OnExpansionChangedListener?): ExpandableLayout {
        mListener = listener
        return this
    }

    private fun animateSize(targetExpansion: Int) {
        if (mAnimator != null) {
            mAnimator!!.cancel()
            mAnimator = null
        }
        mAnimator = ValueAnimator.ofFloat(mExpansion, targetExpansion.toFloat())
        mAnimator!!.interpolator = mInterpolator
        mAnimator!!.duration = duration.toLong()
        mAnimator!!.addUpdateListener(AnimatorUpdateListener { valueAnimator ->
            expansion = valueAnimator.animatedValue as Float
        })
        mAnimator!!.addListener(ExpansionListener(targetExpansion))
        mAnimator!!.start()
    }

    /**
     * 伸缩比率变化监听
     */
    interface OnExpansionChangedListener {
        /**
         * 伸缩比率变化
         *
         * @param expansion 伸缩比率【Value between 0 (收缩) and 1 (伸展) 】
         * @param state     伸缩状态
         */
        fun onExpansionChanged(expansion: Float, state: Int)
    }

    private inner class ExpansionListener(private val targetExpansion: Int) :
        Animator.AnimatorListener {
        private var canceled = false
        override fun onAnimationStart(animation: Animator) {
            state = if (targetExpansion == 0) COLLAPSING else EXPANDING
        }

        override fun onAnimationEnd(animation: Animator) {
            if (!canceled) {
                state = if (targetExpansion == 0) COLLAPSED else EXPANDED
                expansion = targetExpansion.toFloat()
            }
        }

        override fun onAnimationCancel(animation: Animator) {
            canceled = true
        }

        override fun onAnimationRepeat(animation: Animator) {}
    }

    companion object {
        const val KEY_SUPER_STATE = "key_super_state"
        const val KEY_EXPANSION = "key_expansion"
        const val HORIZONTAL = 0
        const val VERTICAL = 1

        /**
         * 默认伸缩动画持续的时间
         */
        private const val DEFAULT_DURATION = 300
    }

    init {
        if (attrs != null) {
            val a = getContext().obtainStyledAttributes(attrs, R.styleable.ExpandableLayout)
            duration = a.getInt(R.styleable.ExpandableLayout_el_duration, DEFAULT_DURATION)
            mExpansion = if (a.getBoolean(
                    R.styleable.ExpandableLayout_el_expanded,
                    false
                )
            ) 1f else 0.toFloat()
            orientation = a.getInt(R.styleable.ExpandableLayout_android_orientation, VERTICAL)
            parallax = a.getFloat(R.styleable.ExpandableLayout_el_parallax, 1f)
            a.recycle()
            state = if (mExpansion == 0f) COLLAPSED else EXPANDED
            setParallax(parallax)
        }
    }
}