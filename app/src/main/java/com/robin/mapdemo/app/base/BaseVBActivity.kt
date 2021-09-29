package com.robin.mapdemo.app.base

import android.app.Activity
import android.content.res.Resources
import android.os.Bundle
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.viewbinding.ViewBinding
import com.robin.jetpackmvvm.base.viewmodel.BaseViewModel
import com.robin.jetpackmvvm.ext.getAppViewModel
import com.robin.jetpackmvvm.ext.getVmClazz
import com.robin.jetpackmvvm.network.manager.NetState
import com.robin.jetpackmvvm.network.manager.NetworkStateManager
import com.robin.jetpackmvvm.util.inflateBindingWithGeneric
import com.robin.mapdemo.app.event.AppViewModel
import com.robin.mapdemo.app.event.EventViewModel
import com.robin.mapdemo.app.ext.dismissLoadingExt
import com.robin.mapdemo.app.ext.showLoadingExt
import me.jessyan.autosize.AutoSizeCompat

abstract class BaseVBActivity<VM : BaseViewModel, VB : ViewBinding> : AppCompatActivity(){

    //Application全局的ViewModel，里面存放了一些账户信息，基本配置信息等
    val appViewModel: AppViewModel by lazy { getAppViewModel<AppViewModel>() }

    //Application全局的ViewModel，用于发送全局通知操作
    val eventViewModel: EventViewModel by lazy { getAppViewModel<EventViewModel>() }

    lateinit var mViewModel: VM

    lateinit var mActivity: AppCompatActivity

    lateinit var binding: VB


    abstract fun initView(savedInstanceState: Bundle?)

    open fun showLoading(message: String = "请求网络中..."){
        showLoadingExt(message)
    }

    open fun dismissLoading(){
        dismissLoadingExt()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mActivity = this
        binding = inflateBindingWithGeneric(layoutInflater)
        setContentView(binding.root)
        init(savedInstanceState)
    }

    private fun init(savedInstanceState: Bundle?) {
        mViewModel = createViewModel()
        registerUiChange()
        initView(savedInstanceState)
        createObserver()
        NetworkStateManager.instance.mNetworkStateCallback.observe(this, Observer {
            onNetworkStateChanged(it)
        })
    }

    /**
     * 网络变化监听 子类重写
     */
    open fun onNetworkStateChanged(netState: NetState) {}

    /**
     * 创建viewModel
     */
    private fun createViewModel(): VM {
        return ViewModelProvider(this).get(getVmClazz(this))
    }
    /**
     * 创建LiveData数据观察者
     */
    open fun createObserver(){}

    /**
     * 注册 UI 事件
     */
    private fun registerUiChange() {
        //显示弹窗
        mViewModel.loadingChange.showDialog.observe(this, Observer {
            showLoading(it)
        })
        //关闭弹窗
        mViewModel.loadingChange.dismissDialog.observe(this, Observer {
            dismissLoading()
        })
    }

    /**
     * 将非该Activity绑定的ViewModel添加 loading回调 防止出现请求时不显示 loading 弹窗bug
     * @param viewModels Array<out BaseViewModel>
     */
    protected fun addLoadingObserve(vararg viewModels: BaseViewModel){
        viewModels.forEach {viewModel ->
            //显示弹窗
            viewModel.loadingChange.showDialog.observe(this, Observer {
                showLoading(it)
            })
            //关闭弹窗
            viewModel.loadingChange.dismissDialog.observe(this, Observer {
                dismissLoading()
            })
        }
    }


    /**
     * 在任何情况下本来适配正常的布局突然出现适配失效，适配异常等问题，只要重写 Activity 的 getResources() 方法
     */
    override fun getResources(): Resources {
        if (Looper.myLooper() != Looper.getMainLooper()) {
            return super.getResources()
        }
        AutoSizeCompat.autoConvertDensityOfGlobal(super.getResources())
        return super.getResources()
    }
}