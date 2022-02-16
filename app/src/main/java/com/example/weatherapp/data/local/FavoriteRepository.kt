package com.example.weatherapp.data.local

import android.content.Context
import android.widget.Toast
import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class FavoriteRepository(private val favoriteDao: FavoriteDao ) {
    val allFavorite: LiveData<List<ModelFavorite>> = favoriteDao.getAllFavorites()
    @WorkerThread
    suspend fun insertFavorite(modelFavorite: ModelFavorite) = withContext(Dispatchers.IO) {
        favoriteDao.insert(modelFavorite)
    }

    @WorkerThread
    suspend fun deleteAllFavorite() = withContext(Dispatchers.IO) {
        favoriteDao.deleteAll()
    }
    suspend fun deleteFavorite(modelFavorite: ModelFavorite)= withContext(Dispatchers.IO) {
        favoriteDao.deleteFavorite(modelFavorite)

    }

}