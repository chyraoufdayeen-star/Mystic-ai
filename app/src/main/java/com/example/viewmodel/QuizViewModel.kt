package com.example.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.repository.AiRepository
import com.example.data.repository.NotesRepository
import com.example.data.repository.PlaceholderAiRepository
import com.example.model.AppLanguage
import com.example.model.GeneratedQuiz
import com.example.model.Subject
import com.example.model.UiState
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class QuizRunnerState(
    val currentIndex: Int = 0,
    val selectedOptionIndex: Int? = null,
    val isAnswerSubmitted: Boolean = false,
    val correctCount: Int = 0,
    val isFinished: Boolean = false
)

class QuizViewModel(
    private val aiRepository: AiRepository = PlaceholderAiRepository()
) : ViewModel() {

    private val _selectedClass = MutableStateFlow(8)
    val selectedClass: StateFlow<Int> = _selectedClass.asStateFlow()

    private val _selectedSubject = MutableStateFlow(Subject.MATH)
    val selectedSubject: StateFlow<Subject> = _selectedSubject.asStateFlow()

    private val _topic = MutableStateFlow("")
    val topic: StateFlow<String> = _topic.asStateFlow()

    private val _questionCount = MutableStateFlow(5)
    val questionCount: StateFlow<Int> = _questionCount.asStateFlow()

    private val _difficulty = MutableStateFlow("Medium")
    val difficulty: StateFlow<String> = _difficulty.asStateFlow()

    private val _quizState = MutableStateFlow<UiState<GeneratedQuiz>>(UiState.Idle)
    val quizState: StateFlow<UiState<GeneratedQuiz>> = _quizState.asStateFlow()

    private val _runnerState = MutableStateFlow(QuizRunnerState())
    val runnerState: StateFlow<QuizRunnerState> = _runnerState.asStateFlow()

    fun setClassLevel(classLevel: Int) {
        _selectedClass.value = classLevel.coerceIn(1, 10)
    }

    fun setSubject(subject: Subject) {
        _selectedSubject.value = subject
    }

    fun setTopic(topic: String) {
        _topic.value = topic
    }

    fun setQuestionCount(count: Int) {
        _questionCount.value = count
    }

    fun setDifficulty(difficulty: String) {
        _difficulty.value = difficulty
    }

    fun prefillAndGenerate(
        classLevel: Int,
        subject: Subject,
        topic: String,
        language: AppLanguage
    ) {
        _selectedClass.value = classLevel.coerceIn(1, 10)
        _selectedSubject.value = subject
        _topic.value = topic
        generateQuiz(language)
    }

    fun generateQuiz(language: AppLanguage) {
        viewModelScope.launch {
            _quizState.value = UiState.Loading
            _runnerState.value = QuizRunnerState()
            try {
                aiRepository.generateQuiz(
                    classLevel = _selectedClass.value,
                    subject = _selectedSubject.value,
                    topic = _topic.value,
                    questionCount = _questionCount.value,
                    difficulty = _difficulty.value,
                    language = language
                ).onSuccess { quiz ->
                    _quizState.value = UiState.Success(quiz)
                }.onFailure { err ->
                    _quizState.value = UiState.Error(err.localizedMessage ?: "Failed to generate quiz.")
                }
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                _quizState.value = UiState.Error(e.localizedMessage ?: "Unexpected error.")
            }
        }
    }

    fun selectOption(index: Int) {
        if (!_runnerState.value.isAnswerSubmitted) {
            _runnerState.value = _runnerState.value.copy(selectedOptionIndex = index)
        }
    }

    fun submitCurrentAnswer(quiz: GeneratedQuiz) {
        val state = _runnerState.value
        val selected = state.selectedOptionIndex ?: return
        if (state.isAnswerSubmitted) return
        val currentQ = quiz.questions.getOrNull(state.currentIndex) ?: return
        val isCorrect = selected == currentQ.correctIndex
        _runnerState.value = state.copy(
            isAnswerSubmitted = true,
            correctCount = if (isCorrect) state.correctCount + 1 else state.correctCount
        )
    }

    fun nextOrFinish(quiz: GeneratedQuiz, notesRepository: NotesRepository) {
        val state = _runnerState.value
        if (state.currentIndex + 1 < quiz.questions.size) {
            _runnerState.value = QuizRunnerState(
                currentIndex = state.currentIndex + 1,
                selectedOptionIndex = null,
                isAnswerSubmitted = false,
                correctCount = state.correctCount,
                isFinished = false
            )
        } else {
            _runnerState.value = state.copy(isFinished = true)
            viewModelScope.launch {
                notesRepository.saveQuizAttempt(
                    classLevel = quiz.classLevel,
                    subject = quiz.subject,
                    topic = quiz.topic,
                    score = state.correctCount,
                    totalQuestions = quiz.questions.size
                )
            }
        }
    }

    fun resetQuiz() {
        _quizState.value = UiState.Idle
        _runnerState.value = QuizRunnerState()
    }
}
