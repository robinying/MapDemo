package com.robin.mapdemo.ui.tab

import android.os.Bundle
import com.robin.jetpackmvvm.base.viewmodel.BaseViewModel
import com.robin.mapdemo.R
import com.robin.mapdemo.app.base.BaseFragment
import com.robin.mapdemo.databinding.FragmentTabViewBinding

class TabViewFragment(private val content: String) :
    BaseFragment<BaseViewModel, FragmentTabViewBinding>() {
    override fun layoutId(): Int {
        return R.layout.fragment_tab_view
    }

    override fun initView(savedInstanceState: Bundle?) {
        mDataBinding.tvContent.text = content

    }
}