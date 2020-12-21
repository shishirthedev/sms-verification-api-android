package com.shishirthedev.smsretriverapi

import android.app.Application
import android.util.Log

class BaseApplication : Application() {

    private val TAG = BaseApplication::class.java.simpleName

    override fun onCreate() {
        super.onCreate()
        // Generate Hash Key >>>>>
        val appSignatureHashHelper = AppSignatureHashHelper(this)
        Log.e(TAG, "HashKey: " + appSignatureHashHelper.appSignatures[0])
    }
}