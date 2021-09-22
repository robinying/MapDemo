package com.robin.mapdemo.ui.amap

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.angcyo.tablayout.delegate2.ViewPager2Delegate
import com.blankj.utilcode.util.LogUtils
import com.robin.jetpackmvvm.base.viewmodel.BaseViewModel
import com.robin.mapdemo.R
import com.robin.mapdemo.app.base.BaseFragment
import com.robin.mapdemo.databinding.FragmentAmapViewPagerBinding

class AmapViewPagerFragment : BaseFragment<BaseViewModel, FragmentAmapViewPagerBinding>() {
    private val fragmentList: MutableList<Fragment> = arrayListOf()
    override fun layoutId(): Int {
        return R.layout.fragment_amap_view_pager
    }

    override fun initView(savedInstanceState: Bundle?) {
        fragmentList.add(AmapPoiFragment())
        fragmentList.add(AmapRouteFragment())
        fragmentList.add(AmapToolFragment())
        fragmentList.add(AmapWeatherFragment())
        fragmentList.add(AmapOtherFragment())
        LogUtils.d("robinTest AmapViewPagerFragment fragment size:"+fragmentList.size)
        mDataBinding.let { binding ->
            binding.viewPager.let {
                it.isUserInputEnabled = false
                it.offscreenPageLimit = fragmentList.size
                it.adapter = object : FragmentStateAdapter(this) {
                    override fun createFragment(position: Int): Fragment {
                        return fragmentList[position]
                    }

                    override fun getItemCount() = fragmentList.size
                }

            }
            ViewPager2Delegate.install(binding.viewPager, binding.tabLayout)
        }
    }
}