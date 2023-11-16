package com.yongsu.floproject.roomdb.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.yongsu.floproject.roomdb.entity.Song

@Dao
interface SongDao {
    @Insert
    fun insert(song: Song)

    @Update
    fun update(song: Song)

    @Delete
    fun delete(song: Song)

    @Query("SELECT * FROM SongTable")
    fun getSongs(): List<Song>

    @Query("SELECT * FROM SongTable WHERE id = :id")
    fun getSong(id: Int): Song

    @Query("SELECT * FROM SongTable WHERE albumIdx = :id")
    fun getSongsByalbumIdx(id: Int): List<Song>

    @Query("UPDATE SongTable SET isLike = :isLike WHERE id = :id")
    fun updateIsLikeById(isLike: Boolean, id: Int)

    @Query("UPDATE SongTable SET isLike = 0 WHERE isLike = 1")
    fun updateIsLikeAllFalse()

    @Query("SELECT * FROM SongTable WHERE isLike = :isLike")
    fun getLikedSong(isLike: Boolean): List<Song>

    @Query("DELETE FROM SongTable")
    fun deleteAllSongs()
}