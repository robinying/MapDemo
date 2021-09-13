package com.robin.mapdemo.ui.amap

import android.content.Context
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import com.amap.api.maps.model.LatLng
import com.robin.jetpackmvvm.base.viewmodel.BaseViewModel
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import java.util.*

class AMapViewModel : BaseViewModel() {
    var mLatLng: LatLng? = null

    /*
    * 获取应用签名sha1
    * */
    fun sha1(context: Context): String {
        try {
            val info: PackageInfo = context.packageManager.getPackageInfo(
                context.packageName, PackageManager.GET_SIGNATURES
            )
            val cert: ByteArray = info.signatures.get(0).toByteArray()
            val md: MessageDigest = MessageDigest.getInstance("SHA1")
            val publicKey: ByteArray = md.digest(cert)
            val hexString = StringBuffer()
            for (i in publicKey.indices) {
                val appendString = Integer.toHexString(0xFF and publicKey[i].toInt())
                    .toUpperCase(Locale.US)
                if (appendString.length == 1) hexString.append("0")
                hexString.append(appendString)
                hexString.append(":")
            }
            val result = hexString.toString()
            return result.substring(0, result.length - 1)
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        } catch (e: NoSuchAlgorithmException) {
            e.printStackTrace()
        }
        return ""
    }

}