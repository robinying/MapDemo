package com.robin.mapdemo.ui.test

import android.os.Bundle
import androidx.lifecycle.Observer
import com.robin.mapdemo.app.base.BaseVBActivity
import com.robin.mapdemo.databinding.ActivityTestVbBinding


class TestVBActivity : BaseVBActivity<TestVBViewModel, ActivityTestVbBinding>() {
    override fun initView(savedInstanceState: Bundle?) {
        mViewModel.test()
    }

    override fun createObserver() {
        super.createObserver()
        mViewModel.data.observe(this, Observer {
            binding.tvContent.text = it

        })
    }

}