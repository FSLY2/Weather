package com.example.weather.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.weather.databinding.FragmentMainBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainFragment : Fragment() {

    private lateinit var binding: FragmentMainBinding
    private val viewModel: MainViewModel by viewModels()

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

        searchCity()

        viewModel.getWeatherLiveData().observe(viewLifecycleOwner) { weatherDataModel ->
            val temp = weatherDataModel.main.temp

            binding.tvTemp.text = temp.toString()
        }
    }

    private fun searchCity() {
        binding.bSearch.setOnClickListener {
            val cityName = binding.etCityName.text.toString()
            viewModel.getWeatherForecast(cityName)
        }
    }
}