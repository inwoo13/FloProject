package com.yongsu.floproject.roomdb.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.yongsu.floproject.roomdb.entity.Album

@Dao
interface AlbumDao {
    @Insert
    fun insertAlbum(album: Album)

    @Update
    fun updateAlbum(album: Album)

    @Delete
    fun deleteAlbum(album: Album)

    @Query("SELECT * FROM AlbumTable")
    fun getAlbums(): List<Album>
}