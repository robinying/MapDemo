package com.robin.mapdemo.ui.view_pager

import android.view.View
import androidx.viewpager.widget.PagerAdapter
import android.view.ViewGroup

import android.widget.Toast


import android.content.Context
import android.widget.ImageView
import com.blankj.utilcode.util.ToastUtils
import com.robin.mapdemo.R


class BannerAdapter(ctx: Context, data: ArrayList<Int>) : PagerAdapter() {
    private val mData: ArrayList<Int> = data
    private val mContext: Context = ctx
    override fun getCount(): Int {
        return Int.MAX_VALUE // 返回数据的个数
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any { //子View显示
        val view = View.inflate(container.context, R.layout.item_banner, null)
        val imageView: ImageView = view.findViewById(R.id.iv_icon)
        imageView.setImageResource(mData[position % mData.size])
        imageView.setOnClickListener(View.OnClickListener {
            ToastUtils.showShort("当前条目：${position % mData.size}")
        })
        container.addView(view) //添加到父控件
        return view
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view === `object` // 过滤和缓存的作用
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as View) //从viewpager中移除掉
    }


}