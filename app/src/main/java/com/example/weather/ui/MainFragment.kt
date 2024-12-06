package com.example.weather.ui

import android.Manifest
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.location.LocationListenerCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.weather.common.*
import com.example.weather.databinding.FragmentMainBinding
import com.example.weather.ui.adapters.DailyForecastAdapter
import com.example.weather.ui.adapters.MainAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainFragment : Fragment() {

    private lateinit var binding: FragmentMainBinding
    private val viewModel: MainViewModel by viewModels()

    private val adapter = MainAdapter()
    private val adapterDaily = DailyForecastAdapter()

    private lateinit var locationManager: LocationManager

    //Permission launcher
    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) {
                requestLocationUpdates()
            } else {
                if (shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION)) {
                    permissionsDialog()
                } else {
                    permissionsDialogDeniedForever()
                }
            }
    }

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
        requestLocationUpdates()
        displayData()
        initAdapter()
        initObservers()
        openSearchFragment()
    }

    private fun displayData() {
        viewModel.weatherLiveData.observe(viewLifecycleOwner) { weatherDataModel ->
            weatherDataModel.apply {
                with(binding) {
                    //Current
                    tvDayOfWeek.text = location.localtime_epoch.toDateFormat(DAY_OF_WEEK)
                    tvTemp.putTextTemp(weatherDataModel.current.temp_c)
                    tvCityName.text = location.name
                    tvCurrentCondition.text = current.condition.text
                    tvHumidityNow.putTextHumidity(current.humidity)
                    tvWindNow.putTextWind(current.wind_kph)
                    tvPressureNow.putTextPressure(current.pressure_mb)
                }
            }
        }
    }

    private fun searchForecastForCity() {
        val city = "Kiev"
        viewModel.getWeatherForecast(city)
    }

    private fun openSearchFragment() {
        binding.bSearch.setOnClickListener {
            val action = MainFragmentDirections.actionMainFragmentToSearchFragment()
            findNavController().navigate(action)
        }
    }

    private fun initAdapter() {
        binding.rvHourly.adapter = adapter
        binding.rvDaily.adapter = adapterDaily
    }

    private fun initObservers() {
        viewModel.weatherLiveData.observe(viewLifecycleOwner) {
            adapter.updateList(it)
            adapterDaily.updateDailyList(it)
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
    }

    private fun permissionsDialog() {
        val explanation = "Location permission is required for the app to work."
        AlertDialog.Builder(requireContext())
            .setTitle("Need permission")
            .setMessage(explanation)
            .setPositiveButton(android.R.string.ok) { _, _ ->
                requestPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
            }
            .setNegativeButton(android.R.string.cancel, null)
            .create()
            .show()
    }

    private fun permissionsDialogDeniedForever() {
        val appSettingsIntent = Intent(
            Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
            Uri.fromParts("package", requireActivity().packageName, null)
        )
        val explanation = "You have denied permissions forever. " +
                "You can change your decision in app settings. \n\n" +
                "Would you like to open app settings?"
        AlertDialog.Builder(requireContext())
            .setTitle("Permissions denied")
            .setMessage(explanation)
            .setPositiveButton(android.R.string.ok) { _, _ ->
                startActivity(appSettingsIntent)
            }
            .create()
            .show()
    }

    private fun requestLocationUpdates() {
        //Check permissions
        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED) {
            locationManager.requestLocationUpdates(
                LocationManager.GPS_PROVIDER,
                150000,
                33000f,
                object : LocationListenerCompat {
                    override fun onLocationChanged(location: Location) {
                        val lat = location.latitude.toString()
                        val lon = location.longitude.toString()
                        val coord = "$lat,$lon"
                        Log.i("MainFragment", "requestLocationUpdates: $coord")
                        viewModel.getWeatherForecast(coord)
                    }

                    override fun onProviderEnabled(provider: String) {
                        super.onProviderEnabled(provider)
                        Log.i("MainFragment", "requestLocationUpdates: ENABLED")
                    }

                    override fun onProviderDisabled(provider: String) {
                        super.onProviderDisabled(provider)
                        Log.i("MainFragment", "requestLocationUpdates: DISABLED")
                    }
                }
            )
        } else {
            requestPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        }
    }
}