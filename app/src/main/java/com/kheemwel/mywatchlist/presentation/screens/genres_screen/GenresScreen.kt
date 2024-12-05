package com.kheemwel.mywatchlist.presentation.screens.genres_screen

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
fun GenresScreen(navController: NavController, viewModel: GenresScreenViewModel = hiltViewModel()) {
    val state = viewModel.state.value
    val genres = state.genres
    val selectedGenres = state.selectedGenres

    val appBarTitle = if (state.isSelectionMode) {
        "Selected ${selectedGenres.size} ${if (selectedGenres.size == 1) "item" else "items"}"
    } else {
        "Edit Genres"
    }

    val formTitle = if (state.selectedId != null) {
        "Rename Genre"
    } else {
        "Add Genre"
    }

    BackHandler(enabled = state.isSelectionMode) {
        viewModel.onEvent(GenresScreenEvent.ToggleSelectionMode(false))
    }

    TagScaffold(
        navController,
        title = appBarTitle,
        snackbarHost = { SnackbarHost(hostState = viewModel.snackbarHostState) },
        floatingActionButton = {
            AddFloatingButton {
                viewModel.onEvent(GenresScreenEvent.OpenForm())
            }
        },
        selectionMode = state.isSelectionMode,
        onDeselectAll = {
            viewModel.onEvent(GenresScreenEvent.DeselectAll)
        },
        onInvertSelected = {
            viewModel.onEvent(GenresScreenEvent.InvertSelected)
        },
        onSelectAll = {
            viewModel.onEvent(GenresScreenEvent.SelectAll)
        },
        enableDelete = state.isSelectionMode && selectedGenres.isNotEmpty(),
        onDelete = {
            viewModel.onEvent(GenresScreenEvent.OpenDeleteSelectedGenresDialog)
        },
        onCancelSelection = {
            viewModel.onEvent(GenresScreenEvent.ToggleSelectionMode(false))
        }
    ) { paddingValues ->
        TagForm(
            show = state.isFormOpen,
            title = formTitle,
            isError = state.error != null,
            errorMessage = state.error,
            value = state.inputName,
            onValueChange = { genre ->
                viewModel.onEvent(GenresScreenEvent.EnterGenre(genre))
            },
            onCancel = {
                viewModel.onEvent(GenresScreenEvent.CloseForm)
            }
        ) { input ->
            if (state.selectedId != null) {
                viewModel.onEvent(GenresScreenEvent.UpdateGenre(state.selectedId, input))
            } else {
                viewModel.onEvent(GenresScreenEvent.AddGenre(input))
            }

            if (state.error == null) {
                viewModel.onEvent(GenresScreenEvent.CloseForm)
            }
        }

        ConfirmationDialog(
            state = state.isDeleteGenreDialogOpen,
            title = "Delete Genre",
            message = "Do you want to delete genre \"${state.inputName}\" ?",
            onCancel = {
                viewModel.onEvent(GenresScreenEvent.CloseDeleteGenreDialog)
            }) {
            state.selectedId?.let {
                viewModel.onEvent(GenresScreenEvent.DeleteGenre(it))
            }
            viewModel.onEvent(GenresScreenEvent.CloseDeleteGenreDialog)
        }

        ConfirmationDialog(
            state = state.isDeleteSelectedGenresDialogOpen,
            title = "Delete ${selectedGenres.size} ${if (selectedGenres.size == 1) "Genre" else "Genres"}",
            message = "Are you sure you want to delete this ${if (selectedGenres.size == 1) "genre" else "genres"}?",
            onCancel = {
                viewModel.onEvent(GenresScreenEvent.CloseDeleteSelectedGenresDialog)
            }) {
            viewModel.onEvent(GenresScreenEvent.DeleteSelectedGenres)
            viewModel.onEvent(GenresScreenEvent.CloseDeleteSelectedGenresDialog)
            viewModel.onEvent(GenresScreenEvent.ToggleSelectionMode(false))
        }

        LazyColumn(modifier = Modifier.padding(paddingValues)) {
            item {
                if (state.isLoading) {
                    LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
                }
            }
            items(genres, key = { it.id }) { genre ->
                TagTile(
                    modifier = Modifier.animateItem(),
                    text = genre.name,
                    onRename = {
                        viewModel.onEvent(GenresScreenEvent.OpenForm(genre.id, genre.name))
                    }, onDelete = {
                        viewModel.onEvent(GenresScreenEvent.OpenDeleteGenreDialog(genre.id, genre.name))
                    },
                    selectionMode = state.isSelectionMode,
                    selected = selectedGenres.contains(genre.id),
                    onClick = {
                        if (state.isSelectionMode) {
                            viewModel.onEvent(GenresScreenEvent.ToggleSelectedGenre(genre.id))
                        }
                    },
                    onLongClick = {
                        viewModel.onEvent(GenresScreenEvent.ToggleSelectionMode(true))
                        viewModel.onEvent(GenresScreenEvent.ToggleSelectedGenre(genre.id))
                    }
                )
            }
            item {
                Spacer(modifier = Modifier.height(150.dp))
            }
        }
    }
}