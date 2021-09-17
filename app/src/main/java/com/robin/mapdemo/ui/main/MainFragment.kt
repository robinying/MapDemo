package com.robin.mapdemo.ui.main

import android.Manifest
import android.animation.ValueAnimator
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Looper
import android.view.animation.AccelerateInterpolator
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.blankj.utilcode.util.LogUtils
import com.robin.commonUi.util.ResUtils
import com.robin.jetpackmvvm.ext.nav
import com.robin.jetpackmvvm.ext.navigateAction
import com.robin.jetpackmvvm.ext.view.clickNoRepeat
import com.robin.jetpackmvvm.ext.view.extraAnimClickListener
import com.robin.jetpackmvvm.ext.view.gone
import com.robin.jetpackmvvm.ext.view.setRoundRectBg
import com.robin.mapdemo.R
import com.robin.mapdemo.app.base.BaseFragment
import com.robin.mapdemo.databinding.FragmentMainBinding
import com.yanzhenjie.permission.AndPermission
import com.yanzhenjie.permission.runtime.Permission
import android.content.Intent
import com.robin.mapdemo.ui.test.TestTransDataActivity


class MainFragment : BaseFragment<MainViewModel, FragmentMainBinding>() {
    private val REQUEST_CODE_PERMISSIONS = 0xff1
    private val REQUIRED_PERMISSIONS = arrayOf(
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.WRITE_EXTERNAL_STORAGE,
        Manifest.permission.CAMERA
    )

    override fun layoutId(): Int {
        return R.layout.fragment_main
    }

    override fun initView(savedInstanceState: Bundle?) {
        mDataBinding.tvAmap.clickNoRepeat {
            nav().navigateAction(R.id.action_main_to_amapFragment)
        }
        mDataBinding.tvBaidu.setOnClickListener {
            nav().navigateAction(R.id.action_main_to_baiduFragment)
        }
        //Test setRoundRectBg Api
        mDataBinding.tvViewPager.setRoundRectBg(ResUtils.getColor(R.color.chinaHoliday), 12f)
        mDataBinding.tvViewPager.setOnClickListener {
            nav().navigateAction(R.id.action_main_to_viewPagerTestFragment)

        }
        mDataBinding.tvTabLayout.extraAnimClickListener(ValueAnimator.ofFloat(1.0f, 1.5f).apply {
            interpolator = AccelerateInterpolator()
            duration = 100
            addUpdateListener {
                mDataBinding.tvTabLayout.scaleX = it.animatedValue as Float
                mDataBinding.tvTabLayout.scaleY = it.animatedValue as Float
            }
        }) {
            nav().navigateAction(R.id.action_main_to_tabLayoutDemoFragment)
        }
        mDataBinding.tvTestTransact.clickNoRepeat {
            val intent = Intent(mActivity, TestTransDataActivity::class.java)
            //intent传值大于512k时，system进程会报错
            //system进程在跟应用交互过程中发生了异常，然后把应用kill掉了。
            intent.putExtra("large_data", ByteArray(500 * 1024))
            startActivity(intent)
        }
        if (!allPermissionsGranted()) {
            AndPermission.with(this)
                .runtime()
                .permission(
                    Permission.Group.LOCATION,
                    Permission.Group.STORAGE,
                    Permission.Group.CAMERA
                )
                .onGranted { }
                .onDenied { }
                .start()
//            ActivityCompat.requestPermissions(
//                mActivity,
//                REQUIRED_PERMISSIONS,
//                REQUEST_CODE_PERMISSIONS
//            )
        }
    }

    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(
            mActivity, it
        ) == PackageManager.PERMISSION_GRANTED
    }

    override fun lazyLoadData() {
        super.lazyLoadData()
        Looper.myQueue().addIdleHandler {
            LogUtils.d("robinTest addIdleHandler")
            false
        }
    }
}