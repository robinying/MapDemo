package com.robin.mapdemo.ui.test

import com.blankj.utilcode.util.LogUtils
import kotlin.properties.Delegates

class TestKotlin {

    fun sum(m: Int, n: Int, param: (x: Int, y: Int) -> Int): Int {
        return param.invoke(m, n)
    }

    fun main(args: Array<String>) {
        //函数类型作为参数
        val param: (x: Int, y: Int) -> Int = { x, y -> x + y }
        sum(1, 2, param)

        sum(3, 4) { x, y ->
            x + y
        }
    }

    fun sum(): (x: Int, y: Int) -> Int {
        return { x, y -> x + y }
    }

    private fun testIn(): Unit {
        val i = 8
        if (i in 0..9) {
            LogUtils.d("robinTest in")
        }

        if (i !in 0..9) {
            LogUtils.d("robinTest out")
        }
        name = "yingyubin"
        name = "robin"

    }

    private fun testVararg(vararg str: String?) {

    }

    private var name: String by Delegates.observable("oldValue") { property, oldValue, newValue ->
        LogUtils.d("robinTest ${property.name} 属性变化: $oldValue -> $newValue")
    }

    private var age: Int by Delegates.observable(0) { property, oldValue, newValue ->
        LogUtils.d("robinTest ${property.name} 属性变化: $oldValue -> $newValue")
    }

    var tagS: String by Delegates.notNull()

    fun init(tagS: String) {
        this.tagS = tagS
    }

    val fun1: (String, String) -> Unit = { s1, s2 ->
        LogUtils.d("robinTest $s1 and $s2")
    }

    //A.(B) -> C
    val fun2: String.(String) -> Unit = { s ->
        LogUtils.d("robinTest $this $s")
    }

}