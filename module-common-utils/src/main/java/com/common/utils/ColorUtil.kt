package com.common.utils

import android.content.Context
import android.graphics.Color
import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import androidx.annotation.IntRange
import androidx.core.content.ContextCompat


object ColorUtil {

    fun alphaColor(@ColorInt color: Int, @IntRange(from = 0, to = 100) alpha: Int): Int {
        return color and 0x00ffffff or ((alpha / 100F * 255.0f + 0.5f).toInt() shl 24)
    }

    fun color2String(@ColorInt colorInt: Int): String {
        return String.format("#%06X", colorInt)
    }

    fun isLightColor(@ColorInt color: Int): Boolean {
        return 0.299 * Color.red(color) + 0.587 * Color.green(color) + 0.114 * Color.blue(color) >= 127.5
    }

    fun getRandomColor(supportAlpha: Boolean): Int {
        val high = if (supportAlpha) (Math.random() * 0x100).toInt() shl 24 else -0x1000000
        return high or (Math.random() * 0x1000000).toInt()
    }

    fun getColor(context: Context, @ColorRes res: Int): Int {
        return ContextCompat.getColor(context, res)
    }

    fun getSafeColor(color: String, defaultColor: Int = Color.TRANSPARENT): Int {
        kotlin.runCatching { Color.parseColor(color) }.onSuccess {
            return it
        }
        return defaultColor
    }
}