package com.kheemwel.mywatchlist.presentation.screens.home_screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.kheemwel.mywatchlist.R
import com.kheemwel.mywatchlist.domain.model.Country
import com.kheemwel.mywatchlist.domain.model.Genre
import com.kheemwel.mywatchlist.domain.model.Status
import com.kheemwel.mywatchlist.presentation.composables.ComboBox
import com.kheemwel.mywatchlist.presentation.composables.ConfirmationDialog
import com.kheemwel.mywatchlist.presentation.composables.DatePickerField
import com.kheemwel.mywatchlist.presentation.composables.MultiComboBox
import com.kheemwel.mywatchlist.utils.convertDateFormat

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MovieSheet(
    sheetState: SheetState,
    error: String?,
    editMode: Boolean,
    onEditMode: () -> Unit,
    genres: List<Genre>,
    countries: List<Country>,
    statuses: List<Status>,
    selectedId: Long?,
    inputTitle: String,
    onEnterTitle: (String) -> Unit,
    inputStatus: Status?,
    onSelectStatus: (Status) -> Unit,
    inputCountry: Country?,
    onSelectCountry: (Country) -> Unit,
    inputGenres: List<Genre>,
    onSelectGenre: (Genre) -> Unit,
    inputReleaseDate: String,
    onEnterReleaseDate: (String) -> Unit,
    inputFavorite: Boolean,
    onToggleFavorite: () -> Unit,
    onTransfer: () -> Unit,
    onSwitchToSeries: () -> Unit,
    onSave: () -> Unit,
    onDelete: () -> Unit,
    onDismiss: () -> Unit
) {
    ModalBottomSheet(
        modifier = Modifier.fillMaxHeight(),
        sheetState = sheetState,
        dragHandle = null,
        onDismissRequest = onDismiss
    ) {
        if (editMode) {
            AddEditMovie(
                error = error,
                genres = genres,
                countries = countries,
                statuses = statuses,
                selectedId = selectedId,
                inputTitle = inputTitle,
                onEnterTitle = onEnterTitle,
                inputStatus = inputStatus,
                onSelectStatus = onSelectStatus,
                inputCountry = inputCountry,
                onSelectCountry = onSelectCountry,
                inputGenres = inputGenres,
                onSelectGenre = onSelectGenre,
                inputReleaseDate = inputReleaseDate,
                onEnterReleaseDate = onEnterReleaseDate,
                inputFavorite = inputFavorite,
                onToggleFavorite = onToggleFavorite,
                onSwitchToSeries = onSwitchToSeries,
                onSave = onSave,
                onExit = onDismiss
            )
        } else {
            ViewMovie(
                title = inputTitle,
                status = inputStatus?.name ?: "",
                country = inputCountry?.name ?: "",
                genres = inputGenres.map { it.name },
                releaseDate = inputReleaseDate,
                isFavorite = inputFavorite,
                onTransfer = onTransfer,
                onEditMode = { onEditMode() },
                onDelete = onDelete,
                onExit = onDismiss
            )
        }
    }
}

@Composable
private fun ViewMovie(
    title: String,
    status: String,
    country: String,
    genres: List<String>,
    releaseDate: String,
    isFavorite: Boolean,
    onTransfer: () -> Unit,
    onEditMode: () -> Unit,
    onDelete: () -> Unit,
    onExit: () -> Unit
) {
    var showDeleteDialog by remember { mutableStateOf(false) }
    var showTransferToSeriesDialog by remember { mutableStateOf(false) }

    ConfirmationDialog(
        state = showDeleteDialog,
        title = "Delete Movie",
        message = "Are you sure you want to delete this movie?",
        onDismiss = { showDeleteDialog = false },
        onCancelText = "Cancel",
        onCancel = {
            showDeleteDialog = false
        },
        onConfirmText = "Ok"
    ) {
        onDelete()
        showDeleteDialog = false
        onExit()
    }

    ConfirmationDialog(
        state = showTransferToSeriesDialog,
        title = "Transfer To Series",
        message = "Do you want to transfer this movie to series?",
        onDismiss = { showTransferToSeriesDialog = false },
        onCancelText = "Cancel",
        onCancel = { showTransferToSeriesDialog = false },
        onConfirmText = "Ok"
    ) {
        onTransfer()
        showTransferToSeriesDialog = false
        onExit()
    }


    LazyColumn(modifier = Modifier.fillMaxSize()) {
        item {
            Row(modifier = Modifier.padding(16.dp)) {
                IconButton(onClick = { onExit() }) {
                    Icon(Icons.Filled.Close, contentDescription = "Close")
                }

                Spacer(modifier = Modifier.weight(1f))

                Row {
                    IconButton(onClick = { showTransferToSeriesDialog = true }) {
                        Icon(painterResource(R.drawable.baseline_swap_horiz_24), contentDescription = "Transfer")
                    }

                    IconButton(onClick = onEditMode) {
                        Icon(Icons.Filled.Edit, contentDescription = "Edit")
                    }

                    IconButton(onClick = { showDeleteDialog = true }) {
                        Icon(Icons.Filled.Delete, contentDescription = "Delete")
                    }
                }
            }
        }
        item {
            Column {
                ListItem(
                    overlineContent = { Text("Title") },
                    headlineContent = { Text(title) },
                    trailingContent = if (isFavorite) {
                        { Icon(Icons.Filled.Star, contentDescription = "Favorite") }
                    } else {
                        null
                    }
                )
                ListItem(
                    overlineContent = { Text("Status") },
                    headlineContent = { Text(status) }
                )
                ListItem(
                    overlineContent = { Text("Genre") },
                    headlineContent = { Text(genres.joinToString()) }
                )
                ListItem(
                    overlineContent = { Text("Country") },
                    headlineContent = { Text(country) }
                )
                ListItem(
                    overlineContent = { Text("Release Date") },
                    headlineContent = { Text(convertDateFormat(releaseDate)) }
                )
            }
        }
    }
}

@Composable
private fun AddEditMovie(
    error: String?,
    genres: List<Genre>,
    countries: List<Country>,
    statuses: List<Status>,
    selectedId: Long?,
    inputTitle: String,
    onEnterTitle: (String) -> Unit,
    inputStatus: Status?,
    onSelectStatus: (Status) -> Unit,
    inputCountry: Country?,
    onSelectCountry: (Country) -> Unit,
    inputGenres: List<Genre>,
    onSelectGenre: (Genre) -> Unit,
    inputReleaseDate: String,
    onEnterReleaseDate: (String) -> Unit,
    inputFavorite: Boolean,
    onToggleFavorite: () -> Unit,
    onSwitchToSeries: () -> Unit,
    onSave: () -> Unit,
    onExit: () -> Unit,
) {
    val initialInputTitle = remember { inputTitle }
    val initialInputStatus = remember { inputStatus }
    val initialInputCountry = remember { inputCountry }
    val initialInputGenres = remember { inputGenres }
    val initialInputReleaseDate = remember { inputReleaseDate }
    val initialInputFavorite = remember { inputFavorite }
    var showDiscardDialog by remember { mutableStateOf(false) }
    var showSaveDialog by remember { mutableStateOf(false) }
    val haveChanges = initialInputTitle != inputTitle || initialInputStatus != inputStatus || initialInputCountry != inputCountry
            || initialInputGenres != inputGenres || initialInputReleaseDate != inputReleaseDate || initialInputFavorite != inputFavorite

    ConfirmationDialog(
        state = showDiscardDialog,
        title = "Discard Changes",
        message = "Are you sure you want to discard this movie?",
        onDismiss = { showDiscardDialog = false },
        onCancelText = "Discard",
        onCancel = {
            showDiscardDialog = false
            onExit()
        },
        onConfirmText = "Keep Editing"
    ) {
        showDiscardDialog = false
    }

    ConfirmationDialog(
        state = showSaveDialog,
        title = "Save Changes",
        message = "Are you sure you want to save changes?",
        onDismiss = { showSaveDialog = false },
        onCancelText = "Cancel",
        onCancel = {
            showSaveDialog = false
        },
        onConfirmText = "Ok"
    ) {
        onSave()
        showSaveDialog = false
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        item {
            Row {
                IconButton(onClick = {
                    if (haveChanges) {
                        showDiscardDialog = true
                    } else {
                        onExit()
                    }
                }) {
                    Icon(Icons.Filled.Close, contentDescription = "Close")
                }
                Spacer(modifier = Modifier.weight(1f))
                if (selectedId == null) {
                    Button(onClick = onSwitchToSeries) {
                        Text("Switch to Series")
                    }
                    Spacer(modifier = Modifier.width(16.dp))
                }
                Button(
                    enabled = inputTitle.isNotBlank() && haveChanges,
                    onClick = {
                        if (selectedId != null) {
                            showSaveDialog = true
                        } else {
                            onSave()
                        }
                    }) {
                    Text("Save${if (selectedId == null) "" else " Changes"}")
                }
            }
        }

        item {
            Text("* Required Fields")
        }

        item {
            error?.let { Text(it, color = MaterialTheme.colorScheme.error) }
        }

        item {
            ListItem(
                leadingContent = { Icon(Icons.Filled.Star, contentDescription = "Favorite") },
                modifier = Modifier.clickable { onToggleFavorite() },
                headlineContent = { Text("Favorite") },
                trailingContent = {
                    Checkbox(checked = inputFavorite, onCheckedChange = null)
                }
            )
        }

        item {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text("Title*") },
                    value = inputTitle,
                    onValueChange = { onEnterTitle(it) },
                    singleLine = true,
                )

                MultiComboBox(
                    label = "Genre",
                    items = genres.map { Pair(it.id, it.name) },
                    selectedItems = inputGenres.map { Pair(it.id, it.name) },
                ) {
                    onSelectGenre(Genre(it.first, it.second))
                }

                ComboBox(
                    label = "Country",
                    items = countries.map { Pair(it.id, it.name) },
                    selectedItem = inputCountry?.let { Pair(it.id, it.name) } ?: Pair(0, "")
                ) {
                    onSelectCountry(Country(it.first, it.second))
                }
            }
        }

        item {
            Spacer(modifier = Modifier.height(32.dp))
        }

        item {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                ComboBox(
                    label = "Status",
                    items = statuses.map { Pair(it.id, it.name) },
                    selectedItem = inputStatus?.let { Pair(it.id, it.name) } ?: Pair(0, "")
                ) {
                    onSelectStatus(Status(it.first, it.second))
                }
                DatePickerField(
                    "Release Date",
                    value = inputReleaseDate
                ) {
                    onEnterReleaseDate(it)
                }
            }
        }
    }
}