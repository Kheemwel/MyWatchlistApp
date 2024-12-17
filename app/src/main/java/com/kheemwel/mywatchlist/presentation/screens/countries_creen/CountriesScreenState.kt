package com.kheemwel.mywatchlist.presentation.screens.countries_creen

import com.kheemwel.mywatchlist.domain.model.Country

data class CountriesScreenState(
    val countries: List<Country> = emptyList(),
    val selectedCountries: List<Long> = emptyList(),
    val inputName: String = "",
    val selectedId: Long? = null,
    val isLoading: Boolean = false,
    val error: String? = null,
    val isSelectionMode: Boolean = false,
    val isFormOpen: Boolean = false,
    val isDeleteCountryDialogOpen: Boolean = false,
    val isDeleteSelectedCountriesDialogOpen: Boolean = false
)
