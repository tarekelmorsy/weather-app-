package com.example.weatherapp.prsentation.weather.view.ui.notifications

import android.app.Application
import androidx.lifecycle.*
import com.example.weatherapp.data.local.AlarmDatabase
import com.example.weatherapp.data.local.AlarmRepository
import kotlinx.coroutines.launch


class NotificationsViewModel(application: Application): AndroidViewModel(application) {
    private val repository: AlarmRepository
    val allAlarms: LiveData<List<ModelAlarm>>

    init {
        val movieDao =  AlarmDatabase.getInstance(application).movieDao()
        repository = AlarmRepository(movieDao)
        allAlarms = repository.allAlarms
    }

    fun insert(movie: ModelAlarm) = viewModelScope.launch {
        repository.insert(movie)
    }
    fun deleteAllAlarm() {
        viewModelScope.launch {
            repository.deleteAllWords()
        }
    }
    fun deleteAlarm(alarm: ModelAlarm) {
        viewModelScope.launch {
            repository.deleteAlarm(alarm)
        }
    }


}