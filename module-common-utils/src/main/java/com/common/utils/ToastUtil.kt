package com.common.utils

import android.content.Context
import android.os.Looper
import android.widget.Toast

object ToastUtil {

    private var mToast: Toast? = null

    fun showShort(context: Context?, charSequence: CharSequence) {
        context?.let {
            val applicationContext = context.applicationContext
            checkInstance(applicationContext, Toast.LENGTH_SHORT)
            mToast
        }?.apply {
            if (isMainThread()) {
                mToast?.setText(charSequence)
                show()
            } else {
                Looper.getMainLooper().thread.run {
                    mToast?.setText(charSequence)
                    show()
                }
            }
        }
    }

    fun showLong(context: Context?, charSequence: CharSequence) {
        context?.let {
            val applicationContext = context.applicationContext
            checkInstance(applicationContext, Toast.LENGTH_LONG)
            mToast
        }?.apply {
            if (isMainThread()) {
                mToast?.setText(charSequence)
                show()
            } else {
                Looper.getMainLooper().thread.run {
                    mToast?.setText(charSequence)
                    show()
                }
            }
        }
    }

    private fun checkInstance(context: Context, duration: Int) {
        if (mToast == null) {
            mToast = Toast.makeText(context, null, duration)
        }
        mToast?.duration = duration
    }

    fun isMainThread(): Boolean {
        return Looper.getMainLooper() == Looper.myLooper()
    }
}