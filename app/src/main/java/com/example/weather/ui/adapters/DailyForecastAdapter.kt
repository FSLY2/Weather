package com.example.weather.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.weather.common.*
import com.example.weather.databinding.ItemDailyBinding
import com.example.weather.models.ForecastDay
import com.example.weather.models.WeatherDataModel
import io.reactivex.rxjava3.core.Observable

class DailyForecastAdapter : RecyclerView.Adapter<DailyForecastAdapter.ViewHolder>() {

    private var inputDailyData: List<ForecastDay> = emptyList()

    class ViewHolder(val binding: ItemDailyBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemDailyBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        with(holder.binding) {
            //Day
            tvDayDate.text = inputDailyData[position].date_epoch.toDateFormat(DAY_FULL_MONTH_NAME)
            ivIconDay.loadImage(inputDailyData[position].day.condition.icon)
            tvTempDay.putTextTemp(inputDailyData[position].day.maxtemp_c)
            tvHumDay.putTextAvgHumidity(inputDailyData[position].day.avghumidity)
            tvWindDay.putTextWind(inputDailyData[position].day.maxwind_kph)

            //Night
            ivIconNight.loadImage(inputDailyData[position].hour[0].condition.icon)
            tvTempNight.putTextTemp(inputDailyData[position].day.mintemp_c)
            tvHumNight.putTextHumidity(inputDailyData[position].hour[0].humidity)
            tvWindNight.putTextWind(inputDailyData[position].hour[0].wind_kph)
        }
    }

    override fun getItemCount(): Int = inputDailyData.size

    fun updateDailyList(newList: WeatherDataModel) {
        inputDailyData = newList.forecast.forecastday
        notifyDataSetChanged()
    }
}