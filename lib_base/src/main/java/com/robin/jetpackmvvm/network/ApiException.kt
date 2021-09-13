package com.robin.jetpackmvvm.network

class ApiException(val msg: String, val status: Int) :
    Throwable()