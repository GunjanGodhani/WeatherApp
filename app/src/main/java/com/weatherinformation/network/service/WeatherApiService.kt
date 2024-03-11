package com.weatherinformation.network.service

import com.weatherinformation.ui.home.data.ForecastData
import com.weatherinformation.ui.home.data.WeatherData
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherApiService {

    @GET("data/2.5/weather")
    suspend fun getCurrentWeatherData(
        @Query("lat") lat: Double,
        @Query("lon") lon: Double,
        @Query("appid") appId: String
    ): Response<WeatherData>

    @GET("data/2.5/forecast")
    suspend fun getForeCastData(
        @Query("lat") lat: Double,
        @Query("lon") lon: Double,
        @Query("appid") appId: String
    ): Response<ForecastData>
}