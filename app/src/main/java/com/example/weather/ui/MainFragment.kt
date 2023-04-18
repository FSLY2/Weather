package com.example.weather.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
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
        initAdapter()
        initObservers()

        viewModel.weatherLiveData.observe(viewLifecycleOwner) { weatherDataModel ->
            val temp = StringBuilder().append(weatherDataModel.current.temp_c.toInt()).append("\u00B0")
            binding.tvTemp.text = temp.toString()
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