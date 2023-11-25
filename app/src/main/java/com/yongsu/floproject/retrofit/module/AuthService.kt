package com.yongsu.floproject.retrofit.module

import android.util.Log
import android.view.View
import com.yongsu.floproject.retrofit.SignUpView
import com.yongsu.floproject.retrofit.api.AuthRetrofitInstance
import com.yongsu.floproject.retrofit.getRetrofit
import com.yongsu.floproject.retrofit.response.AuthResponse
import com.yongsu.floproject.roomdb.entity.User
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AuthService {
    private lateinit var signUpView: SignUpView

    fun setSignUpView(signUpView: SignUpView){
        this.signUpView = signUpView
    }

    fun signUp(user: User){

        val authService = getRetrofit().create(AuthRetrofitInstance::class.java)
        authService.signUp(user).enqueue(object: Callback<AuthResponse> {
            // 응답이 왔을 때 처리하는 부분
            override fun onResponse(call: Call<AuthResponse>, response: Response<AuthResponse>) {
                Log.d("SIGNUP-ACT/RESPONSE", response.toString())
                val resp: AuthResponse = response.body()!!
                when(resp.code){
                    1000 -> signUpView.onSignUpSuccess()
                    else -> signUpView.onSignUpFailure(resp.code, resp.message)
                }
            }

            // 네트워크 연결 자체가 실패했을 때 처리하는 부분
            override fun onFailure(call: Call<AuthResponse>, t: Throwable) {
                Log.d("SIGNUP-ACT/RESPONSE", t.message.toString())
            }

        })

        Log.d("SIGNUP-ACT/RESPONSE", "hello")
    }
}