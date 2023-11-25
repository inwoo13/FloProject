package com.yongsu.floproject.retrofit

import com.yongsu.floproject.retrofit.response.Result

interface AutoLoginView {
    fun onAutoSuccess()
    fun onAutoFailure(code: Int, message: String)
}