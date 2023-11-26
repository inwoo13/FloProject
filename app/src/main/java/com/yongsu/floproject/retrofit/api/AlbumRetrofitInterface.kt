package com.yongsu.floproject.retrofit.api

import com.yongsu.floproject.retrofit.response.AlbumResponse
import com.yongsu.floproject.retrofit.response.AlbumSongRes
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface AlbumRetrofitInterface {

    @GET("/albums")
    fun getAlbums(): Call<AlbumResponse>

    @GET("/albums/{albumIdx}")
    fun getAlbum(@Path("albumIdx") albumIdx: Int) : Call<AlbumSongRes>

}