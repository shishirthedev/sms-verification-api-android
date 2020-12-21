package com.shishirthedev.smsretriverapi

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.google.android.gms.auth.api.phone.SmsRetriever
import com.google.android.gms.common.api.CommonStatusCodes
import com.google.android.gms.common.api.Status


class SMSReceiver : BroadcastReceiver() {
    private var otpListener: OTPReceiveListener? = null

    /**
     * @param otpListener
     */
    fun setOTPListener(otpListener: OTPReceiveListener?) {
        this.otpListener = otpListener
    }

    /**
     * @param context
     * @param intent
     */
    override fun onReceive(context: Context, intent: Intent) {
        if (SmsRetriever.SMS_RETRIEVED_ACTION == intent.action) {
            val extras = intent.extras
            val status = extras!![SmsRetriever.EXTRA_STATUS] as Status?
            when (status!!.statusCode) {
                CommonStatusCodes.SUCCESS -> {

                    //This is the full message
                    val message = extras[SmsRetriever.EXTRA_SMS_MESSAGE] as String?

                    /*<#> Your ExampleApp code is: 123ABC78
                    FA+9qCX9VSu*/

                    //Extract the OTP code and send to the listener
                    if (otpListener != null) {
                        otpListener!!.onOTPReceived(message)
                    }
                }
                CommonStatusCodes.TIMEOUT ->                     // Waiting for SMS timed out (5 minutes)
                    if (otpListener != null) {
                        otpListener!!.onOTPTimeOut()
                    }
                CommonStatusCodes.API_NOT_CONNECTED -> if (otpListener != null) {
                    otpListener!!.onOTPReceivedError("API NOT CONNECTED")
                }
                CommonStatusCodes.NETWORK_ERROR -> if (otpListener != null) {
                    otpListener!!.onOTPReceivedError("NETWORK ERROR")
                }
                CommonStatusCodes.ERROR -> if (otpListener != null) {
                    otpListener!!.onOTPReceivedError("SOME THING WENT WRONG")
                }
            }
        }
    }

    /**
     *
     */
    interface OTPReceiveListener {
        fun onOTPReceived(otp: String?)
        fun onOTPTimeOut()
        fun onOTPReceivedError(error: String?)
    }
}