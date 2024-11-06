package com.kheemwel.mywatchlist.data.models

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class CountryModel : ViewModel() {
    private val _countries = MutableStateFlow(
        listOf(
            "America",
            "UK",
            "Korea",
            "Japan",
            "China",
            "Taiwan",
            "Thailand",
            "Philippines",
            "Indonesia",
            "India"
        )
    )
    val countries: StateFlow<List<String>> = _countries.asStateFlow()

    fun addCountry(country: String) {
        _countries.update { it + country }
    }

    fun updateCountry(index: Int, newCountry: String) {
        if (index in _countries.value.indices) {
            _countries.update { currentList ->
                currentList.toMutableList().apply { set(index, newCountry) }
            }
        }
    }

    fun deleteCountry(index: Int) {
        if (index in _countries.value.indices) {
            _countries.update { currentList ->
                currentList.toMutableList().apply { removeAt(index) }
            }
        }
    }

    fun deleteCountries(indexes: List<Int>) {
        _countries.update { currentList ->
            currentList.toMutableList().apply {
                indexes.sortedDescending().forEach { index ->
                    if (index in indices) {
                        removeAt(index)
                    }
                }
            }
        }
    }

    fun isCountryExists(country: String): Boolean {
        return _countries.value.contains(country)
    }
}