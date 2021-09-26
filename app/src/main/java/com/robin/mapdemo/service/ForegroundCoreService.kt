package com.robin.mapdemo.service

import android.app.Service
import android.content.Intent
import android.os.IBinder

class ForegroundCoreService : Service() {
    override fun onBind(intent: Intent?): IBinder? = null
    private val mForegroundNF:ForegroundNF by lazy {
        ForegroundNF(this)
    }
    override fun onCreate() {
        super.onCreate()
        mForegroundNF.startForegroundNotification()
    }
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        if(null == intent){
            //服务被系统kill掉之后重启进来的
            return START_NOT_STICKY
        }
        mForegroundNF.startForegroundNotification()
        return super.onStartCommand(intent, flags, startId)
    }
    override fun onDestroy() {
        mForegroundNF.stopForegroundNotification()
        super.onDestroy()
    }
}