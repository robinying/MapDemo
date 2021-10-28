package com.robin.mapdemo.ui.face

import android.graphics.*
import android.os.Bundle
import android.text.ParcelableSpan
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import android.text.style.StyleSpan
import android.util.Log
import androidx.lifecycle.Observer
import com.arcsoft.face.*
import com.arcsoft.face.enums.CompareModel
import com.arcsoft.face.enums.DetectFaceOrientPriority
import com.arcsoft.face.enums.DetectMode
import com.arcsoft.face.enums.DetectModel
import com.arcsoft.imageutil.ArcSoftImageFormat
import com.arcsoft.imageutil.ArcSoftImageUtil
import com.arcsoft.imageutil.ArcSoftImageUtilError
import com.blankj.utilcode.util.ToastUtils
import com.bumptech.glide.Glide
import com.robin.jetpackmvvm.util.LogUtils
import com.robin.mapdemo.R
import com.robin.mapdemo.app.base.BaseVBFragment
import com.robin.mapdemo.databinding.FragmentFaceBinding
import java.util.ArrayList

class FaceFragment : BaseVBFragment<FaceViewModel, FragmentFaceBinding>() {
    private val TAG = FaceFragment::class.java.simpleName
    private var faceEngine: FaceEngine? = null
    private var faceEngineCode = -1
    private var mBitmap: Bitmap? = null
    override fun initView(savedInstanceState: Bundle?) {
        initEngine()
    }

    private fun initEngine() {
        faceEngine = FaceEngine()
        faceEngineCode = faceEngine!!.init(
            mActivity,
            DetectMode.ASF_DETECT_MODE_IMAGE,
            DetectFaceOrientPriority.ASF_OP_ALL_OUT,
            16,
            10,
            FaceEngine.ASF_FACE_RECOGNITION or FaceEngine.ASF_FACE_DETECT or FaceEngine.ASF_AGE or FaceEngine.ASF_GENDER or FaceEngine.ASF_FACE3DANGLE or FaceEngine.ASF_LIVENESS
        )
        Log.i(
            TAG,
            "initEngine: init: $faceEngineCode"
        )
        if (faceEngineCode != ErrorInfo.MOK) {
            ToastUtils.showShort(String.format("引擎初始化失败，错误码为 %d", faceEngineCode))
        }
    }

    override fun lazyLoadData() {
        super.lazyLoadData()
        mBitmap = BitmapFactory.decodeResource(resources, R.drawable.faces)
        binding.ivPic.setImageBitmap(mBitmap)
        if (mBitmap != null && faceEngine != null && faceEngineCode == ErrorInfo.MOK) {
            mViewModel.analysisImage(mBitmap, faceEngine!!)
        }
    }

    override fun createObserver() {
        super.createObserver()
        mViewModel.drawBitmap.observe(viewLifecycleOwner, Observer {
            binding.ivNewPic.setImageBitmap(it)

        })
    }

    override fun onDestroy() {
        super.onDestroy()
        if (mBitmap != null && !mBitmap!!.isRecycled) {
            mBitmap!!.recycle()
        }
        mBitmap = null
        unInitEngine()
    }

    /**
     * 销毁引擎
     */
    private fun unInitEngine() {
        if (faceEngine != null) {
            faceEngineCode = faceEngine!!.unInit()
            faceEngine = null
            Log.i(
                TAG,
                "unInitEngine: $faceEngineCode"
            )
        }
    }

}