package com.robin.mapdemo.ui.main

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.robin.jetpackmvvm.ext.nav
import com.robin.jetpackmvvm.ext.navigateAction
import com.robin.jetpackmvvm.ext.view.clickNoRepeat
import com.robin.mapdemo.R
import com.robin.mapdemo.app.base.BaseFragment
import com.robin.mapdemo.databinding.FragmentMainBinding
import com.yanzhenjie.permission.AndPermission
import com.yanzhenjie.permission.runtime.Permission

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
}