package com.example.weather.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.weather.databinding.ItemSearchBinding
import com.example.weather.models.CityItem

class SearchAdapter : RecyclerView.Adapter<SearchAdapter.ViewHolder>() {

    private var inputSearchData: List<CityItem> = emptyList()
    private var filteredCities: MutableList<CityItem> = mutableListOf()

    class ViewHolder(val bindingHolder: ItemSearchBinding) :
        RecyclerView.ViewHolder(bindingHolder.root)

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemSearchBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        with(holder.bindingHolder) {
            val cityData = filteredCities[position]
            tvSearchCityName.text = cityData.city
            tvSearchCountryName.text = cityData.country
        }
    }

    override fun getItemCount(): Int = inputSearchData.size

    fun updateList(newList: List<CityItem>) {
        inputSearchData = newList
        filteredCities.clear()
        filteredCities.addAll(inputSearchData)
        notifyDataSetChanged()
    }
}