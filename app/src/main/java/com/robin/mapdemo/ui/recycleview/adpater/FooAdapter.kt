package com.robin.mapdemo.ui.recycleview.adpater

import com.robin.jetpackmvvm.base.adpater.BaseBindingQuickAdapter
import com.robin.mapdemo.databinding.ItemFooBinding
import com.robin.mapdemo.model.Foo

class FooAdapter : BaseBindingQuickAdapter<Foo, ItemFooBinding>(ItemFooBinding::inflate) {
    override fun convert(holder: BaseBindingHolder, item: Foo) {
        holder.getViewBinding<ItemFooBinding>().apply {
            tvFooTitle.text = item.title
        }
    }
}