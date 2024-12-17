package com.kheemwel.mywatchlist.presentation.screens.countries_creen

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.SnackbarHost
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.kheemwel.mywatchlist.presentation.composables.AddFloatingButton
import com.kheemwel.mywatchlist.presentation.composables.ConfirmationDialog
import com.kheemwel.mywatchlist.presentation.composables.TagForm
import com.kheemwel.mywatchlist.presentation.composables.TagScaffold
import com.kheemwel.mywatchlist.presentation.composables.TagTile

@Composable
fun CountriesScreen(navController: NavController, viewModel: CountriesScreenViewModel = hiltViewModel()) {
    val state = viewModel.state.value
    val countries = state.countries
    val selectedCountries = state.selectedCountries

    val appBarTitle = if (state.isSelectionMode) {
        "Selected ${selectedCountries.size} ${if (selectedCountries.size == 1) "item" else "items"}"
    } else {
        "Edit Countries"
    }

    val formTitle = if (state.selectedId != null) {
        "Rename Country"
    } else {
        "Add Country"
    }

    BackHandler(enabled = state.isSelectionMode) {
        viewModel.onEvent(CountriesScreenEvent.ToggleSelectionMode(false))
    }

    TagScaffold(
        navController,
        title = appBarTitle,
        snackbarHost = { SnackbarHost(hostState = viewModel.snackbarHostState) },
        floatingActionButton = {
            AddFloatingButton {
                viewModel.onEvent(CountriesScreenEvent.OpenForm())
            }
        },
        selectionMode = state.isSelectionMode,
        onDeselectAll = {
            viewModel.onEvent(CountriesScreenEvent.DeselectAll)
        },
        onInvertSelected = {
            viewModel.onEvent(CountriesScreenEvent.InvertSelected)
        },
        onSelectAll = {
            viewModel.onEvent(CountriesScreenEvent.SelectAll)
        },
        enableDelete = state.isSelectionMode && selectedCountries.isNotEmpty(),
        onDelete = {
            viewModel.onEvent(CountriesScreenEvent.OpenDeleteSelectedCountriesDialog)
        },
        onCancelSelection = {
            viewModel.onEvent(CountriesScreenEvent.ToggleSelectionMode(false))
        }
    ) { paddingValues ->
        TagForm(
            show = state.isFormOpen,
            title = formTitle,
            isError = state.error != null,
            errorMessage = state.error,
            value = state.inputName,
            onValueChange = { country ->
                viewModel.onEvent(CountriesScreenEvent.EnterCountry(country))
            },
            onCancel = {
                viewModel.onEvent(CountriesScreenEvent.CloseForm)
            }
        ) { input ->
            if (state.selectedId != null) {
                viewModel.onEvent(CountriesScreenEvent.UpdateCountry(state.selectedId, input))
            } else {
                viewModel.onEvent(CountriesScreenEvent.AddCountry(input))
            }

            if (state.error == null) {
                viewModel.onEvent(CountriesScreenEvent.CloseForm)
            }
        }

        ConfirmationDialog(
            state = state.isDeleteCountryDialogOpen,
            title = "Delete Country",
            message = "Do you want to delete country \"${state.inputName}\" ?",
            onCancel = {
                viewModel.onEvent(CountriesScreenEvent.CloseDeleteCountryDialog)
            }) {
            state.selectedId?.let {
                viewModel.onEvent(CountriesScreenEvent.DeleteCountry(it))
            }
            viewModel.onEvent(CountriesScreenEvent.CloseDeleteCountryDialog)
        }

        ConfirmationDialog(
            state = state.isDeleteSelectedCountriesDialogOpen,
            title = "Delete ${selectedCountries.size} ${if (selectedCountries.size == 1) "Country" else "Countries"}",
            message = "Are you sure you want to delete this ${if (selectedCountries.size == 1) "country" else "countries"}?",
            onCancel = {
                viewModel.onEvent(CountriesScreenEvent.CloseDeleteSelectedCountriesDialog)
            }) {
            viewModel.onEvent(CountriesScreenEvent.DeleteSelectedCountries)
            viewModel.onEvent(CountriesScreenEvent.CloseDeleteSelectedCountriesDialog)
            viewModel.onEvent(CountriesScreenEvent.ToggleSelectionMode(false))
        }

        LazyColumn(modifier = Modifier.padding(paddingValues)) {
            item {
                if (state.isLoading) {
                    LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
                }
            }
            items(countries, key = { it.id }) { country ->
                TagTile(
                    modifier = Modifier.animateItem(),
                    text = country.name,
                    onRename = {
                        viewModel.onEvent(CountriesScreenEvent.OpenForm(country.id, country.name))
                    }, onDelete = {
                        viewModel.onEvent(CountriesScreenEvent.OpenDeleteCountryDialog(country.id, country.name))
                    },
                    selectionMode = state.isSelectionMode,
                    selected = selectedCountries.contains(country.id),
                    onClick = {
                        if (state.isSelectionMode) {
                            viewModel.onEvent(CountriesScreenEvent.ToggleSelectedCountry(country.id))
                        }
                    },
                    onLongClick = {
                        viewModel.onEvent(CountriesScreenEvent.ToggleSelectionMode(true))
                        viewModel.onEvent(CountriesScreenEvent.ToggleSelectedCountry(country.id))
                    }
                )
            }
            item {
                Spacer(modifier = Modifier.height(150.dp))
            }
        }
    }
}