package com.pmj.cleanarchitecture.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserPreferences @Inject constructor(private val context: Context) {

    companion object {
        val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "dining_pref_store")
        val KEY_NIGHT_MODE = intPreferencesKey("key_night_mode")
    }

    val selectedTheme: Flow<Int>
        get() = context.dataStore.data.map { preferences ->
            preferences[KEY_NIGHT_MODE] ?: 0
        }

    suspend fun saveTheme(selectedTheme: Int) {
        context.dataStore.edit { preferences ->
            preferences[KEY_NIGHT_MODE] = selectedTheme
        }
    }

}