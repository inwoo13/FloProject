package com.yongsu.floproject.roomdb.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.yongsu.floproject.roomdb.dao.AlbumDao
import com.yongsu.floproject.roomdb.entity.Album

@Database(entities = [Album::class], version = 1)
abstract class AlbumDatabase: RoomDatabase() {
    abstract fun albumDao(): AlbumDao

    companion object{
        private var instance: AlbumDatabase? = null

        @Synchronized
        fun getInstance(context: Context): AlbumDatabase?{
            if(instance == null){
                synchronized(AlbumDatabase::class){
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        AlbumDatabase::class.java,
                        "album-database"
                    ).allowMainThreadQueries()
                        .build()
                }
            }
            return instance
        }

    }
}