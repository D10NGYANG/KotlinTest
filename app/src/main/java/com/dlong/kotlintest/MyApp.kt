package com.dlong.kotlintest

import android.app.Application
import com.facebook.stetho.Stetho

/**
 * @author D10NG
 * @date on 2019-10-21 14:17
 */
class MyApp : Application() {

    override fun onCreate() {
        super.onCreate()
        // 数据库调试查看器
        //Stetho.initializeWithDefaults(this)
    }
}