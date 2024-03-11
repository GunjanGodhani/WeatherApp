package com.weatherinformation.ui.home.fragment

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.RadioGroup
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherinformation.R
import com.example.weatherinformation.databinding.WeatherInfoFragmentBinding
import com.weatherinformation.base.BaseFragment
import com.weatherinformation.ui.home.activity.HomeActivity
import com.weatherinformation.ui.home.adapter.ForecastAdapter
import com.weatherinformation.ui.home.viewmodel.WeatherDataViewModel
import com.weatherinformation.utils.AppConstants
import com.weatherinformation.utils.roundToTwoDecimalPlaces
import java.time.Instant
import java.time.ZoneId


class WeatherInfoFragment : BaseFragment() {

    private lateinit var binding: WeatherInfoFragmentBinding
    private var latitude: Double = 0.0
    private var longitude: Double = 0.0
    private val weatherViewModel by lazy {
        ViewModelProvider(this)[WeatherDataViewModel::class.java]
    }
    private val forecastAdapter by lazy {
        ForecastAdapter()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = WeatherInfoFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpToolBar()
        getBundleDataAndInitVisibility()
        checkRadioButtonSelected()
        observeCurrentWeatherLiveData()
        callCurrentWeather()
        setupAdapter()
    }

    private fun setUpToolBar(){
        (activity as HomeActivity).apply {
            goBack()
            setTitle(getString(R.string.title_weather_information))
        }
    }

    private fun getBundleDataAndInitVisibility() {
        binding.constCurrentWeather.visibility = View.VISIBLE
        binding.const5DayForecast.visibility = View.GONE
        binding.progressBar.visibility = View.VISIBLE
        arguments?.let {
            latitude = it.getDouble(AppConstants.LAT)
            longitude = it.getDouble(AppConstants.LONG)
        }
    }

    private fun setupAdapter() = with(binding) {
        recyclerViewForeCast.apply {
            layoutManager = LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
            adapter = forecastAdapter
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun checkRadioButtonSelected() = with(binding) {
        radioGroup.setOnCheckedChangeListener(RadioGroup.OnCheckedChangeListener { group, checkedId ->
            val radioButton = group.findViewById<View>(checkedId) as RadioButton
            val selectedText: String = radioButton.text.toString()
            when (selectedText) {
                getString(R.string.label_current_weather) -> {
                    progressBar.visibility = View.VISIBLE
                    const5DayForecast.visibility = View.GONE
                    constCurrentWeather.visibility = View.VISIBLE
                    callCurrentWeather()
                    observeCurrentWeatherLiveData()
                }

                getString(R.string.label__5_days_forecast) -> {
                    progressBar.visibility = View.VISIBLE
                    constCurrentWeather.visibility = View.GONE
                    const5DayForecast.visibility = View.VISIBLE
                    callForeCastData()
                    observeForeCastLiveData()
                }
            }
        })
    }

    /************************************
     **
     ***  API Call
     **
     ************************************ */

    @RequiresApi(Build.VERSION_CODES.O)
    private fun observeCurrentWeatherLiveData() {
        weatherViewModel.weatherLiveData.observe(requireActivity()) {
            binding.progressBar.visibility = View.GONE
            val temp = it.main?.temp
            val finalTemp = temp?.minus(273.15)

            it.name?.let { it1 -> Log.e("name", it1) }
            binding.apply {
                textViewCityName.text = it.name
                textViewTemperature.text = "${roundToTwoDecimalPlaces(finalTemp!!)}°C"
                textViewRain.text = it.rain?.rain.toString()
                textViewHumidityValue.text = "${it.main?.humidity.toString()}%"
                textViewWindValue.text = "${it.wind?.speed}m/s"
                textViewPressureValue.text = "${it.main?.pressure.toString()}hPa"
                val visibility = (it.visibility!!) * 0.001
                textViewVisibilityValue.text = "${visibility}km"
                textViewSunRaiseValue.text = convertDateTIme(it.sys!!.sunrise!!.toLong())
                textViewSunSetValue.text = convertDateTIme(it.sys!!.sunset!!.toLong())
                textViewUvIndexValue.text = it.timezone.toString()
                for (i in it.weather) {
                    textViewStatusOfCloud.text = i.main.toString()
                }
            }
        }
    }

    private fun callCurrentWeather() {
        weatherViewModel.getCurrentWeather(
            roundToTwoDecimalPlaces(latitude),
            roundToTwoDecimalPlaces(longitude),
            AppConstants.APP_ID
        )
    }

    /************************************
     **
     ***  API Call
     **
     ************************************ */

    private fun observeForeCastLiveData() {
        weatherViewModel.forecastLiveData.observe(viewLifecycleOwner) {
            forecastAdapter.forecastList.addAll(it.list)
            binding.progressBar.visibility = View.GONE
            forecastAdapter.notifyDataSetChanged()
        }
    }

    private fun callForeCastData() {
        weatherViewModel.getForeCastData(
            roundToTwoDecimalPlaces(latitude),
            roundToTwoDecimalPlaces(longitude),
            AppConstants.APP_ID
        )
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun convertDateTIme(ts: Long): String {
        val localTime = ts.let {
            Instant.ofEpochSecond(it).atZone(ZoneId.systemDefault()).toLocalTime()
        }
        return localTime.toString()
    }


}