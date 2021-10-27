package com.robin.mapdemo.ui.recycleview.fragment

import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.robin.jetpackmvvm.base.viewmodel.BaseViewModel
import com.robin.mapdemo.app.base.BaseVBFragment
import com.robin.mapdemo.databinding.FragmentRvVbBinding
import com.robin.mapdemo.model.Foo
import com.robin.mapdemo.ui.recycleview.adpater.FooAdapter


class RvVBFragment : BaseVBFragment<BaseViewModel, FragmentRvVbBinding>() {
    private val fooAdapter by lazy { FooAdapter() }
    override fun initView(savedInstanceState: Bundle?) {
        binding.rv.layoutManager = LinearLayoutManager(mActivity)
        binding.rv.adapter = fooAdapter

    }

    override fun lazyLoadData() {
        super.lazyLoadData()
        val fooList = mutableListOf<Foo>()
        fooList.add(Foo("Foo1", System.currentTimeMillis()))
        fooList.add(Foo("Foo2", System.currentTimeMillis()))
        fooList.add(Foo("Foo3", System.currentTimeMillis()))
        fooList.add(Foo("Foo4", System.currentTimeMillis()))
        fooList.add(Foo("Foo5", System.currentTimeMillis()))
        fooAdapter.setList(fooList)

    }
}