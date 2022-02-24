package com.common.utils

object ListUtil {

    fun isNotEmpty(list: List<*>?): Boolean {
        return list != null && list.isNotEmpty()
    }

    fun isEmpty(list: List<*>?): Boolean {
        return !isNotEmpty(list)
    }

    fun isValidPosition(position: Int?, collection: Collection<*>?): Boolean {
        return isValidPosition(position, collection?.size ?: 0)
    }

    fun isValidPosition(position: Int?, size: Int?): Boolean {
        return position != null && size != null && position > -1 && position < size
    }
}