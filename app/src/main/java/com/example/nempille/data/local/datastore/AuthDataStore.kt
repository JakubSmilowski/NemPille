package com.example.nempille.data.local.datastore

//used to remember which user is logged in across app restarts

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

//extension to create DataStore<Preferences> for the app
private val Context.authDataStore by preferencesDataStore(name = "auth_prefs")

//wrapper class around DataStore, so rest of the app doesn't touch low-level API
class AuthDataStore @Inject constructor(
    @ApplicationContext private val context: Context
) {

    //Keys for values we store
    private object Keys {
        val USER_ID = intPreferencesKey("user_id")
        val USER_ROLE = stringPreferencesKey("user_role")
        val IS_LOGGED_IN = booleanPreferencesKey("is_logged_in")
    }

    // Save login session (user id + role + flag)
    suspend fun saveSession(userId: Int, role: UserRole) {
        context.authDataStore.edit { prefs ->
            prefs[Keys.USER_ID] = userId
            prefs[Keys.USER_ROLE] = role.name
            prefs[Keys.IS_LOGGED_IN] = true
        }
    }

    // Clear all auth data (logout)
    suspend fun clearSession() {
        context.authDataStore.edit { prefs->
            prefs.clear()
        }
    }

    // Flow of current user id (null if not logged in)
    fun userIdFlow(): Flow<Int?> =
        context.authDataStore.data.map { prefs ->
            prefs[Keys.USER_ID]
        }

    // Flow of current role
    fun userRoleFlow(): Flow<UserRole?> =
        context.authDataStore.data.map { prefs ->
            prefs[Keys.USER_ROLE]?.let { UserRole.valueOf(it) }
        }

    // Flow of "is user logged in?"
    fun isLoggedInFlow(): Flow<Boolean> =
        context.authDataStore.data.map { prefs ->
            prefs[Keys.IS_LOGGED_IN] ?: false
        }
}