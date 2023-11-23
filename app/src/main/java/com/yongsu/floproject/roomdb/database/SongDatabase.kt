package com.yongsu.floproject.roomdb.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.yongsu.floproject.roomdb.dao.AlbumDao
import com.yongsu.floproject.roomdb.dao.SongDao
import com.yongsu.floproject.roomdb.dao.UserDao
import com.yongsu.floproject.roomdb.entity.Album
import com.yongsu.floproject.roomdb.entity.Like
import com.yongsu.floproject.roomdb.entity.Song
import com.yongsu.floproject.roomdb.entity.User

@Database(entities = [Song::class, User::class, Album::class, Like::class], version = 5)
abstract class SongDatabase: RoomDatabase() {
    abstract fun songDao(): SongDao
    abstract fun userDao(): UserDao
    abstract fun albumDao(): AlbumDao

    companion object{
        private var instance: SongDatabase? = null

        // Migration 객체 정의
        val MIGRATION_1_2: Migration = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("ALTER TABLE SongTable ADD COLUMN albumIdx INTEGER")
            }
        }

        @Synchronized
        fun getInstance(context: Context): SongDatabase? {
            if(instance == null){
                synchronized(SongDatabase::class){
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        SongDatabase::class.java,
                        "song-database"
                    ).allowMainThreadQueries()
                        .addMigrations(MIGRATION_1_2)
                        .fallbackToDestructiveMigration()
                        .build()
                }
            }
            return instance
        }
    }
}