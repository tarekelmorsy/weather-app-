package com.mcit.kmvvm.data.remote

import CurrentWeather
import Alart
import AllWeather
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {

    @GET("onecall")
    suspend fun getWeatherDetails(
        @Query("lat") lat: Double,
        @Query("lon") lon: Double,
        @Query("units") units: String,
        @Query("lang") lang: String,
        @Query("appid") appid: String

    ): Response<AllWeather>

    @GET("onecall")
    suspend fun getWeatherDays(
        @Query("lat") lat: Double,
        @Query("lon") lon: Double,
        @Query("exclude") exclude: String,
        @Query("lang") lang: String,
        @Query("appid") appid: String
    ): Response<AllWeather>

    @GET("onecall")
    suspend fun getWind(
        @Query("lat") lat: Double,
        @Query("lon") lon: Double,
        @Query("units") units: String,
        @Query("lang") lang: String,
        @Query("appid") appid: String
    ): Response<AllWeather>

    @GET("weather")
    suspend fun getCurrentWeather(
        @Query("lat") lat: Double,
        @Query("lon") lon: Double,
        @Query("units") units: String,
        @Query("lang") lang: String,
        @Query("appid") appid: String

        ): Response<CurrentWeather>

}