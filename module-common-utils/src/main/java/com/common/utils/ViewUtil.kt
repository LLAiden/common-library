package com.common.utils

import android.graphics.Rect
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.core.view.ScrollingView

object ViewUtil {
    /**
     * 获得这个节点下所有的View不包含View
     */
    fun getViews(view: View): List<View> {
        val viewList: MutableList<View> = ArrayList()
        if (view is ViewGroup) {
            val childCount = view.childCount
            for (index in 0 until childCount) {
                val childAt = view.getChildAt(index)
                if (childAt is ViewGroup) {
                    viewList.addAll(getViews(childAt))
                } else {
                    viewList.add(childAt)
                }
            }
        } else {
            viewList.add(view)
        }
        return viewList
    }

    /**
     * 判断可滚动的View,不支持ScrollView
     */
    fun getScrollingView(view: View?): List<ScrollingView> {
        val viewList: MutableList<ScrollingView> = ArrayList()
        if (view is ViewGroup) {
            if (view is ScrollingView) {
                viewList.add(view as ScrollingView)
            }
            val childCount = view.childCount
            for (i in 0 until childCount) {
                val childAt = view.getChildAt(i)
                viewList.addAll(getScrollingView(childAt))
            }
        } else if (view is ScrollingView) {
            viewList.add(view as ScrollingView)
        }
        return viewList
    }

    fun isTouch(view: View, ev: MotionEvent): Boolean {
        val location = IntArray(2)
        view.getLocationOnScreen(location)
        val rect = Rect()
        view.getDrawingRect(rect)
        rect.left = location[0]
        rect.top = location[1]
        rect.right = rect.right + location[0]
        rect.bottom = rect.bottom + location[1]
        val rawX = ev.rawX.toInt()
        val rawY = ev.rawY.toInt()
        return rect.contains(rawX, rawY)
    }

    fun isTouch(list: List<*>, ev: MotionEvent): View? {
        for (obj in list) {
            if (obj is View) {
                if (isTouch(obj, ev)) {
                    return obj
                }
            }
        }
        return null
    }

    //判断一个View能不能向上滑动 direction>0表示向上滑动
    fun canBottom2Top(view: View?): Boolean {
        return view?.canScrollVertically(1) ?: false
    }

    fun canTop2Bottom(view: View?): Boolean {
        return view?.canScrollVertically(-1) ?: false
    }

    fun canRight2Left(view: View?): Boolean {
        return view?.canScrollHorizontally(1) ?: false
    }

    fun canLeft2Right(view: View?): Boolean {
        return view?.canScrollHorizontally(-1) ?: false
    }

    fun isBottom2Top(value: Int): Boolean {
        return value > 0
    }

    fun isTop2Bottom(value: Int): Boolean {
        return value < 0
    }

    fun isRight2Left(value: Int): Boolean {
        return value < 0
    }

    fun isLeft2Right(value: Int): Boolean {
        return value > 0
    }
}