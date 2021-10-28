package com.robin.mapdemo.ui.face

import android.graphics.*
import android.text.ParcelableSpan
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import android.text.style.StyleSpan
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.arcsoft.face.*
import com.arcsoft.face.enums.CompareModel
import com.arcsoft.face.enums.DetectModel
import com.arcsoft.imageutil.ArcSoftImageFormat
import com.arcsoft.imageutil.ArcSoftImageUtil
import com.arcsoft.imageutil.ArcSoftImageUtilError
import com.robin.jetpackmvvm.base.viewmodel.BaseViewModel
import com.robin.jetpackmvvm.util.LogUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class FaceViewModel : BaseViewModel() {
    private val TAG = "face"

    val drawBitmap = MutableLiveData<Bitmap>()

    fun analysisImage(mBitmap: Bitmap?, faceEngine: FaceEngine) {
        viewModelScope.launch(Dispatchers.IO) {
            runCatching {
                processImage(mBitmap, faceEngine)
            }.onSuccess {


            }
        }
    }

    /**
     * 主要操作逻辑部分
     */
    private suspend fun processImage(mBitmap: Bitmap?, faceEngine: FaceEngine) {
        /**
         * 1.准备操作（校验，显示，获取BGR）
         */
//        if (mBitmap == null) {
//            mBitmap = BitmapFactory.decodeResource(resources, R.drawable.faces)
//        }
        // 图像对齐
        val bitmap = ArcSoftImageUtil.getAlignedBitmap(mBitmap, true)
        val notificationSpannableStringBuilder = SpannableStringBuilder()
        if (bitmap == null) {
            addNotificationInfo(notificationSpannableStringBuilder, null, " bitmap is null!")
            LogUtils.debugInfo(TAG, notificationSpannableStringBuilder.toString())
            return
        }
        if (faceEngine == null) {
            addNotificationInfo(notificationSpannableStringBuilder, null, " faceEngine is null!")
            LogUtils.debugInfo(TAG, notificationSpannableStringBuilder.toString())
            return
        }
        val width = bitmap.width
        val height = bitmap.height
//        runOnUiThread(Runnable {
//            Glide.with(ivShow.getContext())
//                .load(bitmap)
//                .into(ivShow)
//        })

        // bitmap转bgr24
        val start = System.currentTimeMillis()
        val bgr24 =
            ArcSoftImageUtil.createImageData(bitmap.width, bitmap.height, ArcSoftImageFormat.BGR24)
        val transformCode =
            ArcSoftImageUtil.bitmapToImageData(bitmap, bgr24, ArcSoftImageFormat.BGR24)
        if (transformCode != ArcSoftImageUtilError.CODE_SUCCESS) {
            Log.e(
                TAG,
                "transform failed, code is $transformCode"
            )
            addNotificationInfo(
                notificationSpannableStringBuilder,
                StyleSpan(Typeface.BOLD),
                "transform bitmap To ImageData failed",
                "code is ",
                transformCode.toString(),
                "\n"
            )
            LogUtils.debugInfo(TAG, notificationSpannableStringBuilder.toString())
            return
        }
        //        Log.i(TAG, "processImage:bitmapToBgr24 cost =  " + (System.currentTimeMillis() - start));
        addNotificationInfo(
            notificationSpannableStringBuilder,
            StyleSpan(Typeface.BOLD),
            "start face detection,imageWidth is ",
            width.toString(),
            ", imageHeight is ",
            height.toString(),
            "\n"
        )
        val faceInfoList: List<FaceInfo> = ArrayList()

        /**
         * 2.成功获取到了BGR24 数据，开始人脸检测
         */
        val fdStartTime = System.currentTimeMillis()
        //        ArcSoftImageInfo arcSoftImageInfo = new ArcSoftImageInfo(width,height,FaceEngine.CP_PAF_BGR24,new byte[][]{bgr24},new int[]{width * 3});
//        Log.i(TAG, "processImage: " + arcSoftImageInfo.getPlanes()[0].length);
//        int detectCode = faceEngine.detectFaces(arcSoftImageInfo, faceInfoList);
        val detectCode = faceEngine.detectFaces(
            bgr24,
            width,
            height,
            FaceEngine.CP_PAF_BGR24,
            DetectModel.RGB,
            faceInfoList
        )
        LogUtils.debugInfo(TAG, "detectFaces cost time:" + (System.currentTimeMillis() - start))
        if (detectCode == ErrorInfo.MOK) {
//            Log.i(TAG, "processImage: fd costTime = " + (System.currentTimeMillis() - fdStartTime));
        }

        //绘制bitmap
        val bitmapForDraw = bitmap.copy(Bitmap.Config.RGB_565, true)
        val canvas = Canvas(bitmapForDraw)
        val paint = Paint()
        addNotificationInfo(
            notificationSpannableStringBuilder,
            null,
            "detect result:\nerrorCode is :",
            detectCode.toString(),
            "   face Number is ",
            faceInfoList.size.toString(),
            "\n"
        )
        /**
         * 3.若检测结果人脸数量大于0，则在bitmap上绘制人脸框并且重新显示到ImageView，若人脸数量为0，则无法进行下一步操作，操作结束
         */
        if (faceInfoList.isNotEmpty()) {
            addNotificationInfo(notificationSpannableStringBuilder, null, "face list:\n")
            paint.isAntiAlias = true
            paint.strokeWidth = 5f
            paint.color = Color.BLUE
            for (i in faceInfoList.indices) {
                //绘制人脸框
                paint.style = Paint.Style.STROKE
                canvas.drawRect(faceInfoList[i].rect, paint)
                //绘制人脸序号
                paint.style = Paint.Style.FILL_AND_STROKE
                val textSize = faceInfoList[i].rect.width() / 2
                paint.textSize = textSize.toFloat()
                canvas.drawText(
                    i.toString(),
                    faceInfoList[i].rect.left.toFloat(),
                    faceInfoList[i].rect.top.toFloat(),
                    paint
                )
                addNotificationInfo(
                    notificationSpannableStringBuilder,
                    null,
                    "face[",
                    i.toString(),
                    "]:",
                    faceInfoList[i].toString(),
                    "\n"
                )
            }
            drawBitmap.postValue(bitmapForDraw)
            //显示
//            runOnUiThread(Runnable {
//                Glide.with(ivShow.getContext())
//                    .load(bitmapForDraw)
//                    .into(ivShow)
//            })
        } else {
            addNotificationInfo(
                notificationSpannableStringBuilder,
                null,
                "can not do further action, exit!"
            )
            //showNotificationAndFinish(notificationSpannableStringBuilder)
            return
        }
        addNotificationInfo(notificationSpannableStringBuilder, null, "\n")
        /**
         * 4.上一步已获取到人脸位置和角度信息，传入给process函数，进行年龄、性别、三维角度、活体检测
         */
        val processStartTime = System.currentTimeMillis()
        val faceProcessCode = faceEngine.process(
            bgr24,
            width,
            height,
            FaceEngine.CP_PAF_BGR24,
            faceInfoList,
            FaceEngine.ASF_AGE or FaceEngine.ASF_GENDER or FaceEngine.ASF_FACE3DANGLE or FaceEngine.ASF_LIVENESS
        )
        if (faceProcessCode != ErrorInfo.MOK) {
            addNotificationInfo(
                notificationSpannableStringBuilder,
                ForegroundColorSpan(Color.RED),
                "process failed! code is ",
                faceProcessCode.toString(),
                "\n"
            )
        } else {
//            Log.i(TAG, "processImage: process costTime = " + (System.currentTimeMillis() - processStartTime));
        }
        //年龄信息结果
        val ageInfoList: List<AgeInfo> = ArrayList()
        //性别信息结果
        val genderInfoList: List<GenderInfo> = ArrayList()
        //人脸三维角度结果
        val face3DAngleList: List<Face3DAngle> = ArrayList()
        //活体检测结果
        val livenessInfoList: List<LivenessInfo> = ArrayList()
        //获取年龄、性别、三维角度、活体结果
        val ageCode = faceEngine.getAge(ageInfoList)
        val genderCode = faceEngine.getGender(genderInfoList)
        val face3DAngleCode = faceEngine.getFace3DAngle(face3DAngleList)
        val livenessCode = faceEngine.getLiveness(livenessInfoList)
        LogUtils.debugInfo(TAG, "getFace info cost time:" + (System.currentTimeMillis() - processStartTime))
        if (ageCode or genderCode or face3DAngleCode or livenessCode != ErrorInfo.MOK) {
            addNotificationInfo(
                notificationSpannableStringBuilder,
                null,
                "at least one of age,gender,face3DAngle detect failed!,codes are:",
                ageCode.toString(),
                " , ",
                genderCode.toString(),
                " , ",
                face3DAngleCode.toString()
            )
            //showNotificationAndFinish(notificationSpannableStringBuilder)
            return
        }
        /**
         * 5.年龄、性别、三维角度已获取成功，添加信息到提示文字中
         */
        //年龄数据
        if (ageInfoList.size > 0) {
            addNotificationInfo(
                notificationSpannableStringBuilder,
                StyleSpan(Typeface.BOLD),
                "age of each face:\n"
            )
        }
        for (i in ageInfoList.indices) {
            addNotificationInfo(
                notificationSpannableStringBuilder,
                null,
                "face[",
                i.toString(),
                "]:",
                ageInfoList[i].age.toString(),
                "\n"
            )
        }
        addNotificationInfo(notificationSpannableStringBuilder, null, "\n")

        //性别数据
        if (genderInfoList.size > 0) {
            addNotificationInfo(
                notificationSpannableStringBuilder,
                StyleSpan(Typeface.BOLD),
                "gender of each face:\n"
            )
        }
        for (i in genderInfoList.indices) {
            addNotificationInfo(
                notificationSpannableStringBuilder,
                null,
                "face[",
                i.toString(),
                "]:",
                if (genderInfoList[i].gender == GenderInfo.MALE) "MALE" else if (genderInfoList[i].gender == GenderInfo.FEMALE) "FEMALE" else "UNKNOWN",
                "\n"
            )
        }
        addNotificationInfo(notificationSpannableStringBuilder, null, "\n")


        //人脸三维角度数据
        if (face3DAngleList.size > 0) {
            addNotificationInfo(
                notificationSpannableStringBuilder,
                StyleSpan(Typeface.BOLD),
                "face3DAngle of each face:\n"
            )
            for (i in face3DAngleList.indices) {
                addNotificationInfo(
                    notificationSpannableStringBuilder,
                    null,
                    "face[",
                    i.toString(),
                    "]:",
                    face3DAngleList[i].toString(),
                    "\n"
                )
            }
        }
        addNotificationInfo(notificationSpannableStringBuilder, null, "\n")

        //活体检测数据
        if (livenessInfoList.size > 0) {
            addNotificationInfo(
                notificationSpannableStringBuilder,
                StyleSpan(Typeface.BOLD),
                "liveness of each face:\n"
            )
            for (i in livenessInfoList.indices) {
                var liveness: String? = null
                liveness = when (livenessInfoList[i].liveness) {
                    LivenessInfo.ALIVE -> "ALIVE"
                    LivenessInfo.NOT_ALIVE -> "NOT_ALIVE"
                    LivenessInfo.UNKNOWN -> "UNKNOWN"
                    LivenessInfo.FACE_NUM_MORE_THAN_ONE -> "FACE_NUM_MORE_THAN_ONE"
                    else -> "UNKNOWN"
                }
                addNotificationInfo(
                    notificationSpannableStringBuilder,
                    null,
                    "face[",
                    i.toString(),
                    "]:",
                    liveness,
                    "\n"
                )
            }
        }
        addNotificationInfo(notificationSpannableStringBuilder, null, "\n")
//        /**
//         * 6.最后将图片内的所有人脸进行一一比对并添加到提示文字中
//         */
//        if (faceInfoList.size > 0) {
//            val faceFeatures = arrayOfNulls<FaceFeature>(faceInfoList.size)
//            val extractFaceFeatureCodes = IntArray(faceInfoList.size)
//            addNotificationInfo(
//                notificationSpannableStringBuilder,
//                StyleSpan(Typeface.BOLD),
//                "faceFeatureExtract:\n"
//            )
//            for (i in faceInfoList.indices) {
//                faceFeatures[i] = FaceFeature()
//                //从图片解析出人脸特征数据
//                val frStartTime = System.currentTimeMillis()
//                extractFaceFeatureCodes[i] = faceEngine.extractFaceFeature(
//                    bgr24, width, height, FaceEngine.CP_PAF_BGR24,
//                    faceInfoList[i],
//                    faceFeatures[i]
//                )
//                if (extractFaceFeatureCodes[i] != ErrorInfo.MOK) {
//                    addNotificationInfo(
//                        notificationSpannableStringBuilder,
//                        null,
//                        "faceFeature of face[",
//                        i.toString(),
//                        "]",
//                        " extract failed, code is ",
//                        extractFaceFeatureCodes[i].toString(),
//                        "\n"
//                    )
//                } else {
////                    Log.i(TAG, "processImage: fr costTime = " + (System.currentTimeMillis() - frStartTime));
//                    addNotificationInfo(
//                        notificationSpannableStringBuilder,
//                        null,
//                        "faceFeature of face[",
//                        i.toString(),
//                        "]",
//                        " extract success\n"
//                    )
//                }
//            }
//            addNotificationInfo(notificationSpannableStringBuilder, null, "\n")
//
//            //人脸特征的数量大于2，将所有特征进行比较
//            if (faceFeatures.size >= 2) {
//                addNotificationInfo(
//                    notificationSpannableStringBuilder,
//                    StyleSpan(Typeface.BOLD),
//                    "similar of faces:\n"
//                )
//                for (i in faceFeatures.indices) {
//                    for (j in i + 1 until faceFeatures.size) {
//                        addNotificationInfo(
//                            notificationSpannableStringBuilder,
//                            StyleSpan(Typeface.BOLD_ITALIC),
//                            "compare face[",
//                            i.toString(),
//                            "] and  face[",
//                            j.toString(),
//                            "]:\n"
//                        )
//                        //若其中一个特征提取失败，则不进行比对
//                        var canCompare = true
//                        if (extractFaceFeatureCodes[i] != 0) {
//                            addNotificationInfo(
//                                notificationSpannableStringBuilder,
//                                null,
//                                "faceFeature of face[",
//                                i.toString(),
//                                "] extract failed, can not compare!\n"
//                            )
//                            canCompare = false
//                        }
//                        if (extractFaceFeatureCodes[j] != 0) {
//                            addNotificationInfo(
//                                notificationSpannableStringBuilder,
//                                null,
//                                "faceFeature of face[",
//                                j.toString(),
//                                "] extract failed, can not compare!\n"
//                            )
//                            canCompare = false
//                        }
//                        if (!canCompare) {
//                            continue
//                        }
//                        val matching = FaceSimilar()
//                        //比对两个人脸特征获取相似度信息
//                        faceEngine.compareFaceFeature(
//                            faceFeatures[i],
//                            faceFeatures[j], CompareModel.LIFE_PHOTO, matching
//                        )
//                        //新增相似度比对结果信息
//                        addNotificationInfo(
//                            notificationSpannableStringBuilder,
//                            null,
//                            "similar of face[",
//                            i.toString(),
//                            "] and  face[",
//                            j.toString(),
//                            "] is:",
//                            matching.score.toString(),
//                            "\n"
//                        )
//                    }
//                }
//            }
//        }
        LogUtils.debugInfo(TAG, "all cost time:" + (System.currentTimeMillis() - start))
//        showNotificationAndFinish(notificationSpannableStringBuilder)
    }


    /**
     * 追加提示信息
     *
     * @param stringBuilder 提示的字符串的存放对象
     * @param styleSpan     添加的字符串的格式
     * @param strings       字符串数组
     */
    private fun addNotificationInfo(
        stringBuilder: SpannableStringBuilder?,
        styleSpan: ParcelableSpan?,
        vararg strings: String
    ) {
        if (stringBuilder == null || strings == null || strings.size == 0) {
            return
        }
        val startLength = stringBuilder.length
        for (string in strings) {
            stringBuilder.append(string)
        }
        val endLength = stringBuilder.length
        if (styleSpan != null) {
            stringBuilder.setSpan(
                styleSpan,
                startLength,
                endLength,
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
            )
        }
    }
}