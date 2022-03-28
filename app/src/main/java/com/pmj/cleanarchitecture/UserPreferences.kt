package com.pmj.cleanarchitecture

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserPreferences @Inject constructor(private val context: Context) {

    companion object {
        val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "dining_pref_store")
        val KEY_NIGHT_MODE = booleanPreferencesKey("key_night_mode")
    }

    val isNightMode: Flow<Boolean?>
        get() = context.dataStore.data.map { preferences ->
            preferences[KEY_NIGHT_MODE]
        }

    suspend fun toggleNightMode(isDarkMode: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[KEY_NIGHT_MODE] = isDarkMode
        }
    }

}