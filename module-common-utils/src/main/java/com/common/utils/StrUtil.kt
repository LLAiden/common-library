package com.common.utils

import android.text.TextUtils
import java.util.regex.Pattern

object StrUtil {

    fun isEmpty(str: String) = TextUtils.isEmpty(str)
    fun isNotEmpty(str: String) = !TextUtils.isEmpty(str)

    fun isNumber(any: Any): Boolean {
        if (any is Number) {
            return true
        }
        if (any is String) {
            val pattern = Pattern.compile("[0-9]")
            val matcher = pattern.matcher(any)
            if (matcher.find()) {
                return true
            }
        }
        return false
    }

    fun equals(a: String?, b: String?) = a?.equals(b)

    fun safe2Int(any: Any, def: Int): Int {
        try {
            return any.toString().toInt()
        } catch (ex: Throwable) {
            ex.printStackTrace()
        }
        return def
    }

    fun length(str: String?) = str?.length ?: 0
}