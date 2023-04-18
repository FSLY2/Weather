package com.example.weather.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.weather.R
import com.example.weather.common.HOUR_DOT_MINUTE
import com.example.weather.common.toDateFormat
import com.example.weather.databinding.ItemHourlyBinding
import com.example.weather.models.Hour
import com.example.weather.models.WeatherDataModel

class MainAdapter : RecyclerView.Adapter<MainAdapter.ViewHolder>() {

    private var inputData: List<Hour> = emptyList()

    class ViewHolder(val binding: ItemHourlyBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemHourlyBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }



    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        with(holder.binding){
            itemTvHourlyTime.text = inputData[position].time_epoch.toDateFormat(HOUR_DOT_MINUTE)

            itemTvHourlyTemp.text = StringBuilder().append(inputData[position].temp_c.toInt())
                .append("\u00B0").toString()

            val options = RequestOptions()
                .error(R.drawable.ic_error)

            Glide.with(itemIconHourlyWeatherCondition.context)
                .setDefaultRequestOptions(options)
                .load("https:" + inputData[position].condition.icon)
                .into(itemIconHourlyWeatherCondition)
        }
    }

    override fun getItemCount(): Int = inputData.size

    fun updateList(newList: WeatherDataModel) {
        inputData = newList.forecast.forecastday[0].hour
        notifyDataSetChanged()
    }
}