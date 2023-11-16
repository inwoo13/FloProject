package com.yongsu.floproject.roomdb.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

// 제목, 가수명, 재생시간, 현재 재생시간, 재생 되고 있는지, 사진,

@Entity(tableName = "SongTable")
data class Song(
    var title : String = "",
    var singer : String = "",
    var second : Int = 0,
    var playTime : Int = 0,
    var isPlaying : Boolean = false,
    var music: String = "",
    var coverImg: Int? = null,
    var isLike: Boolean = false,
    var albumIdx: Int? = null
){
    @PrimaryKey(autoGenerate = true) var id: Int = 0
}
