package com.weatherinformation.ui.home.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.weatherinformation.network.service.DataRepository
import com.weatherinformation.ui.home.data.ForecastData
import com.weatherinformation.ui.home.data.WeatherData
import com.weatherinformation.utils.AppConstants
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WeatherDataViewModel @Inject constructor(private val dataRepository: DataRepository) :
    ViewModel() {

    val weatherLiveData = MutableLiveData<WeatherData>()
    val forecastLiveData = MutableLiveData<ForecastData>()

    fun getCurrentWeather(latitude: Double, longitude: Double, appId: String) {
        viewModelScope.launch {
            val response =
                dataRepository.getCurrentWeatherData(
                    latitude,
                    longitude,
                    appId
                )
            if (response.isSuccessful) {
                if (response.body()?.cod == AppConstants.ResponseCode.SUCCESS) {
                    val result = response.body()!!
                    weatherLiveData.value = result
                }
            }
        }
    }

    fun getForeCastData(latitude: Double, longitude: Double, appId: String) {
        viewModelScope.launch {
            val response =
                dataRepository.getForeCastData(
                    latitude, longitude, appId
                )
            if (response.isSuccessful) {
                if (response.body()?.cod!!.toInt() == AppConstants.ResponseCode.SUCCESS) {
                    val result = response.body()!!
                    forecastLiveData.value = result
                } else {
                    Log.e("observe", response.body()!!.list.toString())

                }
            }

        }
    }
//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
}