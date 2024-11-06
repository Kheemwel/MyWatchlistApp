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
import com.kheemwel.mywatchlist.data.models.GenreModel
import com.kheemwel.mywatchlist.ui.composables.ConfirmationDialog
import com.kheemwel.mywatchlist.ui.composables.TagForm
import com.kheemwel.mywatchlist.ui.composables.TagScaffold
import com.kheemwel.mywatchlist.ui.composables.TagTile

@Composable
fun GenresScreen(navController: NavController, viewModel: GenreModel) {
    val genres by viewModel.genres.collectAsState()
    var openAddEditDialog by remember { mutableStateOf(false) }
    var openDeleteItemDialog by remember { mutableStateOf(false) }
    var isGenreExist by remember { mutableStateOf(false) }
    var isEditMode by remember { mutableStateOf(false) }
    var selectedGenreIndex by remember { mutableIntStateOf(-1) }
    val dialogTitle = if (isEditMode) {
        "Rename Genre"
    } else {
        "Add Genre"
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
        "Edit Genres"
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
                selectedItems.addAll(genres.indices)
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
            initialValue = if (isEditMode) genres[selectedGenreIndex] else "",
            isError = isGenreExist,
            errorMessage = "Genre already exists",
            onTextChange = { genre ->
                isGenreExist = viewModel.isGenreExists(genre)
            },
            onCancel = {
                isGenreExist = false
                openAddEditDialog = false
            }) { genre ->
            if (isEditMode) {
                viewModel.updateGenre(selectedGenreIndex, genre)
            } else {
                viewModel.addGenre(genre)
            }
            openAddEditDialog = false
            isGenreExist = false
            isEditMode = false
            selectedGenreIndex = -1
        }
        ConfirmationDialog(
            state = openDeleteItemDialog,
            title = "Delete Genre",
            message = "Do you want to delete genre \"${if (selectedGenreIndex >= 0) genres[selectedGenreIndex] else ""}\" ?",
            onCancel = {
                openDeleteItemDialog = false
            }) {
            viewModel.deleteGenre(selectedGenreIndex)
            selectedGenreIndex = -1
            openDeleteItemDialog = false
        }
        ConfirmationDialog(
            state = openDeleteSelectedDialog,
            title = "Delete ${selectedItems.size} ${if (selectedItems.size == 1) "Genre" else "Genres"}",
            message = "Are you sure you want to delete this ${if (selectedItems.size == 1) "genre" else "genres"}?",
            onCancel = {
                openDeleteSelectedDialog = false
            }) {
            viewModel.deleteGenres(selectedItems)
            selectedItems.clear()
            openDeleteSelectedDialog = false
            selectionMode = false
        }
        LazyColumn(modifier = Modifier.padding(paddingValues)) {
            itemsIndexed(genres, key = { _, item -> item.hashCode() }) { index, item ->
                TagTile(item, onRename = {
                    selectedGenreIndex = index
                    isEditMode = true
                    openAddEditDialog = true
                }, onDelete = {
                    selectedGenreIndex = index
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