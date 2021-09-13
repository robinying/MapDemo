package com.robin.mapdemo.ui.error

import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import cat.ereza.customactivityoncrash.CustomActivityOnCrash
import com.robin.commonUi.util.ColorUtils
import com.robin.jetpackmvvm.base.viewmodel.BaseViewModel
import com.robin.jetpackmvvm.ext.view.clickNoRepeat
import com.robin.mapdemo.BuildConfig
import com.robin.mapdemo.R
import com.robin.mapdemo.app.base.BaseActivity
import com.robin.mapdemo.databinding.ActivityErrorBinding
import com.robin.mapdemo.util.StatusBarUtil

class ErrorActivity : BaseActivity<BaseViewModel, ActivityErrorBinding>() {


    override fun layoutId(): Int {
        return R.layout.activity_error
    }

    override fun initView(savedInstanceState: Bundle?) {

        supportActionBar?.setBackgroundDrawable(ColorDrawable(ColorUtils.getColor(this)))
        StatusBarUtil.setColor(this, ColorUtils.getColor(this), 0)
        val config = CustomActivityOnCrash.getConfigFromIntent(intent)
        mDataBinding.errorRestart.clickNoRepeat {
            config?.run {
                CustomActivityOnCrash.restartApplication(this@ErrorActivity, this)
            }
        }
        CustomActivityOnCrash.getStackTraceFromIntent(intent)?.let {
            if (BuildConfig.DEBUG) {
                mDataBinding.tvErrorInfo.text = it
            }
        }

    }
}