package com.common.utils

object TagUtil {

    fun generateTagId(tag: String): Int {
        return tag.hashCode()
    }
}