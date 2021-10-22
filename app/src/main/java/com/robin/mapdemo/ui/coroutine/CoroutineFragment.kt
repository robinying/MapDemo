package com.robin.mapdemo.ui.coroutine

import android.os.Bundle
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.blankj.utilcode.util.LogUtils
import com.blankj.utilcode.util.ToastUtils
import com.robin.jetpackmvvm.base.viewmodel.BaseViewModel
import com.robin.jetpackmvvm.ext.view.clickNoRepeat
import com.robin.jetpackmvvm.state.ResultState
import com.robin.mapdemo.app.base.BaseVBFragment
import com.robin.mapdemo.databinding.FragmentCoroutineBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CoroutineFragment : BaseVBFragment<CoroutineViewModel, FragmentCoroutineBinding>() {
    //private val mCoroutineViewModel: CoroutineViewModel by viewModels()
    override fun initView(savedInstanceState: Bundle?) {
        mViewModel.curLocation = "NingBo"
        binding.tvAddArticle.clickNoRepeat {
            mViewModel.saveArticleToData()

        }
    }

    override fun createObserver() {
        super.createObserver()
//        mCoroutineViewModel.getWeather().observe(viewLifecycleOwner, Observer { resultState ->
//            when (resultState) {
//                is ResultState.Loading -> {
//                    showLoading(resultState.loadingMessage)
//                }
//                is ResultState.Success -> {
//                    dismissLoading()
//                    LogUtils.d("robinTest", "success get Data:" + resultState.data)
//                    binding.tvTemperature.text = "温度:" + resultState.data
//                }
//                is ResultState.Error -> {
//                    dismissLoading()
//                    LogUtils.d("robinTest", "error msg:" + resultState.error.errorMsg)
//                    binding.tvTemperature.text = resultState.error.errorMsg
//                }
//            }
//        })
        mViewModel.getWeatherTwo().observe(viewLifecycleOwner, Observer { resultState ->
            when (resultState) {
                is ResultState.Loading -> {
                    showLoading(resultState.loadingMessage)
                }
                is ResultState.Success -> {
                    dismissLoading()
                    LogUtils.d("robinTest", "success get Data:" + resultState.data)
                    binding.tvTemperature.text = "温度:" + resultState.data
                }
                is ResultState.Error -> {
                    dismissLoading()
                    LogUtils.d("robinTest", "error msg:" + resultState.error.errorMsg)
                    binding.tvTemperature.text = resultState.error.errorMsg
                }
            }
        })
        mViewModel.getWeatherRealTime()
            .observe(viewLifecycleOwner, Observer { resultState ->
                when (resultState) {
                    is ResultState.Loading -> {
                    }
                    is ResultState.Success -> {
//                        LogUtils.d(
//                            "robinTest",
//                            "getWeatherRealTimesuccess get Data:" + resultState.data
//                        )
                        //binding.tvTemperature.text = "温度:" + resultState.data
                    }
                    is ResultState.Error -> {
//                        LogUtils.d(
//                            "robinTest",
//                            "getWeatherRealTime error msg:" + resultState.error.errorMsg
//                        )
                        //binding.tvTemperature.text = resultState.error.errorMsg
                    }
                }
            })
        mViewModel.startTimer().observe(viewLifecycleOwner, Observer {
            //LogUtils.d("robinTest", "Timer value:$it")
        })
        mViewModel.getNews().observe(viewLifecycleOwner, Observer {
            if (it is ResultState.Success) {
                LogUtils.d("yubinTest", "news data:" + it.data)
            }
        })
        mViewModel.getNewsTwo().observe(viewLifecycleOwner, Observer {


        })
        mViewModel.getArticles().observe(viewLifecycleOwner, Observer {
            LogUtils.d("robinTest","Article Size:"+it.size)
            it.forEach {
                LogUtils.d("robinTest","Article:$it")
            }

        })
    }

    override fun lazyLoadData() {
        super.lazyLoadData()
        //mViewModel.saveArticleToData()
    }
}