package com.robin.mapdemo.ui.recycleview.fragment

import android.os.Bundle
import android.widget.TextView
import com.blankj.utilcode.util.LogUtils
import com.blankj.utilcode.util.ToastUtils
import com.drake.brv.utils.linear
import com.drake.brv.utils.setup
import com.robin.jetpackmvvm.base.viewmodel.BaseViewModel
import com.robin.mapdemo.R
import com.robin.mapdemo.app.base.BaseVBFragment
import com.robin.mapdemo.databinding.FragmentSimpleBinding
import com.robin.mapdemo.model.SimpleModel

class SimpleFragment : BaseVBFragment<BaseViewModel, FragmentSimpleBinding>() {
    override fun initView(savedInstanceState: Bundle?) {
        binding.rv.linear().setup {
            addType<SimpleModel>(R.layout.item_simple)
            onBind {
                findView<TextView>(R.id.tv_simple).text = getModel<SimpleModel>().name
            }
            R.id.tv_simple.onClick {
                ToastUtils.showShort("点击文本 $modelPosition")
            }
        }.models = getData()
        LogUtils.d("robinTest SimpleFragment initView")
    }

    private fun getData(): MutableList<SimpleModel> {
        // 在Model中也可以绑定数据
        return mutableListOf<SimpleModel>().apply {
            for (i in 0 until 9) {
                add(SimpleModel("BRV $i"))
            }
        }
    }

    override fun onResume() {
        super.onResume()
        LogUtils.d("robinTest SimpleFragment onResume")
    }

    override fun onPause() {
        super.onPause()
        LogUtils.d("robinTest SimpleFragment onPause")
    }

    override fun onStop() {
        super.onStop()
        LogUtils.d("robinTest SimpleFragment onStop")
    }

    override fun onDestroy() {
        super.onDestroy()
        LogUtils.d("robinTest SimpleFragment onDestroy")
    }
}