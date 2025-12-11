package com.example.smartschedule.core.data.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.smartschedule.core.domain.model.employees.EmployeeId
import com.example.smartschedule.core.domain.model.employees.enums.EmployeeRole
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

val Context.datastore : DataStore<Preferences> by preferencesDataStore("user_prefs")

@Singleton
class UserPreferences @Inject constructor(
    @ApplicationContext private val context: Context
) {
    companion object{
         val USER_ROLE_KEY = stringPreferencesKey("user_role")
        val USER_ID_KEY = stringPreferencesKey("user_id")
    }

    val userRole : Flow<EmployeeRole?>  = context.datastore.data
        .map { preferences ->
            val roleString = preferences[USER_ROLE_KEY]
            if (roleString != null) EmployeeRole.valueOf(roleString) else null
        }

    val userId : Flow<EmployeeId?> = context.datastore.data
        .map { preferences ->
            val userIdString = preferences[USER_ID_KEY]
            if (userIdString != null) EmployeeId(userIdString) else null
        }

    suspend fun saveUserRole(role: EmployeeRole) {
        context.datastore.edit { preferences ->
            preferences[USER_ROLE_KEY] = role.name
        }
    }
    suspend fun saveUserId(userId: EmployeeId) {
        context.datastore.edit { preferences ->
            preferences[USER_ID_KEY] = userId.value
        }
    }

    suspend fun saveUser(role: EmployeeRole, userId: EmployeeId) {
        context.datastore.edit { preferences ->
            preferences[USER_ROLE_KEY] = role.name
            preferences[USER_ID_KEY] = userId.value
        }
    }
    suspend fun clear(){
        context.datastore.edit { preferences ->
            preferences.clear()
        }
    }
}