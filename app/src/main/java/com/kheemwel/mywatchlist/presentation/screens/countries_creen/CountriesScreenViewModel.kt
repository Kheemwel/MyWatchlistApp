package com.kheemwel.mywatchlist.presentation.screens.countries_creen

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kheemwel.mywatchlist.domain.model.Country
import com.kheemwel.mywatchlist.domain.usecase.country_usecase.CountryUseCases
import com.kheemwel.mywatchlist.presentation.composables.showSnackbar
import com.kheemwel.mywatchlist.utils.update
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CountriesScreenViewModel @Inject constructor(
    private val useCases: CountryUseCases
) : ViewModel() {
    private val _state = mutableStateOf(CountriesScreenState())
    val state: State<CountriesScreenState> = _state
    private var countriesJob: Job? = null
    val snackbarHostState = SnackbarHostState()

    init {
        getCountries()
    }

    fun onEvent(event: CountriesScreenEvent) {
        when (event) {
            is CountriesScreenEvent.EnterCountry -> enterCountry(event.name)
            is CountriesScreenEvent.AddCountry -> addCountry(event.name)
            is CountriesScreenEvent.UpdateCountry -> updateCountry(event.id, event.newName)
            is CountriesScreenEvent.DeleteCountry -> deleteCountry(event.id)
            is CountriesScreenEvent.DeleteSelectedCountries -> deleteSelectedCountries()
            is CountriesScreenEvent.OpenForm -> openForm(event.id, event.name)
            CountriesScreenEvent.CloseForm -> closeForm()
            is CountriesScreenEvent.OpenDeleteCountryDialog -> openDeleteCountryDialog(event.id, event.name)
            CountriesScreenEvent.CloseDeleteCountryDialog -> closeDeleteCountryDialog()
            CountriesScreenEvent.OpenDeleteSelectedCountriesDialog -> openDeleteSelectedCountriesDialog()
            CountriesScreenEvent.CloseDeleteSelectedCountriesDialog -> closeDeleteSelectedCountriesDialog()
            is CountriesScreenEvent.ToggleSelectionMode -> toggleSelectionMode(event.value)
            is CountriesScreenEvent.ToggleSelectedCountry -> toggleSelectedCountry(event.id)
            CountriesScreenEvent.SelectAll -> selectAll()
            CountriesScreenEvent.InvertSelected -> invertSelected()
            CountriesScreenEvent.DeselectAll -> deselectAll()
        }
    }

    private fun getCountries() {
        countriesJob?.cancel()
        countriesJob = useCases.getAllCountriesUseCase()
            .onStart {
                _state.update { copy(isLoading = true) }
            }
            .onCompletion { error ->
                error?.message?.let { showSnackbar(snackbarHostState, it) }
            }
            .onEach {
                _state.update { copy(countries = it, isLoading = false) }
            }.launchIn(viewModelScope)
    }

    private fun enterCountry(name: String) {
        _state.update {
            copy(
                error = if (doesCountryExist(name)) "Country already exist" else null,
                inputName = name
            )
        }
    }

    private fun addCountry(name: String) {
        if (doesCountryExist(name)) {
            _state.update {
                copy(
                    error = "Country already exist",
                )
            }
            return
        }

        viewModelScope.launch {
            try {
                useCases.addCountryUseCase(Country(name = name))
            } catch (e: Exception) {
                _state.update { copy(error = e.message) }
            }
        }
    }

    private fun updateCountry(id: Long, newName: String) {
        viewModelScope.launch {
            try {
                useCases.updateCountryUseCase(Country(id, newName))
            } catch (e: Exception) {
                _state.update { copy(error = e.message) }
            }
        }
    }

    private fun deleteCountry(id: Long) {
        viewModelScope.launch {
            try {
                useCases.deleteCountryUseCase(id)
            } catch (e: Exception) {
                e.message?.let { showSnackbar(snackbarHostState, it) }
            }
        }
    }

    private fun deleteSelectedCountries() {
        viewModelScope.launch {
            try {
                val selected = _state.value.selectedCountries
                useCases.deleteManyCountriesUseCase(selected)
            } catch (e: Exception) {
                e.message?.let { showSnackbar(snackbarHostState, it) }
            }
        }
    }

    private fun openForm(id: Long?, name: String) {
        _state.update { copy(isFormOpen = true, selectedId = id, inputName = name) }
    }

    private fun closeForm() {
        _state.update { copy(isFormOpen = false, error = null, selectedId = null, inputName = "") }
    }

    private fun openDeleteCountryDialog(id: Long, name: String) {
        _state.update { copy(isDeleteCountryDialogOpen = true, selectedId = id, inputName = name) }
    }

    private fun closeDeleteCountryDialog() {
        _state.update { copy(isDeleteCountryDialogOpen = false, selectedId = null, inputName = "") }
    }

    private fun openDeleteSelectedCountriesDialog() {
        _state.update { copy(isDeleteSelectedCountriesDialogOpen = true) }
    }


    private fun closeDeleteSelectedCountriesDialog() {
        _state.update { copy(isDeleteSelectedCountriesDialogOpen = false) }
    }

    private fun toggleSelectionMode(value: Boolean) {
        _state.update { copy(isSelectionMode = value) }
        if (!value) {
            deselectAll()
        }
    }

    private fun toggleSelectedCountry(id: Long) {
        val countries = _state.value.selectedCountries.toMutableList()
        if (countries.contains(id)) {
            countries.remove(id)
        } else {
            countries.add(id)
        }
        _state.update { copy(selectedCountries = countries.toList()) }
    }

    private fun selectAll() {
        val countries = _state.value.countries.map { it.id }
        _state.update { copy(selectedCountries = countries) }
    }


    private fun invertSelected() {
        val countries = _state.value.countries.map { it.id }
        val inverted = countries.filterNot { it in _state.value.selectedCountries }
        _state.update { copy(selectedCountries = inverted) }
    }

    private fun deselectAll() {
        _state.update { copy(selectedCountries = emptyList()) }
    }

    private fun doesCountryExist(name: String): Boolean {
        return _state.value.countries.any { it.name == name }
    }
}