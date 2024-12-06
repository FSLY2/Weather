package com.example.weather.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.weather.databinding.ItemSearchBinding
import com.example.weather.models.CityAndCountry
import java.util.*

class SearchAdapter(private val listener: OnItemClickListener) : RecyclerView.Adapter<SearchAdapter.ViewHolder>() {

    private var inputSearchData: MutableList<CityAndCountry> = mutableListOf()
    private var filteredCities: MutableList<CityAndCountry> = mutableListOf()

    interface OnItemClickListener {
        fun onItemClick(cityAndCountry: CityAndCountry)
    }

    class ViewHolder(val bindingHolder: ItemSearchBinding) :
        RecyclerView.ViewHolder(bindingHolder.root) {
            fun bind(cityAndCountry: CityAndCountry, listener: OnItemClickListener) {
                with(bindingHolder) {
                    tvSearchCityName.text = cityAndCountry.cityName
                    tvSearchCountryName.text = cityAndCountry.countryName

                    itemView.setOnClickListener {
                        listener.onItemClick(cityAndCountry)
                    }
                }
            }
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemSearchBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(filteredCities[position], listener)
//        with(holder.bindingHolder) {
//            val cityData = filteredCities[position]
//            tvSearchCityName.text = cityData.cityName
//            tvSearchCountryName.text = cityData.countryName
//        }
    }

    override fun getItemCount(): Int = filteredCities.size

    fun filter(inputQuery: String?) {
        filteredCities.clear()

        if (inputQuery?.isEmpty() == true) {
            filteredCities.addAll(inputSearchData)
        } else {
            val query = inputQuery?.lowercase(Locale.getDefault())
            for (cityAndCountry in inputSearchData) {
                val cityName = cityAndCountry.cityName.lowercase(Locale.getDefault())
                if (query?.let { cityName.startsWith(it) } == true) {
                    filteredCities.add(cityAndCountry)
                }
            }
        }
        notifyDataSetChanged()
    }

    fun updateList(newList: List<CityAndCountry>) {
        inputSearchData.clear()
        inputSearchData.addAll(newList)
        filteredCities.clear()
        filteredCities.addAll(inputSearchData)
        notifyDataSetChanged()
    }
}