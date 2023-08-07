package com.thirfir.data.datasource.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.thirfir.data.datasource.local.dao.KeywordDao
import com.thirfir.data.datasource.local.entitiy.KeywordEntity

@Database(entities = [KeywordEntity::class], version = 1)
abstract class KReminderDatabase: RoomDatabase() {
    abstract fun keywordDao(): KeywordDao

    companion object {
        private var INSTANCE: KReminderDatabase? = null

        fun getInstance(context: Context): KReminderDatabase? {
            if (INSTANCE == null) {
                synchronized(KReminderDatabase::class) {
                    INSTANCE = Room.databaseBuilder(
                        context.applicationContext,
                        KReminderDatabase::class.java,
                        "kreminder.db"
                    ).build()
                }
            }
            return INSTANCE
        }
    }
}