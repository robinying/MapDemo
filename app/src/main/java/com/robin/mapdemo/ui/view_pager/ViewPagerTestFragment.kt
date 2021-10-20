package com.robin.mapdemo.ui.view_pager

import android.os.Bundle
import com.blankj.utilcode.util.ConvertUtils
import com.blankj.utilcode.util.LogUtils
import com.robin.jetpackmvvm.base.viewmodel.BaseViewModel
import com.robin.mapdemo.R
import com.robin.mapdemo.app.base.BaseFragment
import com.robin.mapdemo.databinding.FragmentViewPagerTestBinding
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class ViewPagerTestFragment : BaseFragment<BaseViewModel, FragmentViewPagerTestBinding>() {
    private val mDrawables: ArrayList<Int> =
        arrayListOf(R.drawable.ic_vp1, R.drawable.ic_vp2, R.drawable.ic_vp3)

    override fun layoutId(): Int {
        return R.layout.fragment_view_pager_test
    }

    override fun initView(savedInstanceState: Bundle?) {
        mDataBinding.viewPager.let {
            it.offscreenPageLimit = mDrawables.size
            it.adapter = BannerAdapter(mActivity, mDrawables)
            it.pageMargin = ConvertUtils.dp2px(10f)
            it.setPageTransformer(true,ScaleTransformer())
            it.currentItem = 100
        }
        mDataBinding.viewPager2.let{
            it.offscreenPageLimit = mDrawables.size
            it.adapter = BannerAdapter(mActivity, mDrawables)
            it.pageMargin = ConvertUtils.dp2px(10f)
            it.setPageTransformer(true,StackPageTransformer(it))
            it.currentItem = 100
        }
    }
}