package com.weatherinformation.ui.home.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherinformation.databinding.RowItemForecastBinding
import com.weatherinformation.ui.home.data.List
import com.weatherinformation.utils.roundToTwoDecimalPlaces

class ForecastAdapter : RecyclerView.Adapter<ForecastAdapter.ViewHolder>() {
    var forecastList = ArrayList<List>()

    inner class ViewHolder(private val binding: RowItemForecastBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(data: List) = with(binding) {
            val temp = data.main?.temp
            val finalTemp = temp?.minus(273.15)
            textViewTemperature.text = "${roundToTwoDecimalPlaces(finalTemp!!)}°C"
            textViewHumidityValue.text = "${data.main?.humidity.toString()}%"
            textViewWindValue.text = "${data.wind?.speed}m/s"
            textViewPressureValue.text = "${data.main?.pressure.toString()}hPa"
            val visibility = (data.visibility!!) * 0.001
            textViewVisibilityValue.text = "${visibility}km"
            for (i in 0..<data.weather.size) {
                textViewDescriptionValue.text = data.weather[i].description
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            RowItemForecastBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return forecastList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(forecastList[position])
    }


}