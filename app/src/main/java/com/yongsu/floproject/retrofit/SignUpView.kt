package com.yongsu.floproject.retrofit

interface SignUpView {
    fun onSignUpSuccess()
    fun onSignUpFailure(code: Int, message: String)
}