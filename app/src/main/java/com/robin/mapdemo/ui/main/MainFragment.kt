package com.robin.mapdemo.ui.main

import android.Manifest
import android.animation.ValueAnimator
import android.app.RecoverableSecurityException
import android.content.ContentValues
import android.content.Context
import android.content.pm.PackageManager
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
import android.content.IntentSender
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.net.Uri
import android.os.*
import android.provider.MediaStore
import android.provider.Settings
import android.util.Log
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import com.blankj.utilcode.util.FileUtils
import com.robin.mapdemo.service.ForegroundCoreService
import com.robin.mapdemo.ui.test.TestTransDataActivity
import com.robin.mapdemo.ui.usb.USBCameraActivity
import com.robin.mapdemo.util.DatetimeUtil
import java.io.*


class MainFragment : BaseFragment<MainViewModel, FragmentMainBinding>() {
    private val REQUEST_CODE_PERMISSIONS = 0xff1
    private val REQUIRED_PERMISSIONS = arrayOf(
        Manifest.permission.READ_PHONE_STATE,
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.WRITE_EXTERNAL_STORAGE,
        Manifest.permission.CAMERA
    )

    private lateinit var contractsLauncher: ActivityResultLauncher<Intent>

    override fun layoutId(): Int {
        return R.layout.fragment_main
    }

    override fun initView(savedInstanceState: Bundle?) {
        contractsLauncher =
            mActivity.registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { activityResult ->
                //查询是否开启成功
                if (mActivity.queryBatteryOptimizeStatus()) {
                    //忽略电池优化开启成功
                } else {
                    //开启失败
                }
            }
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
        mDataBinding.tvCamera.clickNoRepeat {
            nav().navigateAction(R.id.action_main_to_cameraFragment)

        }
        mDataBinding.tvUsb.clickNoRepeat {
            nav().navigateAction(R.id.action_main_to_usbFragment)
//            val intent = Intent(mActivity, USBCameraActivity::class.java)
//            mActivity.startActivity(intent)


        }
        if (!allPermissionsGranted()) {
            AndPermission.with(this)
                .runtime()
                .permission(
                    Permission.Group.PHONE,
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
        val intent = Intent(mActivity, ForegroundCoreService::class.java)
        mActivity.startService(intent)
        mViewModel.activeArcFace()
    }

    private fun startBatteryOptimize() {
        val intent = Intent(Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS)
        intent.data = Uri.parse("package:${mActivity.packageName}")
        //启动忽略电池优化，会弹出一个系统的弹框，我们在上面的
        contractsLauncher.launch(intent)
    }


    private fun Context.queryBatteryOptimizeStatus(): Boolean {
        val powerManager = getSystemService(Context.POWER_SERVICE) as PowerManager?
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            powerManager?.isIgnoringBatteryOptimizations(packageName) ?: false
        } else {
            true
        }
    }


}