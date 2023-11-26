package com.yongsu.floproject.retrofit

import com.yongsu.floproject.retrofit.response.AlbumResult
import com.yongsu.floproject.retrofit.response.AlbumSongRes

interface AlbumSongView {

    fun onGetAlbumSongLoading()
    fun onGetAlbumSongSuccess(code: Int, result: AlbumSongRes)
    fun onGetAlbumSongFailure(code: Int, message: String)

}