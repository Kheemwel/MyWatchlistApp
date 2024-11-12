package com.kheemwel.mywatchlist.ui.screens.home

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavController
import com.kheemwel.mywatchlist.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScaffold(
    navController: NavController,
    title: String,
    backHandler: @Composable () -> Unit = {},
    selectionMode: Boolean = false,
    onDeselectAll: () -> Unit = {},
    onInvertSelected: () -> Unit = {},
    onSelectAll: () -> Unit = {},
    onCancelSelection: () -> Unit = {},
    enableDelete: Boolean = false,
    onDelete: () -> Unit = {},
    onSearch: (String) -> Unit = {},
    isFilterActive: Boolean = false,
    onFilter: () -> Unit = {},
    onAddMovie: () -> Unit = {},
    onAddSeries: () -> Unit = {},
    content: @Composable (PaddingValues) -> Unit
) {
    var showSearch by remember { mutableStateOf(false) }
    var searchText by remember { mutableStateOf("") }
    val focusRequester = remember { FocusRequester() }
    var openAddMenu by remember { mutableStateOf(false) }

    BackHandler(enabled = showSearch && !selectionMode) {
        searchText = ""
        onSearch(searchText)
        showSearch = false
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            if (selectionMode) {
                TopAppBar(
                    navigationIcon = {
                        IconButton(onClick = onCancelSelection) {
                            Icon(Icons.Filled.Clear, contentDescription = "Clear")
                        }
                    },
                    title = { Text(title) },
                    actions = {
                        if (enableDelete) {
                            IconButton(onClick = onDelete) {
                                Icon(
                                    Icons.Filled.Delete,
                                    contentDescription = "Delete"
                                )
                            }
                        }

                        IconButton(onClick = onDeselectAll) {
                            Icon(
                                painterResource(R.drawable.baseline_deselect_24),
                                contentDescription = "Deselect All"
                            )
                        }
                        IconButton(onClick = onInvertSelected) {
                            Icon(
                                painterResource(R.drawable.baseline_flip_to_back_24),
                                contentDescription = "Inverse Selected"
                            )
                        }
                        IconButton(onClick = onSelectAll) {
                            Icon(
                                painterResource(R.drawable.baseline_select_all_24),
                                contentDescription = "Select All"
                            )
                        }
                    }
                )
            } else {
                TopAppBar(
                    navigationIcon = {
                        if (showSearch) {
                            IconButton(onClick = {
                                searchText = ""
                                onSearch(searchText)
                                showSearch = false
                            }) {
                                Icon(Icons.Filled.ArrowBack, contentDescription = "Close Search")
                            }
                        }
                    },
                    title = {
                        if (showSearch) {
                            OutlinedTextField(
                                modifier = Modifier.focusRequester(focusRequester),
                                colors = OutlinedTextFieldDefaults.colors(
                                    unfocusedBorderColor = Color.Transparent,
                                    focusedBorderColor = Color.Transparent,
                                ),
                                singleLine = true,
                                value = searchText,
                                placeholder = { Text("Search...") },
                                onValueChange = {
                                    searchText = it
                                    onSearch(it)
                                })

                            LaunchedEffect(Unit) {
                                focusRequester.requestFocus()
                            }
                        } else {
                            Text(title)
                        }
                    },
                    actions = {
                        if (!showSearch) {
                            IconButton(onClick = { showSearch = true }) {
                                Icon(Icons.Filled.Search, contentDescription = "Search")
                            }
                        }
                        if (showSearch && searchText.isNotEmpty()) {
                            IconButton(onClick = {
                                searchText = ""
                                onSearch(searchText)
                            }) {
                                Icon(Icons.Filled.Clear, contentDescription = "Clear")
                            }
                        }

                        IconButton(onClick = onFilter) {
                            Icon(
                                painterResource(R.drawable.baseline_filter_list_24),
                                contentDescription = "Filter",
                                tint = if (isFilterActive) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
                            )
                        }

                        IconButton(onClick = { navController.navigate("/settings") }) {
                            Icon(Icons.Filled.Settings, contentDescription = "Settings")
                        }
                    }
                )
            }
        },
        floatingActionButton = {
            Box {
                ExtendedFloatingActionButton(
                    text = { Text("Add") },
                    icon = { Icon(Icons.Filled.Add, contentDescription = "Add") },
                    onClick = { openAddMenu = !openAddMenu }
                )

                DropdownMenu(
                    expanded = openAddMenu,
                    onDismissRequest = { openAddMenu = false }
                ) {
                    DropdownMenuItem(
                        leadingIcon = {
                            Icon(
                                painterResource(R.drawable.baseline_movie_24),
                                contentDescription = "Movie"
                            )
                        },
                        text = { Text("Movie") },
                        onClick = {
                            openAddMenu = false
                            onAddMovie()
                        }
                    )
                    DropdownMenuItem(
                        leadingIcon = {
                            Icon(
                                painterResource(R.drawable.baseline_live_tv_24),
                                contentDescription = "Series"
                            )
                        },
                        text = { Text("Series") },
                        onClick = {
                            openAddMenu = false
                            onAddSeries()
                        }
                    )
                }
            }
        },
        content = content
    )
}