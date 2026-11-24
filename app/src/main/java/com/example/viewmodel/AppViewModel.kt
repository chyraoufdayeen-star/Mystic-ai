package com.example.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.local.MysticDatabase
import com.example.data.local.UserPreferences
import com.example.data.local.UserPreferencesRepository
import com.example.data.repository.NotesRepository
import com.example.model.AppLanguage
import com.example.model.QuizAttempt
import com.example.model.StudyNote
import com.example.model.Subject
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

data class NotesFilterState(
    val searchQuery: String = "",
    val classFilter: Int? = null,
    val subjectFilter: Subject? = null,
    val bookmarkedOnly: Boolean = false
)

class AppViewModel(application: Application) : AndroidViewModel(application) {
    private val db = MysticDatabase.getInstance(application)
    val notesRepository = NotesRepository(db.studyNoteDao(), db.quizResultDao())
    private val prefsRepository = UserPreferencesRepository(application)

    val preferences: StateFlow<UserPreferences> = prefsRepository.preferencesFlow
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000L),
            initialValue = UserPreferences()
        )

    val allNotes: StateFlow<List<StudyNote>> = notesRepository.allNotes
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000L),
            initialValue = emptyList()
        )

    val recentQuizzes: StateFlow<List<QuizAttempt>> = notesRepository.recentQuizAttempts
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000L),
            initialValue = emptyList()
        )

    private val _filterState = MutableStateFlow(NotesFilterState())
    val filterState: StateFlow<NotesFilterState> = _filterState.asStateFlow()

    val filteredNotes: StateFlow<List<StudyNote>> = combine(allNotes, _filterState) { notes, filter ->
        notes.filter { note ->
            val matchesClass = filter.classFilter == null || note.classLevel == filter.classFilter
            val matchesSubject = filter.subjectFilter == null || note.subject == filter.subjectFilter
            val matchesBookmark = !filter.bookmarkedOnly || note.isBookmarked
            val query = filter.searchQuery.trim().lowercase()
            val matchesQuery = query.isEmpty() ||
                note.titleEn.lowercase().contains(query) ||
                note.titleBn.lowercase().contains(query) ||
                note.summaryEn.lowercase().contains(query) ||
                note.summaryBn.lowercase().contains(query) ||
                note.contentEn.lowercase().contains(query) ||
                note.contentBn.lowercase().contains(query) ||
                note.tags.any { it.lowercase().contains(query) }
            matchesClass && matchesSubject && matchesBookmark && matchesQuery
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000L),
        initialValue = emptyList()
    )

    private val _adminPinError = MutableStateFlow<String?>(null)
    val adminPinError: StateFlow<String?> = _adminPinError.asStateFlow()

    private val _statusMessage = MutableStateFlow<String?>(null)
    val statusMessage: StateFlow<String?> = _statusMessage.asStateFlow()

    init {
        viewModelScope.launch {
            notesRepository.ensureSeeded()
        }
    }

    fun toggleLanguage() {
        viewModelScope.launch {
            val next = if (preferences.value.language == AppLanguage.EN) AppLanguage.BN else AppLanguage.EN
            prefsRepository.setLanguage(next)
        }
    }

    fun setLanguage(language: AppLanguage) {
        viewModelScope.launch { prefsRepository.setLanguage(language) }
    }

    fun setPreferredClass(classLevel: Int) {
        viewModelScope.launch { prefsRepository.setPreferredClass(classLevel) }
    }

    fun setDarkTheme(enabled: Boolean) {
        viewModelScope.launch { prefsRepository.setDarkTheme(enabled) }
    }

    fun updateSearchQuery(query: String) {
        _filterState.value = _filterState.value.copy(searchQuery = query)
    }

    fun setClassFilter(classLevel: Int?) {
        _filterState.value = _filterState.value.copy(classFilter = classLevel)
    }

    fun setSubjectFilter(subject: Subject?) {
        _filterState.value = _filterState.value.copy(subjectFilter = subject)
    }

    fun toggleBookmarkedOnly() {
        _filterState.value = _filterState.value.copy(bookmarkedOnly = !_filterState.value.bookmarkedOnly)
    }

    fun applyQuickFilter(classLevel: Int? = null, subject: Subject? = null) {
        _filterState.value = NotesFilterState(
            searchQuery = "",
            classFilter = classLevel,
            subjectFilter = subject,
            bookmarkedOnly = false
        )
    }

    fun toggleBookmark(note: StudyNote) {
        viewModelScope.launch {
            notesRepository.toggleBookmark(note.id, !note.isBookmarked)
        }
    }

    fun unlockAdmin(pin: String, invalidMessage: String) {
        viewModelScope.launch {
            try {
                val ok = prefsRepository.verifyAndUnlockAdmin(pin)
                _adminPinError.value = if (ok) null else invalidMessage
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                _adminPinError.value = e.localizedMessage ?: invalidMessage
            }
        }
    }

    fun lockAdmin() {
        viewModelScope.launch {
            _adminPinError.value = null
            prefsRepository.lockAdmin()
        }
    }

    fun clearAdminPinError() {
        _adminPinError.value = null
    }

    fun saveNote(note: StudyNote, onSaved: () -> Unit = {}) {
        viewModelScope.launch {
            val result = if (note.id == 0L) {
                notesRepository.addNote(note)
            } else {
                notesRepository.updateNote(note)
            }
            result.onSuccess {
                _statusMessage.value = if (preferences.value.language == AppLanguage.BN) {
                    "নোট সংরক্ষণ করা হয়েছে।"
                } else {
                    "Study note saved."
                }
                onSaved()
            }.onFailure { err ->
                _statusMessage.value = err.localizedMessage ?: "Failed to save note."
            }
        }
    }

    fun deleteNote(noteId: Long) {
        viewModelScope.launch {
            notesRepository.deleteNote(noteId)
                .onSuccess {
                    _statusMessage.value = if (preferences.value.language == AppLanguage.BN) {
                        "নোট মুছে ফেলা হয়েছে।"
                    } else {
                        "Study note deleted."
                    }
                }
                .onFailure { err ->
                    _statusMessage.value = err.localizedMessage ?: "Failed to delete note."
                }
        }
    }

    fun restoreStarterNotes() {
        viewModelScope.launch {
            notesRepository.restoreStarterNotes()
                .onSuccess {
                    _statusMessage.value = if (preferences.value.language == AppLanguage.BN) {
                        "প্রাথমিক নোট রিস্টোর করা হয়েছে।"
                    } else {
                        "Starter study notes restored."
                    }
                }
                .onFailure { err ->
                    _statusMessage.value = err.localizedMessage ?: "Failed to restore starter notes."
                }
        }
    }

    fun clearStatusMessage() {
        _statusMessage.value = null
    }
}
