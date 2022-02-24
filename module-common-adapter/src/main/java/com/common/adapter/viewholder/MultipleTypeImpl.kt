package com.common.adapter.viewholder

import com.common.adapter.MultipleType

class MultipleTypeImpl(private val itemType: Int) : MultipleType {

    override fun getItemType() = itemType
}
