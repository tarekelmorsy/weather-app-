package com.example.weatherapp.prsentation.weather.view.ui.favorite

import CurrentWeather
import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.*
import com.example.weatherapp.data.local.*
import com.example.weatherapp.prsentation.weather.view.ui.notifications.ModelAlarm
import com.mcit.kmvvm.repo.WeatherRepo
import kotlinx.coroutines.launch

class FavoriteViewModel (application: Application): AndroidViewModel(application) {
       var currentWeatherLiveData = MutableLiveData<CurrentWeather>()
     //  var  currentWeatherLiveData = _currentWeatherLiveData

    private val repo = WeatherRepo()

    private val repository: FavoriteRepository
    val allFavorite: LiveData<List<ModelFavorite>>

    init {
        val favoriteDao =  FavoriteDatabase.getInstance(application).favoriteDao()
        repository = FavoriteRepository(favoriteDao)
        allFavorite = repository.allFavorite
    }

    fun insert(favorite: ModelFavorite) = viewModelScope.launch {
        repository.insertFavorite(favorite)
    }
    fun deleteAllFavorite() {
        viewModelScope.launch {
            repository.deleteAllFavorite()
        }
    }
    fun deleteFavorite(favorite: ModelFavorite) {
        viewModelScope.launch {
            repository.deleteFavorite(favorite)
        }
    }


    fun getCurrentWeather(lat:Double,lon:Double,units:String,lang:String) {



            repo.getCurrentWeather(lat, lon, units, lang)
            currentWeatherLiveData = repo.currentWeatherLiveData


    }


}