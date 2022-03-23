package com.common.utils

import android.content.Context
import android.os.Handler
import android.os.Looper
import android.widget.Toast

object ToastUtil {

    @Volatile
    private var mToast: Toast? = null

    @Volatile
    private var mHandler: Handler? = null

    private val mLockAny = Any()

    fun showShort(context: Context?, charSequence: CharSequence) {
        context?.let {
            val applicationContext = context.applicationContext
            checkToastInstance(applicationContext, Toast.LENGTH_SHORT)
            mToast
        }?.apply {
            if (isMainThread()) {
                mToast?.setText(charSequence)
                show()
            } else {
                checkHandlerInstance()
                mHandler?.post {
                    mToast?.setText(charSequence)
                    show()
                }
            }
        }
    }

    fun showLong(context: Context?, charSequence: CharSequence) {
        context?.let {
            val applicationContext = context.applicationContext
            checkToastInstance(applicationContext, Toast.LENGTH_LONG)
            mToast
        }?.apply {
            if (isMainThread()) {
                mToast?.setText(charSequence)
                show()
            } else {
                checkHandlerInstance()
                mHandler?.post {
                    mToast?.setText(charSequence)
                    show()
                }
            }
        }
    }

    private fun checkToastInstance(context: Context, duration: Int) {
        if (mToast == null) {
            synchronized(mLockAny) {
                if (mToast == null) {
                    mToast = Toast.makeText(context, null, duration)
                }
            }
        }
        mToast?.duration = duration
    }

    private fun isMainThread(): Boolean {
        return Looper.getMainLooper() == Looper.myLooper()
    }

    private fun checkHandlerInstance() {
        if (mHandler == null) {
            synchronized(mLockAny) {
                if (mHandler == null) {
                    mHandler = Handler(Looper.getMainLooper())
                }
            }
        }
    }
}