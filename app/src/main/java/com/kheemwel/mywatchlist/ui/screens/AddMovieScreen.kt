package com.kheemwel.mywatchlist.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.kheemwel.mywatchlist.data.AppData
import com.kheemwel.mywatchlist.data.Movie
import com.kheemwel.mywatchlist.ui.composables.DropDownSelector
import com.kheemwel.mywatchlist.ui.composables.InputText
import com.kheemwel.mywatchlist.ui.composables.MyWatchlistScaffold
import kotlinx.coroutines.launch

@Composable
fun AddMovieScreen() {
    var title by remember { mutableStateOf("") }
    var hours by remember { mutableIntStateOf(0) }
    var minutes by remember { mutableIntStateOf(0) }
    var status by remember { mutableStateOf("") }
    var genre by remember { mutableStateOf("") }
    var country by remember { mutableStateOf("") }
    var favorite by remember { mutableStateOf(false) }


    val coroutineScope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    MyWatchlistScaffold(
        title = "Add Movie",
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier.padding(innerPadding),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                Text("Title")
                InputText(label = "Title", value = title) {
                    title = it
                }
            }
            item {
                Text("Duration")
                Row {
                    InputText(
                        label = "Hours",
                        value = hours.toString(),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                    ) {
                        hours = it.toIntOrNull() ?: 0
                    }
                    InputText(
                        label = "Minutes",
                        value = minutes.toString(),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                    ) {
                        minutes = it.toIntOrNull() ?: 0
                    }
                }
            }
            item {
                Text("Status")
                DropDownSelector(
                    label = "Status",
                    options = AppData.statusList,
                    selectedOption = status,
                    onOptionSelected = { status = it }
                )
            }
            item {
                Text("Genre")
                DropDownSelector(
                    label = "Genre",
                    options = AppData.genreList,
                    selectedOption = genre,
                    onOptionSelected = { genre = it }
                )
            }
            item {
                Text("Country")
                DropDownSelector(
                    label = "Country",
                    options = AppData.countryList,
                    selectedOption = country,
                    onOptionSelected = { country = it }
                )
            }
            item {
                Text("Favorite")
                Checkbox(
                    checked = favorite,
                    onCheckedChange = { favorite = it }
                )
            }
            item {
                Button(onClick = {
                    AppData.movieList.add(
                        Movie(
                            title,
                            "${hours}hrs ${minutes}mins",
                            status,
                            genre,
                            country,
                            favorite
                        )
                    )
                    title = ""
                    hours = 0
                    minutes = 0
                    status = ""
                    genre = ""
                    country = ""
                    favorite = false

                    coroutineScope.launch {
                        snackbarHostState.showSnackbar("Movie added successfully")
                    }
                }) {
                    Text("Save", modifier = Modifier.fillMaxWidth())
                }
            }
        }
    }
}