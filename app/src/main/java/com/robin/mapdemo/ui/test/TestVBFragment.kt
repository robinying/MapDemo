package com.robin.mapdemo.ui.test


import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import com.blankj.utilcode.util.ToastUtils
import com.robin.commonUi.customview.actionbar.TitleBar
import com.robin.jetpackmvvm.base.viewmodel.BaseViewModel
import com.robin.mapdemo.app.base.BaseVBFragment
import com.robin.mapdemo.databinding.FragmentTestVbBinding

class TestVBFragment : BaseVBFragment<TestVBViewModel, FragmentTestVbBinding>() {
    override fun initView(savedInstanceState: Bundle?) {
        binding.viewTitleBar.addAction(object : TitleBar.TextAction("More") {
            override fun performAction(view: View?) {
                ToastUtils.showShort("More")
            }

        })
    }

    override fun lazyLoadData() {
        super.lazyLoadData()
        mViewModel.test()
    }

    override fun createObserver() {
        super.createObserver()
        mViewModel.data.observe(viewLifecycleOwner, Observer {
            binding.tvContent.text = it

        })
    }

    override fun initData() {
        super.initData()
    }
}