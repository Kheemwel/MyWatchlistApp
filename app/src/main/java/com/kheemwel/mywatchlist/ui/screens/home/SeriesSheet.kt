package com.kheemwel.mywatchlist.ui.screens.home

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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardOptions
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
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.kheemwel.mywatchlist.data.models.Series
import com.kheemwel.mywatchlist.data.models.SeriesModel
import com.kheemwel.mywatchlist.ui.composables.ComboBox
import com.kheemwel.mywatchlist.ui.composables.ConfirmationDialog
import com.kheemwel.mywatchlist.ui.composables.DatePickerField
import com.kheemwel.mywatchlist.ui.composables.MultiComboBox
import com.kheemwel.mywatchlist.utils.convertDateFormat

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddSeriesSheet(
    sheetState: SheetState,
    seriesModel: SeriesModel,
    genres: List<String>,
    countries: List<String>,
    statuses: List<String>,
    onDismiss: () -> Unit
) {
    var title by remember { mutableStateOf("") }
    var season by remember { mutableIntStateOf(1) }
    var episode by remember { mutableIntStateOf(1) }
    val seriesGenres = remember { mutableStateListOf<String>() }
    var country by remember { mutableStateOf("") }
    var status by remember { mutableStateOf("") }
    var isFavorite by remember { mutableStateOf(false) }
    var releaseDate by remember { mutableStateOf("") }
    var showConfirmationDialog by remember { mutableStateOf(false) }

    ModalBottomSheet(
        modifier = Modifier.fillMaxHeight(),
        sheetState = sheetState,
        dragHandle = null,
        onDismissRequest = onDismiss
    ) {
        ConfirmationDialog(
            state = showConfirmationDialog,
            title = "Discard Changes",
            message = "Are you sure you want to discard this series?",
            onDismiss = { showConfirmationDialog = false },
            onCancelText = "Discard",
            onCancel = {
                showConfirmationDialog = false
                onDismiss()
            },
            onConfirmText = "Keep Editing"
        ) {
            showConfirmationDialog = false
        }

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            item {
                Row {
                    IconButton(onClick = {
                        if (title.isNotEmpty() || seriesGenres.isNotEmpty() || country.isNotEmpty() || status.isNotEmpty() || releaseDate.isNotEmpty()) {
                            showConfirmationDialog = true
                        } else {
                            onDismiss()
                        }
                    }) {
                        Icon(Icons.Filled.Close, contentDescription = "Close")
                    }
                    Spacer(modifier = Modifier.weight(1f))
                    Button(
                        enabled = title.isNotEmpty() && seriesGenres.isNotEmpty() && country.isNotEmpty() && status.isNotEmpty() && releaseDate.isNotEmpty(),
                        onClick = {
                            seriesModel.addSeries(
                                Series(
                                    title = title,
                                    season = season,
                                    episode = episode,
                                    genres = seriesGenres.toList(),
                                    country = country,
                                    status = status,
                                    isFavorite = isFavorite,
                                    releaseDate = releaseDate,
                                )
                            )
                            onDismiss()
                        }) {
                        Text("Save")
                    }
                }
            }

            item {
                Text("* Required Fields")
            }

            item {
                ListItem(
                    leadingContent = { Icon(Icons.Filled.Star, contentDescription = "Favorite") },
                    modifier = Modifier.clickable { isFavorite = !isFavorite },
                    headlineContent = { Text("Favorite") },
                    trailingContent = {
                        Checkbox(checked = isFavorite, onCheckedChange = { isFavorite = it })
                    }
                )
            }

            item {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(
                        modifier = Modifier.fillMaxWidth(),
                        label = { Text("Title*") },
                        value = title,
                        onValueChange = { title = it },
                        singleLine = true,
                    )

                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        OutlinedTextField(
                            modifier = Modifier.weight(1f),
                            label = { Text("Season (Optional)") },
                            value = season.toString(),
                            onValueChange = { season = it.toIntOrNull() ?: 0 },
                            singleLine = true,
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                        )

                        OutlinedTextField(
                            modifier = Modifier.weight(1f),
                            label = { Text("Episode (Optional)") },
                            value = episode.toString(),
                            onValueChange = { episode = it.toIntOrNull() ?: 0 },
                            singleLine = true,
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                        )
                    }

                    MultiComboBox(label = "Genre", required = true, items = genres) { items ->
                        seriesGenres.clear()
                        seriesGenres.addAll(items)
                    }

                    ComboBox(label = "Country", required = true, items = countries) {
                        country = it
                    }
                }
            }

            item {
                Spacer(modifier = Modifier.height(32.dp))
            }

            item {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    ComboBox(label = "Status", required = true, items = statuses) { status = it }
                    DatePickerField("Release Date", required = true) { releaseDate = it }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ViewEditSeriesSheet(
    editMode: Boolean = false,
    series: Series?,
    sheetState: SheetState,
    seriesModel: SeriesModel,
    genres: List<String>,
    countries: List<String>,
    statuses: List<String>,
    onDismiss: () -> Unit
) {
    var isEditMode by remember { mutableStateOf(editMode) }
    var showExitDialog by remember { mutableStateOf(false) }

    ModalBottomSheet(
        modifier = Modifier.fillMaxHeight(),
        sheetState = sheetState,
        dragHandle = null,
        onDismissRequest = onDismiss
    ) {
        ConfirmationDialog(
            state = showExitDialog,
            title = "Discard Changes",
            message = "Are you sure you want to discard this series?",
            onDismiss = { showExitDialog = false },
            onCancelText = "Discard",
            onCancel = {
                showExitDialog = false
                onDismiss()
            },
            onConfirmText = "Keep Editing"
        ) {
            showExitDialog = false
        }

        if (series != null) {
            if (isEditMode) {
                EditSeries(series, seriesModel, statuses, genres, countries) { show ->
                    if (show) {
                        showExitDialog = true
                    } else {
                        onDismiss()
                    }
                }
            } else {
                ViewSeries(
                    series,
                    seriesModel,
                    statuses,
                    genres,
                    countries,
                    { isEditMode = true },
                    onDismiss
                )
            }
        }
    }
}

@Composable
private fun ViewSeries(
    series: Series,
    seriesModel: SeriesModel,
    statuses: List<String>,
    genres: List<String>,
    countries: List<String>,
    onEditMode: () -> Unit,
    onExit: () -> Unit
) {
    var showDeleteDialog by remember { mutableStateOf(false) }

    ConfirmationDialog(
        state = showDeleteDialog,
        title = "Delete Series",
        message = "Are you sure you want to delete this series?",
        onDismiss = { showDeleteDialog = false },
        onCancelText = "Cancel",
        onCancel = {
            showDeleteDialog = false
        },
        onConfirmText = "Ok"
    ) {
        seriesModel.deleteSeries(series.uuid)
        showDeleteDialog = false
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
                    headlineContent = { Text(series.title) },
                    trailingContent = if (series.isFavorite) {
                        { Icon(Icons.Filled.Star, contentDescription = "Favorite") }
                    } else {
                        null
                    }
                )
                ListItem(
                    overlineContent = { Text("Status") },
                    headlineContent = { Text(series.status) }
                )
                ListItem(
                    overlineContent = { Text("Season") },
                    headlineContent = { Text(series.season.toString()) }
                )
                ListItem(
                    overlineContent = { Text("Episode") },
                    headlineContent = { Text(series.episode.toString()) }
                )
                ListItem(
                    overlineContent = { Text("Genre") },
                    headlineContent = { Text(series.genres.joinToString()) }
                )
                ListItem(
                    overlineContent = { Text("Country") },
                    headlineContent = { Text(series.country) }
                )
                ListItem(
                    overlineContent = { Text("Release Date") },
                    headlineContent = { Text(convertDateFormat(series.releaseDate)) }
                )
            }
        }
    }
}

@Composable
private fun EditSeries(
    series: Series,
    seriesModel: SeriesModel,
    statuses: List<String>,
    genres: List<String>,
    countries: List<String>,
    onExit: (showConfirmExitDialog: Boolean) -> Unit
) {
    var title by remember { mutableStateOf(series.title) }
    var season by remember { mutableIntStateOf(series.season) }
    var episode by remember { mutableIntStateOf(series.episode) }
    val seriesGenres =
        remember(series.genres) { mutableStateListOf<String>().also { it.addAll(series.genres) } }
    var country by remember { mutableStateOf(series.country) }
    var status by remember { mutableStateOf(series.status) }
    var isFavorite by remember { mutableStateOf(series.isFavorite) }
    var releaseDate by remember { mutableStateOf(series.releaseDate) }

    var showSaveDialog by remember { mutableStateOf(false) }

    val newSeries = series.copy(
        title = title,
        season = season,
        episode = episode,
        genres = seriesGenres.toList(),
        country = country,
        status = status,
        isFavorite = isFavorite,
        releaseDate = releaseDate,
    )

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
        seriesModel.updateSeries(series.uuid, newSeries)
        showSaveDialog = false
        onExit(false)
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        item {
            Row {
                IconButton(onClick = {
                    if (series != newSeries) {
                        onExit(true)
                    } else {
                        onExit(false)
                    }
                }) {
                    Icon(Icons.Filled.Close, contentDescription = "Close")
                }
                Spacer(modifier = Modifier.weight(1f))
                Button(
                    enabled = title.isNotEmpty() && seriesGenres.isNotEmpty() && country.isNotEmpty() && status.isNotEmpty() && releaseDate.isNotEmpty() && (newSeries != series),
                    onClick = {
                        if (series != newSeries) {
                            showSaveDialog = true
                        }
                    }) {
                    Text("Save Changes")
                }
            }
        }

        item {
            Text("* Required Fields")
        }

        item {
            ListItem(
                leadingContent = { Icon(Icons.Filled.Star, contentDescription = "Favorite") },
                modifier = Modifier.clickable { isFavorite = !isFavorite },
                headlineContent = { Text("Favorite") },
                trailingContent = {
                    Checkbox(checked = isFavorite, onCheckedChange = { isFavorite = it })
                }
            )
        }

        item {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text("Title*") },
                    value = title,
                    onValueChange = { title = it },
                    singleLine = true,
                )

                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(
                        modifier = Modifier.weight(1f),
                        label = { Text("Season (Optional)") },
                        value = season.toString(),
                        onValueChange = { season = it.toIntOrNull() ?: 0 },
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                    )

                    OutlinedTextField(
                        modifier = Modifier.weight(1f),
                        label = { Text("Episode (Optional)") },
                        value = episode.toString(),
                        onValueChange = { episode = it.toIntOrNull() ?: 0 },
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                    )
                }

                MultiComboBox(
                    label = "Genre",
                    initialValues = seriesGenres,
                    required = true,
                    items = genres
                ) { items ->
                    seriesGenres.clear()
                    seriesGenres.addAll(items)
                }

                ComboBox(
                    label = "Country",
                    initialValue = country,
                    required = true,
                    items = countries
                ) {
                    country = it
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
                    initialValue = status,
                    required = true,
                    items = statuses
                ) { status = it }
                DatePickerField(
                    "Release Date",
                    initialValue = releaseDate,
                    required = true
                ) { releaseDate = it }
            }
        }
    }
}
