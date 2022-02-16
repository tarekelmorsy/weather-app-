package com.example.weatherapp.data.local

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.weatherapp.prsentation.weather.view.ui.notifications.ModelAlarm
import kotlinx.coroutines.flow.Flow

@Dao
interface AlarmDao {
  @Insert
    fun insert(alarm: ModelAlarm)

  @Query("DELETE FROM alarm_table")
    fun deleteAll()

  @Query("SELECT * from alarm_table ORDER BY dateStart ASC ")
  fun getAllAlarms(): LiveData<List<ModelAlarm>>

  @Delete
  fun deleteAlarm(alarm: ModelAlarm)
}




