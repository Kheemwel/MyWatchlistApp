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
    val movieGenres = remember { mutableStateListOf<String>() }
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
        if (showConfirmationDialog) {
            ConfirmationDialog(
                state = showConfirmationDialog,
                title = "Discard Changes",
                message = "Are you sure you want to discard this movie?",
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
        }

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            item {
                Row {
                    IconButton(onClick = {
                        if (title.isNotEmpty() || movieGenres.isNotEmpty() || country.isNotEmpty() || status.isNotEmpty() || releaseDate.isNotEmpty()) {
                            showConfirmationDialog = true
                        } else {
                            onDismiss()
                        }
                    }) {
                        Icon(Icons.Filled.Close, contentDescription = "Close")
                    }
                    Spacer(modifier = Modifier.weight(1f))
                    Button(
                        enabled = title.isNotEmpty() && movieGenres.isNotEmpty() && country.isNotEmpty() && status.isNotEmpty() && releaseDate.isNotEmpty(),
                        onClick = {
                            seriesModel.addSeries(
                                Series(
                                    title = title,
                                    season = season,
                                    episode = episode,
                                    genres = movieGenres,
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
                        movieGenres.clear()
                        movieGenres.addAll(items)
                    }

                    ComboBox(label = "County", required = true, items = countries) {
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
                    DatePickerField("Release Date", true) { releaseDate = it }
                }
            }
        }
    }
}