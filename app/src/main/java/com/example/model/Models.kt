package com.example.model

import androidx.compose.ui.graphics.Color
import com.example.ui.theme.SubjectBangla
import com.example.ui.theme.SubjectEnglish
import com.example.ui.theme.SubjectIct
import com.example.ui.theme.SubjectMath
import com.example.ui.theme.SubjectScience

enum class AppLanguage {
    EN,
    BN
}

enum class Subject(
    val id: String,
    val labelEn: String,
    val labelBn: String,
    val accentColor: Color
) {
    MATH("MATH", "Mathematics", "গণিত", SubjectMath),
    ENGLISH("ENGLISH", "English", "ইংরেজি", SubjectEnglish),
    BANGLA("BANGLA", "Bangla", "বাংলা", SubjectBangla),
    SCIENCE("SCIENCE", "Science", "বিজ্ঞান", SubjectScience),
    ICT("ICT", "ICT", "তথ্য ও যোগাযোগ প্রযুক্তি", SubjectIct);

    fun label(language: AppLanguage): String = if (language == AppLanguage.BN) labelBn else labelEn

    companion object {
        fun fromId(id: String): Subject = entries.firstOrNull { it.id == id } ?: MATH
    }
}

enum class AiToolTab {
    ASSISTANT,
    MATH_SOLVER,
    GRAMMAR_TUTOR,
    HOMEWORK_HELPER
}

data class StudyNote(
    val id: Long = 0L,
    val titleEn: String,
    val titleBn: String,
    val summaryEn: String,
    val summaryBn: String,
    val contentEn: String,
    val contentBn: String,
    val classLevel: Int,
    val subject: Subject,
    val tags: List<String> = emptyList(),
    val isBookmarked: Boolean = false,
    val updatedAt: Long = System.currentTimeMillis()
) {
    fun localizedTitle(language: AppLanguage): String =
        if (language == AppLanguage.BN && titleBn.isNotBlank()) titleBn else titleEn

    fun localizedSummary(language: AppLanguage): String =
        if (language == AppLanguage.BN && summaryBn.isNotBlank()) summaryBn else summaryEn

    fun localizedContent(language: AppLanguage): String =
        if (language == AppLanguage.BN && contentBn.isNotBlank()) contentBn else contentEn
}

data class QuizAttempt(
    val id: Long = 0L,
    val classLevel: Int,
    val subject: Subject,
    val topic: String,
    val score: Int,
    val totalQuestions: Int,
    val createdAt: Long = System.currentTimeMillis()
)

data class ChatMessage(
    val id: String,
    val isUser: Boolean,
    val text: String,
    val hasImageAttachment: Boolean = false,
    val isSamplePreview: Boolean = false
)

data class MathSolution(
    val problem: String,
    val steps: List<String>,
    val finalAnswer: String,
    val conceptTip: String,
    val isSamplePreview: Boolean = true
)

data class GrammarMistake(
    val issue: String,
    val correction: String,
    val rule: String
)

data class GrammarAnalysis(
    val originalText: String,
    val correctedText: String,
    val scorePercent: Int,
    val mistakes: List<GrammarMistake>,
    val learningTip: String,
    val isSamplePreview: Boolean = true
)

data class HomeworkHelpResult(
    val question: String,
    val explanationSteps: List<String>,
    val keyTakeaway: String,
    val practiceQuestion: String,
    val isSamplePreview: Boolean = true
)

data class QuizQuestion(
    val question: String,
    val options: List<String>,
    val correctIndex: Int,
    val explanation: String
)

data class GeneratedQuiz(
    val classLevel: Int,
    val subject: Subject,
    val topic: String,
    val questions: List<QuizQuestion>,
    val isSamplePreview: Boolean = true
)

sealed interface UiState<out T> {
    data object Idle : UiState<Nothing>
    data object Loading : UiState<Nothing>
    data class Success<T>(val data: T) : UiState<T>
    data class Error(val message: String) : UiState<Nothing>
}
