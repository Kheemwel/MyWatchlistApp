package com.kheemwel.mywatchlist.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.kheemwel.mywatchlist.data.AppData
import com.kheemwel.mywatchlist.ui.composables.MyWatchlistScaffold
import com.kheemwel.mywatchlist.ui.composables.TextCapsule
import kotlinx.coroutines.launch

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun SettingsScreen() {
    val coroutineScope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    MyWatchlistScaffold(
        title = "Settings",
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier.padding(innerPadding),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("Statuses")
                    AddItemDialog(title = "Add Status") {
                        AppData.statusList.add(it)
                        coroutineScope.launch {
                            snackbarHostState.showSnackbar("Status added successfully")
                        }
                    }
                }
                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    AppData.statusList.forEach {
                        EditableItem(value = it, onDelete = {
                            AppData.statusList.remove(it)
                            coroutineScope.launch {
                                snackbarHostState.showSnackbar("Status deleted successfully")
                            }
                        }, onSave = { old, new ->
                            AppData.statusList[AppData.statusList.indexOf(old)] = new
                            coroutineScope.launch {
                                snackbarHostState.showSnackbar("Status updated successfully")
                            }
                        })
                    }
                }
            }
            item {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("Countries")
                    AddItemDialog(title = "Add Country") {
                        AppData.countryList.add(it)
                        coroutineScope.launch {
                            snackbarHostState.showSnackbar("Country added successfully")
                        }
                    }
                }
                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    AppData.countryList.forEach {
                        EditableItem(value = it, onDelete = {
                            AppData.countryList.remove(it)
                            coroutineScope.launch {
                                snackbarHostState.showSnackbar("Country deleted successfully")
                            }
                        }, onSave = { old, new ->
                            AppData.countryList[AppData.countryList.indexOf(old)] = new
                            coroutineScope.launch {
                                snackbarHostState.showSnackbar("Country updated successfully")
                            }
                        })
                    }
                }
            }
            item {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("Genres")
                    AddItemDialog(title = "Add Genre") {
                        AppData.genreList.add(it)
                        coroutineScope.launch {
                            snackbarHostState.showSnackbar("Genre added successfully")
                        }
                    }
                }
                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    AppData.genreList.forEach {
                        EditableItem(value = it, onDelete = {
                            AppData.genreList.remove(it)
                            coroutineScope.launch {
                                snackbarHostState.showSnackbar("Genre deleted successfully")
                            }
                        }, onSave = { old, new ->
                            AppData.genreList[AppData.genreList.indexOf(old)] = new
                            coroutineScope.launch {
                                snackbarHostState.showSnackbar("Genre updated successfully")
                            }
                        })
                    }
                }
            }
        }
    }
}

@Composable
fun AddItemDialog(title: String, onSave: (String) -> Unit) {
    var openDialog by remember { mutableStateOf(false) }
    var item by remember { mutableStateOf("") }

    Button(onClick = { openDialog = true }) {
        Icon(Icons.Filled.Add, contentDescription = "Add")
    }

    if (openDialog) {
        Dialog(onDismissRequest = {
            item = ""
            openDialog = false
        }) {
            Card(
                shape = RoundedCornerShape(16.dp),
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = title,
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center,
                    )
                    OutlinedTextField(value = item, onValueChange = { item = it })
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
                        Button(onClick = {
                            item = ""
                            openDialog = false
                        }) {
                            Text("Cancel")
                        }
                        Button(onClick = {
                            onSave(item)
                            item = ""
                            openDialog = false
                        }) {
                            Text("Save")
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun EditableItem(value: String, onDelete: () -> Unit, onSave: (old: String, new: String) -> Unit) {
    var openDialog by remember { mutableStateOf(false) }
    var text by remember { mutableStateOf(value) }

    TextCapsule(value) {
        openDialog = true
    }

    if (openDialog) {
        Dialog(onDismissRequest = { openDialog = false }) {
            Card(
                shape = RoundedCornerShape(16.dp),
            ) {
                Column(modifier = Modifier.padding(16.dp).fillMaxWidth()) {
                    Text(
                        text = "Edit Item",
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center,
                    )
                    
                    Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween) {
                        OutlinedTextField(value = text, onValueChange = { text = it }, modifier = Modifier.width(250.dp))
                        IconButton(
                            onClick = {
                                onDelete()
                                openDialog = false
                            },
                            modifier = Modifier.size(36.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Filled.Delete,
                                contentDescription = "Delete",
                            )
                        }
                    }
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
                        Button(onClick = { openDialog = false }) {
                            Text("Cancel")
                        }
                        Button(onClick = {
                            onSave(value, text)
                            openDialog = false
                        }) {
                            Text("Save")
                        }
                    }
                }
            }
        }
    }
}