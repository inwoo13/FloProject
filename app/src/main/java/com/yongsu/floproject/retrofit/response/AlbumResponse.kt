package com.yongsu.floproject.retrofit.response

import com.google.gson.annotations.SerializedName

data class AlbumResponse(
    @SerializedName("isSuccess") val isSuccess: Boolean,
    @SerializedName("code") val code: Int,
    @SerializedName("message") val message: String,
    @SerializedName("result") val result: AlbumResult
)

data class AlbumResult(
    @SerializedName("albums") val albums: List<FloChartAlbums>
)

data class FloChartAlbums(
    @SerializedName("albumIdx") val albumIdx: Int,
    @SerializedName("title") val title:String,
    @SerializedName("singer") val singer: String,
    @SerializedName("coverImgUrl") val coverImgUrl : String
)

