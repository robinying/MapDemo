package com.robin.mapdemo.ui.recycleview.fragment

import android.os.Bundle
import com.blankj.utilcode.util.LogUtils
import com.blankj.utilcode.util.ToastUtils
import com.drake.brv.item.ItemExpand
import com.drake.brv.utils.linear
import com.drake.brv.utils.setup
import com.robin.jetpackmvvm.base.viewmodel.BaseViewModel
import com.robin.mapdemo.R
import com.robin.mapdemo.app.base.BaseVBFragment
import com.robin.mapdemo.databinding.FragmentGroupBinding
import com.robin.mapdemo.model.GroupModel
import com.robin.mapdemo.model.Model
import com.robin.mapdemo.model.NestedGroupModel

class GroupFragment:BaseVBFragment<BaseViewModel,FragmentGroupBinding>() {
    override fun initView(savedInstanceState: Bundle?) {
        binding.rv.linear().setup {

            // 任何条目都需要添加类型到BindingAdapter中
            addType<GroupModel>(R.layout.item_group_title)
            //addType<NestedGroupModel>(R.layout.item_nested_group_title)
            addType<Model>(R.layout.item_multi_type_simple)
            R.id.item.onFastClick {
                when (itemViewType) {
                    R.layout.item_nested_group_title, R.layout.item_group_title -> {

                        val changeCount =
                            if (getModel<ItemExpand>().itemExpand) "折叠 ${expandOrCollapse()} 条" else "展开 ${expandOrCollapse()} 条"

                        ToastUtils.showShort(changeCount)
                    }
                }
            }

        }.models = getData()
    }

    private fun getData(): MutableList<GroupModel> {
        return mutableListOf<GroupModel>().apply {
            for (i in 0..4) {

                // 第二个分组存在嵌套分组
//                if (i == 0) {
//                    val nestedGroupModel = GroupModel().apply {
//                        itemSublist =
//                            listOf(NestedGroupModel(), NestedGroupModel(), NestedGroupModel())
//                    }
//                    add(nestedGroupModel)
//                    continue
//                }
                LogUtils.d("robinTest getData i:$i")
                add(GroupModel())
            }
        }
    }
}