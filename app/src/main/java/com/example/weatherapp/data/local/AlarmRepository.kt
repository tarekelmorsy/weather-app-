package com.example.weatherapp.data.local

import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import com.example.weatherapp.prsentation.weather.view.ui.notifications.ModelAlarm
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext


class AlarmRepository(private val alarmDao: AlarmDao) {
  val allAlarms: LiveData<List<ModelAlarm>> = alarmDao.getAllAlarms()


  @WorkerThread
  suspend fun insert(modelAlarm: ModelAlarm) = withContext(Dispatchers.IO) {
    alarmDao.insert(modelAlarm)
  }

  @WorkerThread
  suspend fun deleteAllWords() = withContext(Dispatchers.IO) {
    alarmDao.deleteAll()
  }
  suspend fun deleteAlarm(alarm: ModelAlarm)= withContext(Dispatchers.IO) {
    alarmDao.deleteAlarm(alarm)
  }



}