package com.example.data.repository

import com.example.data.local.QuizResultDao
import com.example.data.local.QuizResultEntity
import com.example.data.local.SeedNotesProvider
import com.example.data.local.StudyNoteDao
import com.example.data.local.toDomain
import com.example.data.local.toEntity
import com.example.model.QuizAttempt
import com.example.model.StudyNote
import com.example.model.Subject
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class NotesRepository(
    private val studyNoteDao: StudyNoteDao,
    private val quizResultDao: QuizResultDao
) {
    val allNotes: Flow<List<StudyNote>> = studyNoteDao.observeAllNotes()
        .map { entities -> entities.map { it.toDomain() } }

    val recentQuizAttempts: Flow<List<QuizAttempt>> = quizResultDao.observeRecentResults()
        .map { entities -> entities.map { it.toDomain() } }

    suspend fun ensureSeeded() {
        try {
            if (studyNoteDao.countNotes() == 0) {
                studyNoteDao.insertAll(SeedNotesProvider.defaultNotes())
            }
        } catch (e: CancellationException) {
            throw e
        } catch (_: Exception) {
            // Ignored on seed check; CRUD surfaces explicit errors
        }
    }

    suspend fun restoreStarterNotes(): Result<Unit> = runCatching {
        studyNoteDao.deleteAllNotes()
        studyNoteDao.insertAll(SeedNotesProvider.defaultNotes())
    }

    suspend fun addNote(note: StudyNote): Result<Unit> = runCatching {
        require(note.titleEn.isNotBlank() || note.titleBn.isNotBlank()) {
            "Note title is required."
        }
        require(note.contentEn.isNotBlank() || note.contentBn.isNotBlank()) {
            "Note content is required."
        }
        studyNoteDao.insertNote(note.copy(updatedAt = System.currentTimeMillis()).toEntity())
        Unit
    }

    suspend fun updateNote(note: StudyNote): Result<Unit> = runCatching {
        require(note.id > 0L) { "Invalid note ID." }
        require(note.titleEn.isNotBlank() || note.titleBn.isNotBlank()) {
            "Note title is required."
        }
        studyNoteDao.updateNote(note.copy(updatedAt = System.currentTimeMillis()).toEntity())
    }

    suspend fun deleteNote(noteId: Long): Result<Unit> = runCatching {
        studyNoteDao.deleteNoteById(noteId)
    }

    suspend fun toggleBookmark(noteId: Long, newBookmarked: Boolean): Result<Unit> = runCatching {
        studyNoteDao.setBookmarked(noteId, newBookmarked)
    }

    suspend fun saveQuizAttempt(
        classLevel: Int,
        subject: Subject,
        topic: String,
        score: Int,
        totalQuestions: Int
    ): Result<Unit> = runCatching {
        quizResultDao.insertResult(
            QuizResultEntity(
                classLevel = classLevel.coerceIn(1, 10),
                subjectId = subject.id,
                topic = topic.ifBlank { subject.labelEn },
                score = score,
                totalQuestions = totalQuestions
            )
        )
        Unit
    }
}
