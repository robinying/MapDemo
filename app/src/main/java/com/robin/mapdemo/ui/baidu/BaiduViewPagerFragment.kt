package com.robin.mapdemo.ui.baidu

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.robin.jetpackmvvm.base.viewmodel.BaseViewModel
import com.robin.mapdemo.R
import com.robin.mapdemo.app.base.BaseFragment
import com.robin.mapdemo.databinding.FragmentBaiduViewPagerBinding
import it.sephiroth.android.library.bottomnavigation.BottomNavigation

class BaiduViewPagerFragment : BaseFragment<BaseViewModel, FragmentBaiduViewPagerBinding>() {
    private val fragmentList: MutableList<Fragment> = arrayListOf()
    override fun layoutId(): Int {
        return R.layout.fragment_baidu_view_pager
    }

    override fun initView(savedInstanceState: Bundle?) {
        fragmentList.add(BaiduPoiFragment())
        fragmentList.add(BaiduRouteFragment())
        fragmentList.add(BaiduToolFragment())
        mDataBinding.viewPager.let {
            it.isUserInputEnabled = false
            it.offscreenPageLimit = fragmentList.size
            it.adapter = object : FragmentStateAdapter(this) {
                override fun createFragment(position: Int): Fragment {
                    return fragmentList[position]
                }

                override fun getItemCount() = fragmentList.size
            }
//            it.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
//                override fun onPageSelected(position: Int) {
//                    super.onPageSelected(position)
//                    //mDataBinding.baiduBottom.setSelectedIndex(position, true)
//                }
//
//                override fun onPageScrolled(
//                    position: Int,
//                    positionOffset: Float,
//                    positionOffsetPixels: Int
//                ) {
//                    super.onPageScrolled(position, positionOffset, positionOffsetPixels)
//
//                }
//
//                override fun onPageScrollStateChanged(state: Int) {
//                    super.onPageScrollStateChanged(state)
//                }
//            })
        }
        mDataBinding.baiduBottom.menuItemSelectionListener = object :
            BottomNavigation.OnMenuItemSelectionListener {
            override fun onMenuItemReselect(itemId: Int, position: Int, fromUser: Boolean) {

            }

            override fun onMenuItemSelect(itemId: Int, position: Int, fromUser: Boolean) {
                when (itemId) {
                    R.id.menu_1 -> mDataBinding.viewPager.setCurrentItem(0, true)
                    R.id.menu_2 -> mDataBinding.viewPager.setCurrentItem(1, true)
                    R.id.menu_3 -> mDataBinding.viewPager.setCurrentItem(2, true)
                }
            }

        }

    }
}