package com.kheemwel.mywatchlist.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.kheemwel.mywatchlist.data.models.CountryModel
import com.kheemwel.mywatchlist.ui.composables.ConfirmationDialog
import com.kheemwel.mywatchlist.ui.composables.TagForm
import com.kheemwel.mywatchlist.ui.composables.TagScaffold
import com.kheemwel.mywatchlist.ui.composables.TagTile

@Composable
fun CountriesScreen(navController: NavController, viewModel: CountryModel) {
    val countries by viewModel.countries.collectAsState()
    var openAddEditDialog by remember { mutableStateOf(false) }
    var openDeleteItemDialog by remember { mutableStateOf(false) }
    var isCountryExist by remember { mutableStateOf(false) }
    var isEditMode by remember { mutableStateOf(false) }
    var selectedCountryIndex by remember { mutableIntStateOf(-1) }
    val dialogTitle = if (isEditMode) {
        "Rename Country"
    } else {
        "Add Country"
    }
    var selectionMode by remember { mutableStateOf(false) }
    val selectedItems = remember { mutableStateListOf<Int>() }
    var openDeleteSelectedDialog by remember { mutableStateOf(false) }

    BackHandler(enabled = selectionMode) {
        selectionMode = false
        selectedItems.clear()
    }

    val title = if (selectionMode) {
        "Selected ${selectedItems.size} ${if (selectedItems.size == 1) "item" else "items"}"
    } else {
        "Edit Countries"
    }

    TagScaffold(navController, title = title, floatingActionButton = {
        ExtendedFloatingActionButton(
            onClick = {
                openAddEditDialog = true
            },
            icon = { Icon(Icons.Filled.Add, "Add") },
            text = { Text(text = "Add") },
        )
    },
        selectionMode = selectionMode,
        onSelectAll = { selectAll ->
            if (selectAll) {
                selectedItems.clear()
                selectedItems.addAll(countries.indices)
            } else {
                selectedItems.clear()
            }
        },
        enableDelete = selectionMode && !selectedItems.isEmpty(),
        onDelete = {
            openDeleteSelectedDialog = true
        },
        onCancelSelection = {
            selectionMode = false
            selectedItems.clear()
        }
    ) { paddingValues ->
        TagForm(
            show = openAddEditDialog,
            title = dialogTitle,
            initialValue = if (isEditMode) countries[selectedCountryIndex] else "",
            isError = isCountryExist,
            errorMessage = "Country already exists",
            onTextChange = { genre ->
                isCountryExist = viewModel.isCountryExists(genre)
            },
            onCancel = {
                isCountryExist = false
                openAddEditDialog = false
            }) { genre ->
            if (isEditMode) {
                viewModel.updateCountry(selectedCountryIndex, genre)
            } else {
                viewModel.addCountry(genre)
            }
            openAddEditDialog = false
            isCountryExist = false
            isEditMode = false
            selectedCountryIndex = -1
        }
        ConfirmationDialog(
            state = openDeleteItemDialog,
            title = "Delete Country",
            message = "Do you want to delete country \"${if (selectedCountryIndex >= 0) countries[selectedCountryIndex] else ""}\" ?",
            onCancel = {
                openDeleteItemDialog = false
            }) {
            viewModel.deleteCountry(selectedCountryIndex)
            selectedCountryIndex = -1
            openDeleteItemDialog = false
        }
        ConfirmationDialog(
            state = openDeleteSelectedDialog,
            title = "Delete ${selectedItems.size} ${if (selectedItems.size == 1) "Country" else "Countries"}",
            message = "Are you sure you want to delete this ${if (selectedItems.size == 1) "country" else "countries"}?",
            onCancel = {
                openDeleteSelectedDialog = false
            }) {
            viewModel.deleteCountries(selectedItems)
            selectedItems.clear()
            openDeleteSelectedDialog = false
            selectionMode = false
        }
        LazyColumn(modifier = Modifier.padding(paddingValues)) {
            itemsIndexed(countries, key = { _, item -> item.hashCode() }) { index, item ->
                TagTile(item, onRename = {
                    selectedCountryIndex = index
                    isEditMode = true
                    openAddEditDialog = true
                }, onDelete = {
                    selectedCountryIndex = index
                    openDeleteItemDialog = true
                },
                    selectionMode = selectionMode,
                    selected = selectedItems.contains(index),
                    onClick = {
                        if (selectionMode) {
                            if (selectedItems.contains(index)) {
                                selectedItems.remove(index)
                            } else {
                                selectedItems.add(index)
                            }
                        }
                    },
                    onLongClick = {
                        selectionMode = true
                        if (!selectedItems.contains(index)) {
                            selectedItems.add(index)
                        }
                    }
                )
            }
            item {
                Spacer(modifier = Modifier.height(150.dp))
            }
        }
    }
}