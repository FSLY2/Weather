package com.example.weather.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.weather.databinding.FragmentSearchBinding
import com.example.weather.models.CityAndCountry
import com.example.weather.ui.adapters.SearchAdapter
import dagger.hilt.android.AndroidEntryPoint
import java.util.*
import kotlin.collections.ArrayList

@AndroidEntryPoint
class SearchFragment : Fragment(), SearchAdapter.OnItemClickListener {

    private lateinit var binding: FragmentSearchBinding
    private val viewModel: SearchViewModel by viewModels()

    private val adapterSearch = SearchAdapter(this)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initAdapter()
        initObservers()
        searchAndFilterCity()
    }

    private fun searchAndFilterCity() {
        viewModel.getCities()
        binding.searchField.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(p0: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                adapterSearch.filter(newText)
                return true
            }
        })
    }

    override fun onItemClick(cityAndCountry: CityAndCountry) {
        val cityName = cityAndCountry.cityName
        binding.searchField.setQuery(cityName, true)
        binding.searchField.clearFocus()
    }

    private fun initAdapter() {
        binding.rvPredictions.adapter = adapterSearch
    }
    private fun initObservers() {
        viewModel.observeOnSearchCitiesLiveData().observe(viewLifecycleOwner) {
            adapterSearch.updateList(it)
        }
    }
}