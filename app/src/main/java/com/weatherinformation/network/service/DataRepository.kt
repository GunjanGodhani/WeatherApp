package com.weatherinformation.network.service


import com.weatherinformation.ui.home.data.ForecastData
import com.weatherinformation.ui.home.data.WeatherData
import retrofit2.Response
import retrofit2.http.Query
import javax.inject.Inject

class DataRepository @Inject constructor(private val apiService: WeatherApiService) {
    suspend fun getCurrentWeatherData(
        lat: Double,
        lon: Double,
        appId: String
    ): Response<WeatherData> {
        return apiService.getCurrentWeatherData(lat, lon, appId)
    }

    suspend fun getForeCastData(
        lat: Double,
        lon: Double,
        appId: String
    ): Response<ForecastData> {
        return apiService.getForeCastData(lat, lon, appId)
    }

}