package com.example.smartschedule.data.local.datastore.user_session

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.smartschedule.domain.models.user.roles.Roles
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import javax.inject.Inject

private val Context.datastore by preferencesDataStore("user_session")

class UserSessionManager @Inject constructor(
    @param:ApplicationContext private val context: Context
){

    companion object{
        private val KEY_USER_ID = stringPreferencesKey("user_id")
        private val KEY_USER_ROLE = stringPreferencesKey("user_role")
    }

    val userIdFlow : Flow<String?> = context.datastore.data.map { preferences ->
        preferences[KEY_USER_ID]
    }

    val userRoleFlow: Flow<Roles?> = context.datastore.data.map { prefs ->
        prefs[KEY_USER_ROLE]?.let { raw ->
            Roles.entries.firstOrNull { it.name == raw }
        }
    }


    suspend fun saveUserSession(userId: String, userRole: Roles) {
        context.datastore.edit { preferences ->
            preferences[KEY_USER_ID] = userId
            preferences[KEY_USER_ROLE] = userRole.name
        }
    }

    suspend fun clearSession() {
        context.datastore.edit { preferences ->
            preferences.clear()
        }
    }

    val isUserLoggedInFlow: Flow<Boolean> = context.datastore.data.map { prefs ->
        prefs[KEY_USER_ID] != null
    }

}