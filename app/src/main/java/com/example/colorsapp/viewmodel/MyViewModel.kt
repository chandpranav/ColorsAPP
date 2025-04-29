package com.example.colorsapp.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.colorsapp.MainActivity
import com.example.colorsapp.data.MyDataStoreRepository
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

const val Log_Tag = "MainActivity"

class MyViewModel : ViewModel() {
    private val prefs = MyDataStoreRepository.get()

    fun saveInput(value: Int, index: Int) {
        viewModelScope.launch {
            prefs.saveInput(value, index)
            Log.v(Log_Tag, "Saved seekbar input $index = $value")
        }
    }

    fun saveSwitchInput(value: Boolean, index: Int) {
        viewModelScope.launch {
            prefs.saveInputSwitch(value, index)
            Log.v(Log_Tag, "Saved switch input $index = $value")
        }
    }

    fun loadInputs(activity: MainActivity) {
        viewModelScope.launch {
            prefs.redvalue.collectLatest {
                activity.redSeek.progress = it
                Log.v(Log_Tag, "Loaded red seek = $it")
            }
        }

        viewModelScope.launch {
            prefs.greenvalue.collectLatest {
                activity.greenSeek.progress = it
                Log.v(Log_Tag, "Loaded green seek = $it")
            }
        }

        viewModelScope.launch {
            prefs.bluevalue.collectLatest {
                activity.blueSeek.progress = it
                Log.v(Log_Tag, "Loaded blue seek = $it")
            }
        }

        viewModelScope.launch {
            prefs.redstatus.collectLatest {
                activity.switchRed.isChecked = it
                Log.v(Log_Tag, "Loaded red switch = $it")
            }
        }

        viewModelScope.launch {
            prefs.greenstatus.collectLatest {
                activity.switchGreen.isChecked = it
                Log.v(Log_Tag, "Loaded green switch = $it")
            }
        }

        viewModelScope.launch {
            prefs.bluestatus.collectLatest {
                activity.switchBlue.isChecked = it
                Log.v(Log_Tag, "Loaded blue switch = $it")
            }
        }
    }
}