package com.example.viewmodel

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.repository.AiRepository
import com.example.data.repository.PlaceholderAiRepository
import com.example.model.AiToolTab
import com.example.model.AppLanguage
import com.example.model.ChatMessage
import com.example.model.GrammarAnalysis
import com.example.model.HomeworkHelpResult
import com.example.model.MathSolution
import com.example.model.Subject
import com.example.model.UiState
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.UUID

class AiToolsViewModel(
    private val aiRepository: AiRepository = PlaceholderAiRepository()
) : ViewModel() {

    private val _activeTab = MutableStateFlow(AiToolTab.ASSISTANT)
    val activeTab: StateFlow<AiToolTab> = _activeTab.asStateFlow()

    // 1. AI Study Assistant
    private val _chatMessages = MutableStateFlow<List<ChatMessage>>(emptyList())
    val chatMessages: StateFlow<List<ChatMessage>> = _chatMessages.asStateFlow()

    private val _assistantInput = MutableStateFlow("")
    val assistantInput: StateFlow<String> = _assistantInput.asStateFlow()

    private val _assistantSubject = MutableStateFlow<Subject?>(null)
    val assistantSubject: StateFlow<Subject?> = _assistantSubject.asStateFlow()

    private val _assistantPhotoUri = MutableStateFlow<Uri?>(null)
    val assistantPhotoUri: StateFlow<Uri?> = _assistantPhotoUri.asStateFlow()

    private val _assistantState = MutableStateFlow<UiState<Unit>>(UiState.Idle)
    val assistantState: StateFlow<UiState<Unit>> = _assistantState.asStateFlow()

    // 2. AI Math Solver
    private val _mathInput = MutableStateFlow("")
    val mathInput: StateFlow<String> = _mathInput.asStateFlow()

    private val _mathPhotoUri = MutableStateFlow<Uri?>(null)
    val mathPhotoUri: StateFlow<Uri?> = _mathPhotoUri.asStateFlow()

    private val _mathState = MutableStateFlow<UiState<MathSolution>>(UiState.Idle)
    val mathState: StateFlow<UiState<MathSolution>> = _mathState.asStateFlow()

    // 3. English Grammar Tutor
    private val _grammarInput = MutableStateFlow("")
    val grammarInput: StateFlow<String> = _grammarInput.asStateFlow()

    private val _grammarState = MutableStateFlow<UiState<GrammarAnalysis>>(UiState.Idle)
    val grammarState: StateFlow<UiState<GrammarAnalysis>> = _grammarState.asStateFlow()

    // 4. Homework Helper
    private val _homeworkInput = MutableStateFlow("")
    val homeworkInput: StateFlow<String> = _homeworkInput.asStateFlow()

    private val _homeworkSubject = MutableStateFlow(Subject.SCIENCE)
    val homeworkSubject: StateFlow<Subject> = _homeworkSubject.asStateFlow()

    private val _homeworkHintOnly = MutableStateFlow(false)
    val homeworkHintOnly: StateFlow<Boolean> = _homeworkHintOnly.asStateFlow()

    private val _homeworkPhotoUri = MutableStateFlow<Uri?>(null)
    val homeworkPhotoUri: StateFlow<Uri?> = _homeworkPhotoUri.asStateFlow()

    private val _homeworkState = MutableStateFlow<UiState<HomeworkHelpResult>>(UiState.Idle)
    val homeworkState: StateFlow<UiState<HomeworkHelpResult>> = _homeworkState.asStateFlow()

    fun selectTab(tab: AiToolTab) {
        _activeTab.value = tab
    }

    fun openAssistantWithPrompt(prompt: String, subject: Subject? = null) {
        _activeTab.value = AiToolTab.ASSISTANT
        _assistantInput.value = prompt
        _assistantSubject.value = subject
    }

    fun updateAssistantInput(text: String) {
        _assistantInput.value = text
    }

    fun selectAssistantSubject(subject: Subject?) {
        _assistantSubject.value = subject
    }

    fun setAssistantPhoto(uri: Uri?) {
        _assistantPhotoUri.value = uri
    }

    fun clearChat() {
        _chatMessages.value = emptyList()
        _assistantState.value = UiState.Idle
        _assistantPhotoUri.value = null
    }

    fun sendAssistantMessage(classLevel: Int, language: AppLanguage) {
        val text = _assistantInput.value.trim()
        val hasImage = _assistantPhotoUri.value != null
        if (text.isEmpty() && !hasImage) {
            _assistantState.value = UiState.Error(
                if (language == AppLanguage.BN) "অনুগ্রহ করে একটি প্রশ্ন লিখুন।" else "Please enter a study question."
            )
            return
        }

        val userMsg = ChatMessage(
            id = UUID.randomUUID().toString(),
            isUser = true,
            text = text.ifEmpty { if (language == AppLanguage.BN) "[ছবি সংযুক্ত প্রশ্ন]" else "[Photo question attached]" },
            hasImageAttachment = hasImage
        )
        _chatMessages.value = _chatMessages.value + userMsg
        _assistantInput.value = ""
        _assistantPhotoUri.value = null

        viewModelScope.launch {
            _assistantState.value = UiState.Loading
            try {
                val result = aiRepository.sendStudyAssistantMessage(
                    prompt = text,
                    classLevel = classLevel,
                    subject = _assistantSubject.value,
                    language = language,
                    hasImage = hasImage
                )
                result.onSuccess { reply ->
                    val aiMsg = ChatMessage(
                        id = UUID.randomUUID().toString(),
                        isUser = false,
                        text = reply,
                        isSamplePreview = true
                    )
                    _chatMessages.value = _chatMessages.value + aiMsg
                    _assistantState.value = UiState.Success(Unit)
                }.onFailure { err ->
                    _assistantState.value = UiState.Error(
                        err.localizedMessage ?: "Could not generate study response."
                    )
                }
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                _assistantState.value = UiState.Error(e.localizedMessage ?: "Unexpected error.")
            }
        }
    }

    fun updateMathInput(text: String) {
        _mathInput.value = text
    }

    fun setMathPhoto(uri: Uri?) {
        _mathPhotoUri.value = uri
    }

    fun solveMath(classLevel: Int, language: AppLanguage) {
        val text = _mathInput.value.trim()
        val hasImage = _mathPhotoUri.value != null
        viewModelScope.launch {
            _mathState.value = UiState.Loading
            try {
                aiRepository.solveMathProblem(text, classLevel, language, hasImage)
                    .onSuccess { _mathState.value = UiState.Success(it) }
                    .onFailure { _mathState.value = UiState.Error(it.localizedMessage ?: "Math solving failed.") }
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                _mathState.value = UiState.Error(e.localizedMessage ?: "Unexpected error.")
            }
        }
    }

    fun updateGrammarInput(text: String) {
        _grammarInput.value = text
    }

    fun analyzeGrammar(classLevel: Int, language: AppLanguage) {
        val text = _grammarInput.value.trim()
        viewModelScope.launch {
            _grammarState.value = UiState.Loading
            try {
                aiRepository.analyzeEnglishGrammar(text, classLevel, language)
                    .onSuccess { _grammarState.value = UiState.Success(it) }
                    .onFailure { _grammarState.value = UiState.Error(it.localizedMessage ?: "Grammar check failed.") }
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                _grammarState.value = UiState.Error(e.localizedMessage ?: "Unexpected error.")
            }
        }
    }

    fun updateHomeworkInput(text: String) {
        _homeworkInput.value = text
    }

    fun selectHomeworkSubject(subject: Subject) {
        _homeworkSubject.value = subject
    }

    fun setHomeworkHintOnly(hintOnly: Boolean) {
        _homeworkHintOnly.value = hintOnly
    }

    fun setHomeworkPhoto(uri: Uri?) {
        _homeworkPhotoUri.value = uri
    }

    fun requestHomeworkHelp(classLevel: Int, language: AppLanguage) {
        val text = _homeworkInput.value.trim()
        val hasImage = _homeworkPhotoUri.value != null
        viewModelScope.launch {
            _homeworkState.value = UiState.Loading
            try {
                aiRepository.getHomeworkHelp(
                    question = text,
                    classLevel = classLevel,
                    subject = _homeworkSubject.value,
                    hintOnly = _homeworkHintOnly.value,
                    language = language,
                    hasImage = hasImage
                ).onSuccess { _homeworkState.value = UiState.Success(it) }
                    .onFailure { _homeworkState.value = UiState.Error(it.localizedMessage ?: "Homework help failed.") }
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                _homeworkState.value = UiState.Error(e.localizedMessage ?: "Unexpected error.")
            }
        }
    }
}
