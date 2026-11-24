package com.example.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface StudyNoteDao {
    @Query("SELECT * FROM study_notes ORDER BY classLevel ASC, updatedAt DESC")
    fun observeAllNotes(): Flow<List<StudyNoteEntity>>

    @Query("SELECT COUNT(*) FROM study_notes")
    suspend fun countNotes(): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNote(note: StudyNoteEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(notes: List<StudyNoteEntity>)

    @Update
    suspend fun updateNote(note: StudyNoteEntity)

    @Query("UPDATE study_notes SET isBookmarked = :bookmarked WHERE id = :noteId")
    suspend fun setBookmarked(noteId: Long, bookmarked: Boolean)

    @Query("DELETE FROM study_notes WHERE id = :noteId")
    suspend fun deleteNoteById(noteId: Long)

    @Query("DELETE FROM study_notes")
    suspend fun deleteAllNotes()
}

@Dao
interface QuizResultDao {
    @Query("SELECT * FROM quiz_results ORDER BY createdAt DESC LIMIT 25")
    fun observeRecentResults(): Flow<List<QuizResultEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertResult(result: QuizResultEntity): Long
}
