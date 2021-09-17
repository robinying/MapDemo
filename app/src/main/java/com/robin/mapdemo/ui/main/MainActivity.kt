package com.robin.mapdemo.ui.main

import android.os.Build
import android.os.Bundle
import androidx.activity.OnBackPressedCallback
import androidx.navigation.Navigation
import com.blankj.utilcode.util.ToastUtils
import com.robin.jetpackmvvm.base.viewmodel.BaseViewModel
import com.robin.mapdemo.R
import com.robin.mapdemo.app.base.BaseActivity
import com.robin.mapdemo.databinding.ActivityMainBinding
import android.os.IBinder
import android.util.ArrayMap
import android.util.Log
import com.blankj.utilcode.util.LogUtils
import java.lang.Exception
import java.lang.reflect.Field
import java.lang.reflect.InvocationHandler
import java.lang.reflect.Method
import java.lang.reflect.Proxy
import java.util.*
import kotlin.collections.HashMap
import android.os.Parcel
import com.blankj.utilcode.util.GsonUtils
import com.robin.mapdemo.BuildConfig
import java.lang.RuntimeException


class MainActivity : BaseActivity<BaseViewModel, ActivityMainBinding>() {
    var exitTime = 0L


    override fun layoutId(): Int {
        return R.layout.activity_main
    }

    override fun initView(savedInstanceState: Bundle?) {
        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                val navController =
                    Navigation.findNavController(this@MainActivity, R.id.host_fragment)
                if (navController.currentDestination != null && navController.currentDestination!!.id != R.id.mainFragment) {
                    navController.navigateUp()
                } else {
                    if (System.currentTimeMillis() - exitTime > 2000L) {
                        ToastUtils.showShort("请再按一次退出应用")
                        exitTime = System.currentTimeMillis()
                    } else {
                        finish()
                    }
                }
            }

        })
//        hook()
    }


//    fun hook() {
//        try {
//            // 1. 首先我们通过ServiceManager中的sCache获取到原来activity_task对应的IBinder；
//            val serviceManager = Class.forName("android.os.ServiceManager")
//            val getServiceMethod: Method =
//                serviceManager.getMethod("getService", String::class.java)
//            LogUtils.d("robinTest, getServiceMethod:$getServiceMethod")
//            val sCacheField: Field = serviceManager.getDeclaredField("sCache")
//            LogUtils.d("robinTest, sCacheField:$sCacheField")
//            sCacheField.isAccessible = true
//            val sCache = sCacheField.get(null) as Map<String, IBinder>
//            sCache.forEach { str, iBinder ->
//                LogUtils.d("robinTest, str:$str")
//            }
//            val sNewCache: MutableMap<String, IBinder>
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
//                sNewCache = ArrayMap()
//                sNewCache.putAll(sCache)
//            } else {
//                sNewCache = HashMap(sCache)
//            }
//            val activityTaskRemoteBinder = getServiceMethod.invoke(null, "package") as IBinder
//
//            // 步骤2，步骤3
//            sNewCache["package"] =
//                Proxy.newProxyInstance(serviceManager.classLoader, arrayOf<Class<*>>(
//                    IBinder::class.java
//                )
//                ) { proxy, method, args ->
//                    if ("transact" == method.name) {
//                        if (args != null && args.size > 1) {
//                            val arg = args[1]
//                            if (arg is Parcel) {
//                                val dataSize = arg.dataSize()
//                                if (dataSize > 300 * 1024) {
//                                    // TODO 报警
//                                    Log.e(
//                                        "robinTest", Log.getStackTraceString(
//                                            RuntimeException(
//                                                "[error]TransactionTooLargeException: parcel size exceed 300Kb:$dataSize"
//                                            )
//                                        )
//                                    )
//                                    if (BuildConfig.DEBUG) {
//                                        if (dataSize > 512 * 1024) {
//                                            throw RuntimeException("[error]TransactionTooLargeException: parcel size exceed 300Kb:$dataSize")
//                                        }
//                                    }
//                                }
//                            }
//                        }
//                    }
//                    LogUtils.d("robinTest args:${GsonUtils.toJson(args)}")
//                     method.invoke(activityTaskRemoteBinder, args)
//                } as IBinder
//            sCacheField.set(null, sNewCache)
//            Log.d("robinTest", "hook success")
//        } catch (e: Exception) {
//            e.printStackTrace()
//        }
//    }
}