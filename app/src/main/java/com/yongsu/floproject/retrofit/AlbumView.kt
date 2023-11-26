package com.yongsu.floproject.retrofit

import com.yongsu.floproject.retrofit.response.AlbumResult

interface AlbumView {
    fun onGetAlbumLoading()
    fun onGetAlbumSuccess(code: Int, result: AlbumResult)
    fun onGetAlbumFailure(code: Int, message: String)
}