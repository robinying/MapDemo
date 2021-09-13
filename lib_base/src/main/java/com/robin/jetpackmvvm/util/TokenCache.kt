package com.robin.jetpackmvvm.util


import com.tencent.mmkv.MMKV

object TokenCache {

    fun setToken(token: String?) {
        val kv = MMKV.mmkvWithID("app")
        if (token == null) {
            kv.encode("token", "")
        } else {
            kv.encode("token", token)
        }
    }

    fun getToken(): String? {
        val kv = MMKV.mmkvWithID("app")
        return kv.decodeString("token")
    }

}