package com.thirfir.data.datasource.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.thirfir.data.datasource.local.dao.BookmarkDao
import com.thirfir.data.datasource.local.dao.KeywordDao
import com.thirfir.data.datasource.local.entity.BookmarkEntity
import com.thirfir.data.datasource.local.entity.KeywordEntity

@Database(entities = [KeywordEntity::class, BookmarkEntity::class], version = 1)
abstract class KReminderDatabase: RoomDatabase() {
    abstract fun keywordDao(): KeywordDao
    abstract fun bookmarkDao(): BookmarkDao

    companion object {
        @Volatile // 인스턴스가 메인 메모리를 바로 참조하여 인스턴스 중복 생성 방지
        private var INSTANCE: KReminderDatabase? = null

        fun getInstance(context: Context): KReminderDatabase =
            INSTANCE ?: synchronized(KReminderDatabase::class) {
                Room.databaseBuilder(
                    context.applicationContext,
                    KReminderDatabase::class.java,
                    "kreminder.db"
                ).build().also { INSTANCE = it }
            }
    }
}