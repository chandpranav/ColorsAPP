package com.example.colorsapp.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStoreFile
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

class MyDataStoreRepository private constructor(private val dataStore: DataStore<Preferences>) {

    val redvalue: Flow<Int> = dataStore.data.map { it[RED_SEEK_VALUE] ?: 128 }.distinctUntilChanged()
    val greenvalue: Flow<Int> = dataStore.data.map { it[GREEN_SEEK_VALUE] ?: 128 }.distinctUntilChanged()
    val bluevalue: Flow<Int> = dataStore.data.map { it[BLUE_SEEK_VALUE] ?: 128 }.distinctUntilChanged()

    val redstatus: Flow<Boolean> = dataStore.data.map { it[RED_SWITCH_STATUS] ?: true }.distinctUntilChanged()
    val greenstatus: Flow<Boolean> = dataStore.data.map { it[GREEN_SWITCH_STATUS] ?: true }.distinctUntilChanged()
    val bluestatus: Flow<Boolean> = dataStore.data.map { it[BLUE_SWITCH_STATUS] ?: true }.distinctUntilChanged()

    suspend fun saveInput(value: Int, index: Int) {
        dataStore.edit { prefs ->
            when (index) {
                1 -> prefs[RED_SEEK_VALUE] = value
                2 -> prefs[GREEN_SEEK_VALUE] = value
                3 -> prefs[BLUE_SEEK_VALUE] = value
            }
        }
    }

    suspend fun saveInputSwitch(value: Boolean, index: Int) {
        dataStore.edit { prefs ->
            when (index) {
                4 -> prefs[RED_SWITCH_STATUS] = value
                5 -> prefs[GREEN_SWITCH_STATUS] = value
                6 -> prefs[BLUE_SWITCH_STATUS] = value
            }
        }
    }

    companion object {
        private const val DATA_STORE_FILE = "color_settings"
        private val RED_SEEK_VALUE = intPreferencesKey("redvalue")
        private val GREEN_SEEK_VALUE = intPreferencesKey("greenvalue")
        private val BLUE_SEEK_VALUE = intPreferencesKey("bluevalue")
        private val RED_SWITCH_STATUS = booleanPreferencesKey("redstatus")
        private val GREEN_SWITCH_STATUS = booleanPreferencesKey("greenstatus")
        private val BLUE_SWITCH_STATUS = booleanPreferencesKey("bluestatus")

        private var INSTANCE: MyDataStoreRepository? = null

        fun get(): MyDataStoreRepository {
            return INSTANCE ?: throw IllegalStateException("Repository not initialized")
        }

        fun initialize(context: Context) {
            if (INSTANCE == null) {
                val dataStore = PreferenceDataStoreFactory.create {
                    context.preferencesDataStoreFile(DATA_STORE_FILE)
                }
                INSTANCE = MyDataStoreRepository(dataStore)
            }
        }
    }
}