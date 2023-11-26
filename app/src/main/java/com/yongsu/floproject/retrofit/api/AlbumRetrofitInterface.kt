package com.yongsu.floproject.retrofit.api

import com.yongsu.floproject.retrofit.response.AlbumResponse
import retrofit2.Call
import retrofit2.http.GET

interface AlbumRetrofitInterface {

    @GET("/albums")
    fun getAlbums(): Call<AlbumResponse>

}