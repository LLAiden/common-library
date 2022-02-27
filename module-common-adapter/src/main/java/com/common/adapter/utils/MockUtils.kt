package com.common.adapter.utils

import java.util.ArrayList

object MockUtils {

    fun getMockStringList(size: Int): List<String> {
        val stringList: MutableList<String> = ArrayList()
        val content = "mock data"
        for (i in 0 until size) {
            stringList.add("$i--->$content")
        }
        return stringList
    }

}