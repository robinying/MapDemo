package com.robin.jetpackmvvm.base.activity

import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.robin.jetpackmvvm.base.viewmodel.BaseViewModel

/**
 * 作者　: hegaojian
 * 时间　: 2019/12/12
 * 描述　: 包含ViewModel 和Databind ViewModelActivity基类，把ViewModel 和Databind注入进来了
 * 需要使用Databind的清继承它
 */
abstract class BaseVmDbActivity<VM : BaseViewModel, DB : ViewDataBinding> : BaseVmActivity<VM>() {

    lateinit var mDataBinding: DB

    override fun onCreate(savedInstanceState: Bundle?) {
        userDataBinding(true)
        super.onCreate(savedInstanceState)
    }

    /**
     * 创建DataBinding
     */
    override fun initDataBind() {
        mDataBinding = DataBindingUtil.setContentView(this, layoutId())
        mDataBinding.lifecycleOwner = this
    }
}