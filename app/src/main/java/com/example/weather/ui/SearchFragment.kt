package com.example.weather.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.weather.databinding.FragmentSearchBinding
import com.example.weather.ui.adapters.SearchAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SearchFragment : Fragment() {

    private lateinit var binding: FragmentSearchBinding
    private val viewModel: SearchViewModel by viewModels()

    private val adapterSearch = SearchAdapter()

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

        // Reading the request from the search field
        binding.searchField.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false // Ignore the send event (Enter)
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                viewModel.getPrediction(newText.orEmpty())
                return true
            }
        })
    }

    private fun initAdapter() {
        binding.rvPredictions.adapter = adapterSearch
    }

    private fun initObservers() {
        viewModel.searchCitiesLiveData.observe(viewLifecycleOwner) {
            adapterSearch.updateList(it)
        }
    }
}