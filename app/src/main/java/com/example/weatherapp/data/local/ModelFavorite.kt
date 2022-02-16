package com.example.weatherapp.data.local

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorite_table")
data class ModelFavorite(@ColumnInfo(name = "temp") var temp: Double, @ColumnInfo(name = "name") var name:String
                         , @ColumnInfo(name = "country") var country:String, @ColumnInfo(name = "humidity") var humidity: Int, @ColumnInfo(name = "wind") var wind: Double
                         , @ColumnInfo(name = "icon") val icon: String, @ColumnInfo(name = "main") val main:String)

{ @PrimaryKey(autoGenerate = true)
var wordId: Int = 0
}