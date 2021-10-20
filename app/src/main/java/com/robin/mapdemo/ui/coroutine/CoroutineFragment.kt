package com.robin.mapdemo.ui.coroutine

import android.os.Bundle
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.blankj.utilcode.util.LogUtils
import com.blankj.utilcode.util.ToastUtils
import com.robin.jetpackmvvm.base.viewmodel.BaseViewModel
import com.robin.jetpackmvvm.state.ResultState
import com.robin.mapdemo.app.base.BaseVBFragment
import com.robin.mapdemo.databinding.FragmentCoroutineBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CoroutineFragment : BaseVBFragment<BaseViewModel, FragmentCoroutineBinding>() {
    private val mCoroutineViewModel: CoroutineViewModel by viewModels()
    override fun initView(savedInstanceState: Bundle?) {
        mCoroutineViewModel.curLocation = "NingBo"
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
        mCoroutineViewModel.getWeatherTwo().observe(viewLifecycleOwner, Observer { resultState ->
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
        mCoroutineViewModel.getWeatherRealTime()
            .observe(viewLifecycleOwner, Observer { resultState ->
                when (resultState) {
                    is ResultState.Loading -> {
                    }
                    is ResultState.Success -> {
                        LogUtils.d(
                            "robinTest",
                            "getWeatherRealTimesuccess get Data:" + resultState.data
                        )
                        //binding.tvTemperature.text = "温度:" + resultState.data
                    }
                    is ResultState.Error -> {
                        LogUtils.d(
                            "robinTest",
                            "getWeatherRealTime error msg:" + resultState.error.errorMsg
                        )
                        //binding.tvTemperature.text = resultState.error.errorMsg
                    }
                }
            })
        mCoroutineViewModel.startTimer().observe(viewLifecycleOwner, Observer {
            LogUtils.d("robinTest", "Timer value:$it")

        })
    }
}