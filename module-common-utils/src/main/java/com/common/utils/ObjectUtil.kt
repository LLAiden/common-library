package com.common.utils

import java.util.*

object ObjectUtil {

    fun equals(a: Any, b: Any) = Objects.equals(a, b)
    fun isEmpty(a: Any?) = a == null
    fun isNotEmpty(a: Any?) = !isEmpty(a)
}