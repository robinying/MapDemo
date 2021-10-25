package com.robin.mapdemo.ui.test

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.blankj.utilcode.util.ToastUtils
import com.robin.jetpackmvvm.base.viewmodel.BaseViewModel
import com.robin.jetpackmvvm.data.model.ApiResponse
import com.robin.jetpackmvvm.ext.request
import com.robin.jetpackmvvm.network.BaseResponse
import com.robin.jetpackmvvm.util.LogUtils
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class TestVBViewModel : BaseViewModel() {

    val data = MutableLiveData<String>()

    private suspend fun getData(): ApiResponse<String> {
        delay(3000)

        return ApiResponse(100, "success", "ready data")
    }

    fun test() {
        request({ getData() }, {
            data.postValue(it)
            ToastUtils.showShort("Get Data $it")
        }, {


        }, true)
    }

    /*
    * 基于此，异或有以下特性：
    1,a^a = 0
    相同的数异或为零。
    2,a^0 = a
    任何数与零异都为其本身。
    3,a = a ^ b ^ b
    * */
    fun testXOR() {
        // 不用中间变量交换 a 和 b
        var a = 2333
        var b = 1234
        a = a xor b
        LogUtils.debugInfo("robinTest", "a:$a")
        b = a xor b
        LogUtils.debugInfo("robinTest", "b:$b")
        a = a xor b
        LogUtils.debugInfo("robinTest", "a:$a ---b:$b")

        //一个整形数组除了一个数只出现一次，其他都出现两次，求这个数？
        val ints = intArrayOf(2, 3, 4, 5, 3, 4, 5, 7, 2, 9, 0, 7, 0)
        var result = 0
        for (i in ints) {
            result = result xor i
        }

        LogUtils.debugInfo("robinTest", "result:$result")
    }

    /*
    * 一个整形数组除了两个数只出现一次，其他都出现两次，求这两个数？
    * */
    fun getXor2() {
        val ints = intArrayOf(2, 5, 2, 9, 7, 5, 6, 9)
        var num = 0
        for (i in ints) {
            num = num xor i
        }
        val list: ArrayList<Int> = ArrayList()
        for (i in ints) {
            if (list.contains(num xor i)) {
                list.remove(Integer.valueOf(num xor i))
            } else {
                list.add(num xor i)
            }
        }
        LogUtils.debugInfo(
            "robinTest",
            "list size:" + list.size + " item 0:" + list[0] + "item 1:" + list[1]
        )
    }


    /*
    * 一个整形数组除了三个数只出现一次，其他都出现两次，求这三个数？
    * */
    fun getXor3() {
        val ints = intArrayOf(2, 5, 2, 9, 7, 5, 6, 9, 8)
        var num = 0
        for (i in ints) {
            num = num xor i
        }
        val list: ArrayList<Int> = ArrayList()
        for (i in ints) {
            if (list.contains(num xor i)) {
                list.remove(Integer.valueOf(num xor i))
            } else {
                list.add(num xor i)
            }
        }
        LogUtils.debugInfo(
            "robinTest",
            "item 0:" + (list[0] xor num).toString() + " item 1:" + (list[1] xor num).toString() + " item 2:" + (list[2] xor num).toString()
        )
    }

}