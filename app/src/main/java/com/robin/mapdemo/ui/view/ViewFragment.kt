package com.robin.mapdemo.ui.view

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.Message
import com.robin.jetpackmvvm.base.viewmodel.BaseViewModel
import com.robin.jetpackmvvm.util.LogUtils
import com.robin.mapdemo.app.base.BaseVBFragment
import com.robin.mapdemo.databinding.FragmentViewBinding

//Test View ontouch
class ViewFragment : BaseVBFragment<BaseViewModel, FragmentViewBinding>() {

    private val mHandler = Handler(Looper.getMainLooper()) {
        LogUtils.debugInfo("Handler", "run handler message:" + it.what)
        true
    }

    override fun initView(savedInstanceState: Bundle?) {
        for (i in 1..10) {
            val msg = Message.obtain()
            msg.what = i
            mHandler.sendMessageDelayed(msg, 2000)
        }

    }
}