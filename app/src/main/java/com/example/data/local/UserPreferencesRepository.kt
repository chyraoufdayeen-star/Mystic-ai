package com.example.data.local

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.model.AppLanguage
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.security.MessageDigest

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "mystic_prefs")

data class UserPreferences(
    val language: AppLanguage = AppLanguage.EN,
    val preferredClass: Int = 8,
    val isDarkTheme: Boolean = true,
    val isAdminUnlocked: Boolean = false
)

class UserPreferencesRepository(private val context: Context) {
    private object Keys {
        val LANGUAGE = stringPreferencesKey("app_language")
        val PREFERRED_CLASS = intPreferencesKey("preferred_class")
        val DARK_THEME = booleanPreferencesKey("dark_theme")
        val ADMIN_UNLOCKED = booleanPreferencesKey("admin_unlocked")
        val ADMIN_PIN_HASH = stringPreferencesKey("admin_pin_hash")
    }

    val preferencesFlow: Flow<UserPreferences> = context.dataStore.data.map { prefs ->
        val langStr = prefs[Keys.LANGUAGE] ?: AppLanguage.EN.name
        val lang = AppLanguage.entries.firstOrNull { it.name == langStr } ?: AppLanguage.EN
        UserPreferences(
            language = lang,
            preferredClass = (prefs[Keys.PREFERRED_CLASS] ?: 8).coerceIn(1, 10),
            isDarkTheme = prefs[Keys.DARK_THEME] ?: true,
            isAdminUnlocked = prefs[Keys.ADMIN_UNLOCKED] ?: false
        )
    }

    suspend fun setLanguage(language: AppLanguage) {
        context.dataStore.edit { it[Keys.LANGUAGE] = language.name }
    }

    suspend fun setPreferredClass(classLevel: Int) {
        context.dataStore.edit { it[Keys.PREFERRED_CLASS] = classLevel.coerceIn(1, 10) }
    }

    suspend fun setDarkTheme(enabled: Boolean) {
        context.dataStore.edit { it[Keys.DARK_THEME] = enabled }
    }

    suspend fun verifyAndUnlockAdmin(pin: String): Boolean {
        val enteredHash = sha256(pin.trim())
        var matched = false
        context.dataStore.edit { prefs ->
            val storedHash = prefs[Keys.ADMIN_PIN_HASH] ?: DEFAULT_DEMO_PIN_HASH
            if (enteredHash == storedHash) {
                prefs[Keys.ADMIN_UNLOCKED] = true
                matched = true
            }
        }
        return matched
    }

    suspend fun lockAdmin() {
        context.dataStore.edit { it[Keys.ADMIN_UNLOCKED] = false }
    }

    private fun sha256(input: String): String {
        val bytes = MessageDigest.getInstance("SHA-256").digest(input.toByteArray())
        return bytes.joinToString("") { "%02x".format(it) }
    }

    companion object {
        // SHA-256 of demo preview PIN "2026"
        private const val DEFAULT_DEMO_PIN_HASH =
            "b08948e039bc4282b872a30a45282083a222d02875e534785b02344aa903335a"
    }
}
