package com.robin.mapdemo.ui.test


import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import com.blankj.utilcode.util.ToastUtils
import com.robin.commonUi.customview.actionbar.TitleBar
import com.robin.mapdemo.R
import com.robin.mapdemo.app.base.BaseVBFragment
import com.robin.mapdemo.databinding.FragmentTestVbBinding
import com.robin.mapdemo.widget.demo.CircleImageDrawable
import com.robin.mapdemo.widget.demo.RoundImageDrawable

class TestVBFragment : BaseVBFragment<TestVBViewModel, FragmentTestVbBinding>() {
    override fun initView(savedInstanceState: Bundle?) {
        binding.viewTitleBar.addAction(object : TitleBar.TextAction("More") {
            override fun performAction(view: View?) {
                ToastUtils.showShort("More")
            }
        })
        val bitmap = BitmapFactory.decodeResource(resources, R.drawable.ic_vp1)
        binding.ivPic.setImageDrawable(CircleImageDrawable(bitmap))
        binding.ivPic2.setImageDrawable(RoundImageDrawable(bitmap).apply {
            rx = 30f
            ry = 30f

        })
    }

    override fun lazyLoadData() {
        super.lazyLoadData()
        mViewModel.test()
        mViewModel.testXOR()
        mViewModel.getXor2()
        mViewModel.getXor3()
    }

    override fun createObserver() {
        super.createObserver()
        mViewModel.data.observe(viewLifecycleOwner, Observer {
            binding.tvContent.text = it
            binding.pieView.anim.start()

        })
    }

    override fun initData() {
        super.initData()
    }
}