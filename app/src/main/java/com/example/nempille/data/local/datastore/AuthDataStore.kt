package com.example.nempille.data.local.datastore

import android.content.Context
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.nempille.domain.model.UserRole
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

private val Context.authDataStore by preferencesDataStore(name = "auth_prefs")

/**
 * CONCEPT: DataStore (Asynchronous Preferences)
 * 
 * How it works:
 * DataStore provides a safe and asynchronous way to store simple 
 * key-value pairs, replacing SharedPreferences. It uses Flow to 
 * expose data, ensuring that reads are non-blocking and UI updates 
 * are reactive.
 */
class AuthDataStore @Inject constructor(
    @ApplicationContext private val context: Context
) {

    private object Keys {
        val USER_ID = intPreferencesKey("user_id")
        val USER_ROLE = stringPreferencesKey("user_role")
        val IS_LOGGED_IN = booleanPreferencesKey("is_logged_in")
    }

    suspend fun saveSession(userId: Int, role: UserRole) {
        context.authDataStore.edit { prefs ->
            prefs[Keys.USER_ID] = userId
            prefs[Keys.USER_ROLE] = role.name
            prefs[Keys.IS_LOGGED_IN] = true
        }
    }

    suspend fun clearSession() {
        context.authDataStore.edit { prefs->
            prefs.clear()
        }
    }

    fun userIdFlow(): Flow<Int?> = 
        context.authDataStore.data.map { prefs ->
            prefs[Keys.USER_ID]
        }

    fun userRoleFlow(): Flow<UserRole?> =
        context.authDataStore.data.map { prefs ->
            prefs[Keys.USER_ROLE]?.let { UserRole.valueOf(it) }
        }

    fun isLoggedInFlow(): Flow<Boolean> =
        context.authDataStore.data.map { prefs ->
            prefs[Keys.IS_LOGGED_IN] ?: false
        }
}
