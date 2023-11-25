package com.yongsu.floproject.retrofit.api

import com.yongsu.floproject.R
import com.yongsu.floproject.retrofit.response.AuthResponse
import com.yongsu.floproject.roomdb.entity.User
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.POST

interface AuthRetrofitInstance {

    @POST("/users")
    fun signUp(@Body user: User): Call<AuthResponse>

    @POST("/users/login")
    fun login(@Body user: User): Call<AuthResponse>

   @GET("/users/auto-login")
    fun autoLogin(@Header("X-ACCESS-TOKEN") token: String): Call<AuthResponse>
}