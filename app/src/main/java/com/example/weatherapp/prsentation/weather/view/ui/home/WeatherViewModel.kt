package com.example.weatherapp.prsentation.weather.view.ui.home

import CurrentWeather
import Daily
import Alart
import AllWeather
import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
 import com.mcit.kmvvm.repo.WeatherRepo

class WeatherViewModel() : ViewModel() {
    var weatherLiveData = MutableLiveData<AllWeather>()
    var weatherDaysLiveData = MutableLiveData<List<Daily>>()
    var currentWeatherLiveData = MutableLiveData<CurrentWeather>()
    var windWeatherLiveData = MutableLiveData<AllWeather>()

    private val repo = WeatherRepo()
    fun getWeather(lat:Double,lon:Double,units:String,lang: String) {
        repo.getWeather(lat,lon,units,lang)
        weatherLiveData = repo.weatherLiveData

    }
    fun getDaysWeather(lat:Double,lon:Double,exclude:String,lang: String) {
        repo.getDaysWeather(lat,lon,exclude,lang)
        weatherDaysLiveData = repo.weatherDaysLiveData

    }
    fun getCurrentWeather(lat:Double,lon:Double,units:String,lang:String,context: Context) {
        repo.getCurrentWeather(lat,lon,units,lang)
        currentWeatherLiveData = repo.currentWeatherLiveData

    }
    fun getWindWeather(lat:Double,lon:Double,units:String,lang: String) {
        repo.getWind(lat,lon,units,lang)
        windWeatherLiveData = repo.windWeatherLiveData

    }

}