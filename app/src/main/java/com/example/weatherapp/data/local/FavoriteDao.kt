package com.example.weatherapp.data.local

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.weatherapp.prsentation.weather.view.ui.notifications.ModelAlarm
@Dao
interface FavoriteDao {
    @Insert
    fun insert(alarm: ModelFavorite)

    @Query("DELETE FROM favorite_table")
    fun deleteAll()

    @Query("SELECT * from favorite_table ORDER BY `temp` ASC ")
    fun getAllFavorites(): LiveData<List<ModelFavorite>>

    @Delete
    fun deleteFavorite(favorite: ModelFavorite)
}