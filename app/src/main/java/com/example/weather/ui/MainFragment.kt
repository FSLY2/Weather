package com.example.weather.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import com.example.weather.common.DAY_FULL_MONTH_NAME
import com.example.weather.common.DAY_OF_WEEK
import com.example.weather.common.toDateFormat
import com.example.weather.databinding.FragmentMainBinding
import com.example.weather.ui.adapters.MainAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainFragment : Fragment() {

    private lateinit var binding: FragmentMainBinding
    private val viewModel: MainViewModel by viewModels()

    private val adapter = MainAdapter()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        searchForecastForCity()
        displayData()
        initAdapter()
        initObservers()
    }

    private fun displayData() {
        viewModel.weatherLiveData.observe(viewLifecycleOwner) { weatherDataModel ->
            weatherDataModel.apply {
                with(binding) {
                    //Current
                    tvDayOfWeek.text = location.localtime_epoch.toDateFormat(DAY_OF_WEEK)
                    tvTemp.text = StringBuilder()
                        .append(weatherDataModel.current.temp_c.toInt()).append("\u00B0").toString()
                    tvCityName.text = location.name
                    tvCurrentCondition.text = current.condition.text

                    tvHumidityNow.text = StringBuilder().append(current.humidity).append("%")
                    tvWindNow.text = StringBuilder().append(current.wind_kph.toInt()).append(" kph")
                    tvPressureNow.text = StringBuilder().append(current.pressure_mb.toInt())
                        .append(" mmHg")

                    //Tomorrow Day
                    Glide.with(ivTomorrowIconDay.context)
                        .load("https:" + forecast.forecastday[1].day.condition.icon)
                        .into(ivTomorrowIconDay)

                    tvTomorrowTempDay.text = StringBuilder()
                        .append(forecast.forecastday[1].day.maxtemp_c.toInt()).append("\u00B0")
                    tvTomorrowHumDay.text = StringBuilder()
                        .append(forecast.forecastday[1].day.avghumidity.toInt()).append("%")
                    tvTomorrowWindDay.text = StringBuilder()
                        .append(forecast.forecastday[1].day.maxwind_kph.toInt()).append(" kph")

                    //Night
                    Glide.with(ivTomorrowIconNight.context)
                        .load("https:" + forecast.forecastday[1].hour[0].condition.icon)
                        .into(ivTomorrowIconNight)

                    tvTomorrowTempNight.text = StringBuilder()
                        .append(forecast.forecastday[1].day.mintemp_c.toInt()).append("\u00B0")
                    tvTomorrowHumNight.text = StringBuilder()
                        .append(forecast.forecastday[1].hour[0].humidity).append("%")
                    tvTomorrowWindNight.text = StringBuilder()
                        .append(forecast.forecastday[1].hour[0].wind_kph.toInt()).append(" kph")

                    //TwoDay
                    tvTwoDayDate.text = forecast.forecastday[2].date_epoch
                        .toDateFormat(DAY_FULL_MONTH_NAME)

                    Glide.with(ivTwoIconDay.context)
                        .load("https:" + forecast.forecastday[2].day.condition.icon)
                        .into(ivTwoIconDay)

                    tvTwoTempDay.text = StringBuilder()
                        .append(forecast.forecastday[2].day.maxtemp_c.toInt()).append("\u00B0")
                    tvTwoHumDay.text = StringBuilder()
                        .append(forecast.forecastday[2].day.avghumidity.toInt()).append("%")
                    tvTwoWindDay.text = StringBuilder()
                        .append(forecast.forecastday[2].day.maxwind_kph.toInt()).append(" kph")

                    //Night
                    Glide.with(ivTwoIconNight.context)
                        .load("https:" + forecast.forecastday[2].hour[0].condition.icon)
                        .into(ivTwoIconNight)
                    tvTwoTempNight.text = StringBuilder()
                        .append(forecast.forecastday[2].day.mintemp_c.toInt()).append("\u00B0")
                    tvTwoHumNight.text = StringBuilder()
                        .append(forecast.forecastday[2].hour[0].humidity.toString()).append("%")
                    tvTwoWindNight.text = StringBuilder()
                        .append(forecast.forecastday[2].hour[0].wind_kph.toInt()).append(" kph")
                }
            }
        }
    }

    private fun searchForecastForCity() {
        val city = "Kiev"
        viewModel.getWeatherForecast(city)
    }

    private fun initAdapter() {
        binding.rvHourly.adapter = adapter
    }

    private fun initObservers() {
        viewModel.weatherLiveData.observe(viewLifecycleOwner) {
            adapter.updateList(it)
        }
    }
}