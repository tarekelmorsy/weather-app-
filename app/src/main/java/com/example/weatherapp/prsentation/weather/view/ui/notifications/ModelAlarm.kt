package com.example.weatherapp.prsentation.weather.view.ui.notifications

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "alarm_table")
  data class ModelAlarm(@ColumnInfo(name = "dateStart") var dateStart:String, @ColumnInfo(name = "dateEnd")
var dateEnd:String, @ColumnInfo(name = "timeStart") var timeStart:String,@ColumnInfo(name = "timeEnd")  var timeEnd:String)
{ @PrimaryKey(autoGenerate = true)
    var wordId: Int = 0
}