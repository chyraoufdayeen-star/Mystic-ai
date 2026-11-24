package com.example.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.model.QuizAttempt
import com.example.model.StudyNote
import com.example.model.Subject

@Entity(tableName = "study_notes")
data class StudyNoteEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0L,
    val titleEn: String,
    val titleBn: String,
    val summaryEn: String,
    val summaryBn: String,
    val contentEn: String,
    val contentBn: String,
    val classLevel: Int,
    val subjectId: String,
    val tagsCsv: String,
    val isBookmarked: Boolean = false,
    val updatedAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "quiz_results")
data class QuizResultEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0L,
    val classLevel: Int,
    val subjectId: String,
    val topic: String,
    val score: Int,
    val totalQuestions: Int,
    val createdAt: Long = System.currentTimeMillis()
)

fun StudyNoteEntity.toDomain(): StudyNote = StudyNote(
    id = id,
    titleEn = titleEn,
    titleBn = titleBn,
    summaryEn = summaryEn,
    summaryBn = summaryBn,
    contentEn = contentEn,
    contentBn = contentBn,
    classLevel = classLevel,
    subject = Subject.fromId(subjectId),
    tags = tagsCsv.split(",").map { it.trim() }.filter { it.isNotEmpty() },
    isBookmarked = isBookmarked,
    updatedAt = updatedAt
)

fun StudyNote.toEntity(): StudyNoteEntity = StudyNoteEntity(
    id = id,
    titleEn = titleEn,
    titleBn = titleBn,
    summaryEn = summaryEn,
    summaryBn = summaryBn,
    contentEn = contentEn,
    contentBn = contentBn,
    classLevel = classLevel.coerceIn(1, 10),
    subjectId = subject.id,
    tagsCsv = tags.joinToString(", "),
    isBookmarked = isBookmarked,
    updatedAt = updatedAt
)

fun QuizResultEntity.toDomain(): QuizAttempt = QuizAttempt(
    id = id,
    classLevel = classLevel,
    subject = Subject.fromId(subjectId),
    topic = topic,
    score = score,
    totalQuestions = totalQuestions,
    createdAt = createdAt
)
