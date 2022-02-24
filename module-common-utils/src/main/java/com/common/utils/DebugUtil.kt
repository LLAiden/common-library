package com.common.utils

import android.util.Log

class DebugUtil {

    fun debug(tag: String) {
        try {
            throw DebugException()
        } catch (th: Throwable) {
            Log.e(tag, "", th)
        }
    }

    class DebugException : Exception()
}