package com.mcit.kmvvm.repo

import CurrentWeather
import Daily
import Alart
import AllWeather
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.util.Log
import androidx.lifecycle.MutableLiveData
 import com.mcit.kmvvm.data.remote.API_KEY
import com.mcit.kmvvm.data.remote.RetrofitClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class WeatherRepo {
    private val TAG = "WeatherRepo"
    val weatherLiveData = MutableLiveData<AllWeather>()
    val weatherDaysLiveData = MutableLiveData<List<Daily>>()
    val currentWeatherLiveData = MutableLiveData<CurrentWeather>()
    val windWeatherLiveData = MutableLiveData<AllWeather>()



    fun getWeather(lat:Double,lon:Double,units:String,lang: String) {

        GlobalScope.launch(Dispatchers.IO) {
            val response = RetrofitClient.getApiService().getWeatherDetails(lat, lon,units,lang,API_KEY)

            withContext(Dispatchers.Main){
            if (response.isSuccessful) {
                response.body()?.let {
                    Log.d("WeatherReporrr", "onResponserrrr: ${it}")
                    weatherLiveData.value = it
                }
            }
            }

        }
    }
    fun getWind(lat:Double,lon:Double,units:String,lang:String) {

        GlobalScope.launch(Dispatchers.IO) {
            val response = RetrofitClient.getApiService().getWind(lat, lon,units,lang,API_KEY)

            withContext(Dispatchers.Main){
                if (response.isSuccessful) {
                    response.body()?.let {
                        Log.d("WeatherReporrr", "onResponserrrr: ${it}")
                        windWeatherLiveData.value = it
                    }
                }
            }

        }
    }
    fun getDaysWeather(lat:Double,lon:Double,exclude:String,lang:String) {

        GlobalScope.launch(Dispatchers.IO) {
            val response = RetrofitClient.getApiService().getWeatherDays(lat, lon,exclude,lang,API_KEY)

            withContext(Dispatchers.Main){
                if (response.isSuccessful) {
                    response.body()?.let {
                        Log.d(TAG, "onResponse: ${it.daily}")
                        weatherDaysLiveData.value = it.daily
                    }
                }
            }

        }
    }

    fun getCurrentWeather(lat:Double,lon:Double,units:String,lang:String) {


        GlobalScope.launch(Dispatchers.IO) {
            val response = RetrofitClient.getApiService().getCurrentWeather(lat, lon,units,lang,API_KEY)

            withContext(Dispatchers.Main){
                if (response.isSuccessful) {
                    response.body()?.let {
                        Log.d(TAG, "onResponse: ${it}")
                        currentWeatherLiveData.value = it

                }
            }

        }}
    }

    private fun checkForInternet(context: Context): Boolean {

        // register activity with the connectivity manager service
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        // if the android version is equal to M
        // or greater we need to use the
        // NetworkCapabilities to check what type of
        // network has the internet connection
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            // Returns a Network object corresponding to
            // the currently active default data network.
            val network = connectivityManager.activeNetwork ?: return false

            // Representation of the capabilities of an active network.
            val activeNetwork = connectivityManager.getNetworkCapabilities(network) ?: return false

            return when {
                // Indicates this network uses a Wi-Fi transport,
                // or WiFi has network connectivity
                activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true

                // Indicates this network uses a Cellular transport. or
                // Cellular has network connectivity
                activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true

                // else return false
                else -> false
            }
        } else {
            // if the android version is below M
            @Suppress("DEPRECATION") val networkInfo =
                connectivityManager.activeNetworkInfo ?: return false
            @Suppress("DEPRECATION")
            return networkInfo.isConnected
        }
    }


}