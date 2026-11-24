package com.example.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [StudyNoteEntity::class, QuizResultEntity::class],
    version = 1,
    exportSchema = false
)
abstract class MysticDatabase : RoomDatabase() {
    abstract fun studyNoteDao(): StudyNoteDao
    abstract fun quizResultDao(): QuizResultDao

    companion object {
        @Volatile
        private var INSTANCE: MysticDatabase? = null

        fun getInstance(context: Context): MysticDatabase {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    MysticDatabase::class.java,
                    "mystic_ai_database"
                ).build().also { INSTANCE = it }
            }
        }
    }
}
