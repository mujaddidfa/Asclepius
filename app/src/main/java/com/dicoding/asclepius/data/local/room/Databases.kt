package com.dicoding.asclepius.data.local.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.dicoding.asclepius.data.local.entity.HistoryEntity
import com.dicoding.asclepius.data.local.entity.NewsEntity

@Database(entities = [NewsEntity::class, HistoryEntity::class], version = 3, exportSchema = false)
@TypeConverters(Converters::class)
abstract class Databases : RoomDatabase() {
    abstract fun newsDao(): NewsDao
    abstract fun historyDao(): HistoryDao

    companion object {
        @Volatile
        private var INSTANCE: Databases? = null

        fun getInstance(context: Context): Databases {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    Databases::class.java,
                    "databases"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}