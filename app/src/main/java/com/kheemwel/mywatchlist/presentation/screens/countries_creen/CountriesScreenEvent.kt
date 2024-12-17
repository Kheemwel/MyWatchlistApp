package com.kheemwel.mywatchlist.presentation.screens.countries_creen

sealed class CountriesScreenEvent {
    data class OpenForm(val id: Long? = null, val name: String = "") : CountriesScreenEvent()
    data object CloseForm : CountriesScreenEvent()
    data class OpenDeleteCountryDialog(val id: Long, val name: String) : CountriesScreenEvent()
    data object CloseDeleteCountryDialog : CountriesScreenEvent()
    data object OpenDeleteSelectedCountriesDialog : CountriesScreenEvent()
    data object CloseDeleteSelectedCountriesDialog : CountriesScreenEvent()
    data class ToggleSelectionMode(val value: Boolean) : CountriesScreenEvent()
    data class ToggleSelectedCountry(val id: Long) : CountriesScreenEvent()
    data object SelectAll : CountriesScreenEvent()
    data object InvertSelected : CountriesScreenEvent()
    data object DeselectAll : CountriesScreenEvent()
    data class EnterCountry(val name: String) : CountriesScreenEvent()
    data class AddCountry(val name: String) : CountriesScreenEvent()
    data class UpdateCountry(val id: Long, val newName: String) : CountriesScreenEvent()
    data class DeleteCountry(val id: Long) : CountriesScreenEvent()
    data object DeleteSelectedCountries : CountriesScreenEvent()
}