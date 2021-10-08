package com.robin.mapdemo.model

import androidx.databinding.BaseObservable
import com.drake.brv.item.ItemExpand
import com.drake.brv.item.ItemHover
import com.drake.brv.item.ItemPosition
import com.robin.mapdemo.R

class GroupModel : ItemExpand, ItemHover, ItemPosition,
    BaseObservable() {

    override var itemGroupPosition: Int = 0
    override var itemExpand: Boolean = false
        set(value) {
            field = value
            notifyChange()
        }

    // 这种代理方式是为了避免Gson等框架解析Kotlin会覆盖默认值问题: https://liangjingkanji.github.io/BRV/group.html#_2
    override var itemSublist: List<Any?>?
        get() = finalList
        set(value) {
            finalList = value as List<Model>
        }

    var finalList: List<Model> = listOf(Model(), Model(), Model(), Model())

    override var itemHover: Boolean = true
    override var itemPosition: Int = 0

    val title get() = "分组 [ $itemGroupPosition ]"

    val expandIcon get() = if (itemExpand) R.drawable.ic_arrow_expand else R.drawable.ic_arrow_collapse

}