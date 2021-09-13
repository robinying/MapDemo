package com.robin.commonUi.loadCallBack

import com.kingja.loadsir.callback.Callback
import com.robin.commonUi.R


class EmptyCallback : Callback() {
    override fun onCreateView(): Int {
        return R.layout.layout_empty
    }

}