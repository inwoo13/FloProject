package com.yongsu.floproject.retrofit

import com.yongsu.floproject.retrofit.response.Result

interface LoginView {
    fun onLoginSuccess(code: Int, result: Result)
    fun onLoginFailure(code: Int, message: String)
}