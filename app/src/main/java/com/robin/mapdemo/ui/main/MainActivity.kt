package com.robin.mapdemo.ui.main

import android.os.Bundle
import androidx.activity.OnBackPressedCallback
import androidx.navigation.Navigation
import com.blankj.utilcode.util.ToastUtils
import com.robin.jetpackmvvm.base.viewmodel.BaseViewModel
import com.robin.mapdemo.R
import com.robin.mapdemo.app.base.BaseActivity
import com.robin.mapdemo.databinding.ActivityMainBinding

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

    }
}