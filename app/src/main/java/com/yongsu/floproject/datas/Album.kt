package com.yongsu.floproject.datas

data class Album(
    var title: String? = "",
    var singer: String? = "",
    var coverImg: Int? = null,
    var songs: ArrayList<Song>? = null  // 앨범이기 때문에 수록곡들을 넣는 list
)
