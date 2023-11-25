package com.yongsu.floproject.retrofit

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

// 여기서 맨 뒤에 '/'를 붙이지 않았다. 만약 여기서 '/'를 붙이면 api를 적을 때에는 붙이지 않아야 한다.
const val BASE_URL = "https://edu-api-test.softsquared.com"

// api를 사용하여 서버와 연결하는 작업을 할 때, 여러 화면에서 사용되기 때문에 함수로 정의
// retrofit 타입을 반환
fun getRetrofit(): Retrofit {

    val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    return retrofit
}