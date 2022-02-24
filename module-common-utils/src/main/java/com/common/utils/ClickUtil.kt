package com.common.utils

import android.util.Log
import android.view.View

object ClickUtil {

    private const val INTERVAL = 500L

    fun isDoubleClick(view: View): Boolean {
        return isDoubleClick(view, INTERVAL)
    }

    fun isDoubleClick(view: View, interval: Long): Boolean {
        var result = false
        val generateTagId = TagUtil.generateTagId(Constants.TAG.TAG_DOUBLE_CLICK)
        Log.e("ClickUtil", "isDoubleClick: generateTagId: $generateTagId")
        val tag = view.getTag(generateTagId)
        val currentTimestamp = currentTimestamp
        if (tag is Long) {
            val pastTime = currentTimestamp - tag
            if (pastTime <= interval) {
                result = true
            }
        }
        view.setTag(generateTagId, currentTimestamp)
        return result
    }

    private val currentTimestamp: Long get() = System.currentTimeMillis()
}