package com.robin.jetpackmvvm.ext

import android.content.Context
import android.text.TextUtils
import android.widget.Toast
import androidx.annotation.StringRes
import com.robin.jetpackmvvm.base.BaseApp
import com.robin.jetpackmvvm.base.Ktx

/**
 * des Toast工具类
 * @date 2020/5/14
 */

fun Context.toast(content: String, duration: Int = Toast.LENGTH_SHORT) {
    if (TextUtils.isEmpty(content))return
    Toast.makeText(this, content, duration).apply {
        show()
    }
}

fun Context.toast(@StringRes id: Int, duration: Int = Toast.LENGTH_SHORT) {
    toast(getString(id), duration)
}


fun toast(content: String, duration: Int = Toast.LENGTH_SHORT) {
    if (TextUtils.isEmpty(content))return
    Ktx.app.toast(content, duration)
}

fun toast(@StringRes id: Int, duration: Int= Toast.LENGTH_SHORT) {
    Ktx.app.toast(id, duration)
}

fun longToast(content: String,duration: Int= Toast.LENGTH_LONG) {
    if (TextUtils.isEmpty(content))return
    Ktx.app.toast(content,duration)
}

fun longToast(@StringRes id: Int, duration: Int= Toast.LENGTH_LONG) {
    Ktx.app.toast(id,duration)
}