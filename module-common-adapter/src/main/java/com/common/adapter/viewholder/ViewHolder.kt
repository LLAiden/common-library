@file:Suppress("UNCHECKED_CAST")

package com.common.adapter.viewholder

import android.graphics.Bitmap
import android.graphics.Paint
import android.graphics.Typeface
import android.graphics.drawable.Drawable
import android.text.util.Linkify
import android.util.SparseArray
import android.view.View
import android.view.View.OnLongClickListener
import android.view.View.OnTouchListener
import android.widget.*
import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.annotation.IdRes
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView

class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    private val views = SparseArray<View>()

    fun <T : View?> getView(@IdRes id: Int): T {
        var view = views[id]
        if (view == null) {
            view = itemView.findViewById(id)
            views.put(id, view)
        }
        return view as T
    }

    fun setText(@IdRes viewId: Int, text: String?): ViewHolder {
        val tv = getView<TextView>(viewId)
        tv.text = text
        return this
    }

    fun setImageResource(@IdRes viewId: Int, @DrawableRes resId: Int): ViewHolder {
        val view = getView<ImageView>(viewId)
        view.setImageResource(resId)
        return this
    }

    fun setImageBitmap(@IdRes viewId: Int, bitmap: Bitmap?): ViewHolder {
        val view = getView<ImageView>(viewId)
        view.setImageBitmap(bitmap)
        return this
    }

    fun setImageDrawable(@IdRes viewId: Int, drawable: Drawable?): ViewHolder {
        val view = getView<ImageView>(viewId)
        view.setImageDrawable(drawable)
        return this
    }

    fun setBackgroundColor(@IdRes viewId: Int, @ColorInt color: Int): ViewHolder {
        getView<View>(viewId).setBackgroundColor(color)
        return this
    }

    fun setBackgroundRes(@IdRes viewId: Int, @DrawableRes backgroundRes: Int): ViewHolder {
        getView<View>(viewId).setBackgroundResource(backgroundRes)
        return this
    }

    fun setTextColor(@IdRes viewId: Int, @ColorInt textColor: Int): ViewHolder {
        val view = getView<TextView>(viewId)
        view.setTextColor(textColor)
        return this
    }

    fun setTextColorRes(@IdRes viewId: Int, @ColorRes textColorRes: Int): ViewHolder {
        val view = getView<TextView>(viewId)
        view.setTextColor(ContextCompat.getColor(itemView.context, textColorRes))
        return this
    }

    fun setAlpha(@IdRes viewId: Int, value: Float): ViewHolder {
        getView<View>(viewId).alpha = value
        return this
    }

    fun setVisible(@IdRes viewId: Int, visible: Boolean): ViewHolder {
        getView<View>(viewId).visibility = if (visible) View.VISIBLE else View.GONE
        return this
    }

    fun linkify(@IdRes viewId: Int): ViewHolder {
        val view = getView<TextView>(viewId)
        Linkify.addLinks(view, Linkify.ALL)
        return this
    }

    fun setTypeface(typeface: Typeface?, @IdRes vararg viewIds: Int): ViewHolder {
        for (viewId in viewIds) {
            val view = getView<TextView>(viewId)
            view.typeface = typeface
            view.paintFlags = view.paintFlags or Paint.SUBPIXEL_TEXT_FLAG
        }
        return this
    }

    fun setProgress(@IdRes viewId: Int, progress: Int): ViewHolder {
        val view = getView<ProgressBar>(viewId)
        view.progress = progress
        return this
    }

    fun setProgress(@IdRes viewId: Int, progress: Int, max: Int): ViewHolder {
        val view = getView<ProgressBar>(viewId)
        view.max = max
        view.progress = progress
        return this
    }

    fun setMax(@IdRes viewId: Int, max: Int): ViewHolder {
        val view = getView<ProgressBar>(viewId)
        view.max = max
        return this
    }

    fun setRating(@IdRes viewId: Int, rating: Float): ViewHolder {
        val view = getView<RatingBar>(viewId)
        view.rating = rating
        return this
    }

    fun setRating(@IdRes viewId: Int, rating: Float, max: Int): ViewHolder {
        val view = getView<RatingBar>(viewId)
        view.max = max
        view.rating = rating
        return this
    }

    fun setTag(@IdRes viewId: Int, tag: Any?): ViewHolder {
        val view = getView<View>(viewId)
        view.tag = tag
        return this
    }

    fun setTag(@IdRes viewId: Int, key: Int, tag: Any?): ViewHolder {
        getView<View>(viewId).setTag(key, tag)
        return this
    }

    fun setChecked(@IdRes viewId: Int, checked: Boolean): ViewHolder {
        val view = getView<CheckBox>(viewId)
        view.isChecked = checked
        return this
    }

    /**
     * 关于事件的
     */
    fun setOnClickListener(@IdRes viewId: Int, listener: View.OnClickListener?): ViewHolder {
        getView<View>(viewId).setOnClickListener(listener)
        return this
    }

    fun setOnTouchListener(@IdRes viewId: Int, listener: OnTouchListener?): ViewHolder {
        getView<View>(viewId).setOnTouchListener(listener)
        return this
    }

    fun setOnLongClickListener(@IdRes viewId: Int, listener: OnLongClickListener?): ViewHolder {
        getView<View>(viewId).setOnLongClickListener(listener)
        return this
    }
}
