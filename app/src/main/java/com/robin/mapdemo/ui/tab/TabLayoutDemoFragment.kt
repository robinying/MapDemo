package com.robin.mapdemo.ui.tab

import android.graphics.ColorFilter
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.airbnb.lottie.LottieAnimationView
import com.airbnb.lottie.LottieProperty
import com.airbnb.lottie.SimpleColorFilter
import com.airbnb.lottie.model.KeyPath
import com.airbnb.lottie.value.LottieValueCallback
import com.angcyo.dsladapter.L
import com.angcyo.dsladapter.dpi
import com.angcyo.tablayout.TabGradientCallback
import com.angcyo.tablayout.delegate2.ViewPager2Delegate
import com.robin.commonUi.util.ResUtils
import com.robin.jetpackmvvm.base.viewmodel.BaseViewModel
import com.robin.mapdemo.R
import com.robin.mapdemo.app.base.BaseFragment
import com.robin.mapdemo.databinding.FragmentTabLayoutDemoBinding

class TabLayoutDemoFragment : BaseFragment<BaseViewModel, FragmentTabLayoutDemoBinding>() {
    private val fragmentList: MutableList<Fragment> = arrayListOf()
    override fun layoutId(): Int = R.layout.fragment_tab_layout_demo

    override fun initView(savedInstanceState: Bundle?) {
        fragmentList.add(TabViewFragment("Play"))
        fragmentList.add(TabViewFragment("Add"))
        fragmentList.add(TabViewFragment("Home"))
        mDataBinding.viewPager.let {
            it.isUserInputEnabled = true
            it.offscreenPageLimit = fragmentList.size
            it.adapter = object : FragmentStateAdapter(this) {
                override fun createFragment(position: Int) = fragmentList[position]
                override fun getItemCount() = fragmentList.size
            }
            it.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback(){
                override fun onPageScrolled(
                    position: Int,
                    positionOffset: Float,
                    positionOffsetPixels: Int
                ) {
                    super.onPageScrolled(position, positionOffset, positionOffsetPixels)
                }

                override fun onPageSelected(position: Int) {
                    super.onPageSelected(position)
                    mDataBinding.tabLayout2.onPageSelected(position)
                    mDataBinding.tabLayout1.onPageSelected(position)
                }

                override fun onPageScrollStateChanged(state: Int) {
                    super.onPageScrollStateChanged(state)
                }

            })

        }
        mDataBinding.tabLayout1.configTabLayoutConfig {
            onSelectIndexChange = { fromIndex, selectIndexList, reselect, fromUser ->
                Log.i(
                    "angcyo",
                    "选择:[$fromIndex]->${selectIndexList} reselect:$reselect fromUser:$fromUser"
                )
            }

            //选中view的回调
            onSelectViewChange = { fromView, selectViewList, reselect, fromUser ->
                val toView = selectViewList.first()
                L.i("fromView:${fromView.hashCode()} toView:${toView.hashCode()}")
            }

        }

        mDataBinding.tabLayout2.configTabLayoutConfig {
            onGetIcoStyleView = { itemView, index ->
                when (index) {
                    0 -> itemView.findViewById<LottieAnimationView>(R.id.lottie_view1)
                    1 -> itemView.findViewById<LottieAnimationView>(R.id.lottie_view2)
                    2 -> itemView.findViewById<LottieAnimationView>(R.id.lottie_view3)
                    else -> {
                        itemView.findViewById<LottieAnimationView>(R.id.lottie_view1)
                    }
                }

            }

            onGetTextStyleView = { itemView, index ->
                when (index) {
                    0 -> itemView.findViewById(R.id.tv_tab1)
                    1 -> itemView.findViewById(R.id.tv_tab2)
                    2 -> itemView.findViewById(R.id.tv_tab3)
                    else -> {
                        itemView.findViewById(R.id.tv_tab1)
                    }
                }

            }
            tabGradientCallback = object : TabGradientCallback() {
                override fun onUpdateIcoColor(view: View?, color: Int) {
                    (view as? LottieAnimationView)?.run {
                        setLottieColorFilter(color)
                    } ?: super.onUpdateIcoColor(view, color)
                }
            }

            //选中view的回调
            onSelectViewChange = { fromView, selectViewList, reselect, fromUser ->
                val toView = selectViewList.first()
                fromView?.apply { onGetTextStyleView(this, -1)?.visibility = View.GONE }
                if (reselect) {
                    //重复选择
                } else {
                    toView.findViewById<LottieAnimationView>(R.id.lottie_view)
                        ?.playAnimation()
                    onGetTextStyleView(toView, -1)?.visibility = View.VISIBLE
                }
                L.i("fromView:${fromView.hashCode()} toView:${toView.hashCode()}")
            }
            onSelectIndexChange = { fromIndex, selectIndexList, reselect, fromUser ->
                Log.i(
                    "tabLayout2",
                    "选择:[$fromIndex]->${selectIndexList} reselect:$reselect fromUser:$fromUser"
                )
                val position = selectIndexList[0]
                mDataBinding.viewPager.setCurrentItem(position, true)
            }



        }
        //ViewPager2Delegate.install(mDataBinding.viewPager, mDataBinding.tabLayout2)
        mDataBinding.tabLayout2.updateTabBadge(1){
            badgeText="7"
            badgeSolidColor= ResUtils.getColor(R.color.chinaHoliday)
            badgeTextColor = ResUtils.getColor(R.color.white)
            badgeOffsetX = 20 * dpi
            badgeOffsetY = -20 * dpi
        }
    }


    fun LottieAnimationView.setLottieColorFilter(color: Int) {
        val filter = SimpleColorFilter(color)
        val keyPath = KeyPath("**")
        val callback = LottieValueCallback<ColorFilter>(filter)
        addValueCallback(keyPath, LottieProperty.COLOR_FILTER, callback)
    }
}