package com.robin.mapdemo.ui.usb

import android.content.Context
import android.hardware.usb.UsbDevice
import android.hardware.usb.UsbManager
import android.os.Bundle
import android.os.UserManager
import android.util.Log
import com.blankj.utilcode.util.LogUtils
import com.blankj.utilcode.util.ToastUtils
import com.robin.jetpackmvvm.base.viewmodel.BaseViewModel

import com.robin.jetpackmvvm.util.usbManager
import com.robin.mapdemo.R
import com.robin.mapdemo.app.base.BaseFragment
import com.robin.mapdemo.databinding.FragmentUsbBinding

class UsbFragment : BaseFragment<BaseViewModel, FragmentUsbBinding>() {
    companion object {
        const val TAG = "UsbFragment"
    }

    override fun layoutId(): Int {
        return R.layout.fragment_usb
    }

    override fun initView(savedInstanceState: Bundle?) {
        findUSB()
        //mDataBinding.loadingView.anim.start()
        mDataBinding.cameraTestView.anim.start()
    }

    private fun findUSB() {
        //1)创建usbManager
        val usbManager = mActivity.usbManager
        //2)获取到所有设备 选择出满足的设备
        val deviceList = usbManager?.deviceList
        ToastUtils.showShort(" deviceList size:" + deviceList?.size)
        LogUtils.d(TAG + " deviceList size:" + deviceList?.size)
        val deviceIterator = deviceList?.values?.iterator();
        while (deviceIterator != null && deviceIterator.hasNext()) {
            val device = deviceIterator.next()
            LogUtils.d(TAG + " vendorID--" + device.getVendorId() + "ProductId--" + device.getProductId());
        }
    }
}