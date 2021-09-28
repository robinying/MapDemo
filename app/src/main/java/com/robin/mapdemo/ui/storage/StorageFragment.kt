package com.robin.mapdemo.ui.storage

import android.app.RecoverableSecurityException
import android.content.ContentUris
import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.os.ParcelFileDescriptor
import android.provider.MediaStore
import android.util.Log
import com.blankj.utilcode.util.FileUtils
import com.blankj.utilcode.util.LogUtils
import com.robin.jetpackmvvm.base.viewmodel.BaseViewModel
import com.robin.mapdemo.R
import com.robin.mapdemo.app.base.BaseFragment
import com.robin.mapdemo.databinding.FragmentStorageBinding
import com.robin.mapdemo.util.DatetimeUtil
import java.io.*

/*
*
*
* */
class StorageFragment : BaseFragment<BaseViewModel, FragmentStorageBinding>() {
    override fun layoutId(): Int {
        return R.layout.fragment_storage
    }

    override fun initView(savedInstanceState: Bundle?) {

    }

    override fun lazyLoadData() {
        super.lazyLoadData()

    }

    private fun saveBitmap() {
        val fileName = DatetimeUtil.formatDate(
            System.currentTimeMillis(),
            "yyyyMMddHHmmss"
        ) + ".png"
        val folderPath = if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
            File(
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM),
                "Demo"
            ).path
        } else {
            Environment.DIRECTORY_PICTURES + "/Demo"
        }
        FileUtils.createOrExistsDir(folderPath)
        val imageMediaPath = "$folderPath/$fileName"


        LogUtils.d("robinTest imageMediaPath:$imageMediaPath" + " -- Build SDK version:" + Build.VERSION.SDK_INT)

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
            try {
                val bitmap = Bitmap.createBitmap(400, 400, Bitmap.Config.ARGB_8888)
                //创建了一个红色的图片
                val canvas = Canvas(bitmap)
                canvas.drawColor(Color.RED)
                val outputFile = File(imageMediaPath)
                val fos = FileOutputStream(outputFile)
                bitmap.compress(Bitmap.CompressFormat.PNG, 90, fos)
                fos.close()
            } catch (e: FileNotFoundException) {
                Log.d("robinTest", "创建失败：${e.message}")
            } catch (e: IOException) {
                Log.d("robinTest", "创建失败：${e.message}")
            }
        } else {
            val values = ContentValues()
            values.put(MediaStore.Images.Media.DISPLAY_NAME, "red_image.png")
            values.put(MediaStore.Images.Media.DESCRIPTION, "This is an image")
            values.put(MediaStore.Images.Media.MIME_TYPE, "image/png")
            values.put(MediaStore.Images.Media.RELATIVE_PATH, imageMediaPath)
            val external = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
            val insertUri = mActivity.contentResolver.insert(external, values)
            var os: OutputStream? = null
            try {
                if (insertUri != null) {
                    os = mActivity.contentResolver.openOutputStream(insertUri)
                }
                if (os != null) {
                    val bitmap = Bitmap.createBitmap(400, 400, Bitmap.Config.ARGB_8888)
                    //创建了一个红色的图片
                    val canvas = Canvas(bitmap)
                    canvas.drawColor(Color.RED)
                    // 向os流写入数据
                    bitmap.compress(Bitmap.CompressFormat.PNG, 90, os)
                }
            } catch (e: IOException) {
                Log.d("robinTest", "创建失败：${e.message}")
            } finally {
                os?.close()
            }
        }
    }

    private fun deletePic(imgUri: Uri, context: Context) {
        val queryUri = imgUri
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
            try {
                val projection = arrayOf(MediaStore.Images.Media.DATA)
                val cursor = context.contentResolver.query(
                    queryUri, projection,
                    null, null, null
                )
                cursor?.let {
                    val columnIndex = it.getColumnIndex(MediaStore.Images.Media.DATA)
                    if (columnIndex > -1) {
                        val file = File(it.getString(columnIndex))
                        file.delete()
                    }
                }
                cursor?.close()
            } catch (e: IOException) {
                Log.e("robinTest", "delete failed :${e.message}")
            }
        } else {
            try {
                context.contentResolver.delete(queryUri, null, null)
            } catch (e: IOException) {
                Log.e("robinTest", "delete failed :${e.message}")
            } catch (e1: RecoverableSecurityException) {
            }
        }
    }

    private fun modifyUri(queryUri: Uri) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            var pfd: ParcelFileDescriptor? = null
            try {
                pfd = mActivity.contentResolver.openFileDescriptor(queryUri, "r")
                if (pfd != null) {
                    val bitmap = BitmapFactory.decodeFileDescriptor(pfd.fileDescriptor)
//                imageIv.setImageBitmap(bitmap)
                }
            } catch (e: IOException) {
                e.printStackTrace()
            } finally {
                pfd?.close()
            }
        }
    }

    private fun searchImage(){
        val external = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        val projection = arrayOf(MediaStore.Images.Media._ID)
        val cursor = mActivity.contentResolver.query(external, projection, null, null, null)
        if (cursor != null && cursor.moveToFirst()) {
            val queryUri = ContentUris.withAppendedId(external, cursor.getLong(0))
            // queryUri即上图中对应的uri
            cursor.close()
        }
    }
}