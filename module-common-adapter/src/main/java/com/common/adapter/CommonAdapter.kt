package com.common.adapter

import android.content.Context
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.RecyclerView
import com.common.adapter.listener.OnItemClickListener
import com.common.adapter.listener.OnItemLongClickListener
import com.common.adapter.viewholder.ViewHolder
import kotlin.properties.Delegates

abstract class CommonAdapter<T> : RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private var type: MultipleType? = null
    private var dataList = mutableListOf<T>()
    private var context: Context
    private var layoutId by Delegates.notNull<Int>()

    private val TAG = "CommonAdapter"

    constructor(context: Context, @LayoutRes layoutId: Int, type: MultipleType) {
        this.context = context
        this.layoutId = layoutId
        this.type = type
    }


    constructor(context: Context, @LayoutRes layoutId: Int, dataList: List<T>) {
        this.context = context
        this.layoutId = layoutId
        this.dataList.addAll(dataList)
    }

    private var itemClickListener: OnItemClickListener? = null
    private var itemLongClickListener: OnItemLongClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val itemView = View.inflate(context, layoutId, null)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        convert(holder as ViewHolder, dataList[position])
        holder.itemView.setOnClickListener { itemClickListener?.onItemClick(it, position) }
        holder.itemView.setOnLongClickListener {
            itemLongClickListener?.onItemLongClick(it, position) ?: false
        }
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    abstract fun convert(holder: ViewHolder, t: T)
    fun getType() = type

    fun setOnItemClickListener(itemClickListener: OnItemClickListener): CommonAdapter<T> {
        this.itemClickListener = itemClickListener
        return this
    }

    fun setOnItemLongClickListener(itemLongClickListener: OnItemLongClickListener): CommonAdapter<T> {
        this.itemLongClickListener = itemLongClickListener
        return this
    }

    fun addData(t: T) {
        dataList.add(t)
    }

    fun addData(t: List<T>) {
        dataList.addAll(t)
    }
}
