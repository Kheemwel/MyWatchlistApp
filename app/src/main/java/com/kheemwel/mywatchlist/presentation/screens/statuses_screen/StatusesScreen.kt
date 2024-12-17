package com.kheemwel.mywatchlist.presentation.screens.statuses_screen

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
fun StatusesScreen(navController: NavController, viewModel: StatusesScreenViewModel = hiltViewModel()) {
    val state = viewModel.state.value
    val statuses = state.statuses
    val selectedStatuses = state.selectedStatuses

    val appBarTitle = if (state.isSelectionMode) {
        "Selected ${selectedStatuses.size} ${if (selectedStatuses.size == 1) "item" else "items"}"
    } else {
        "Edit Statuses"
    }

    val formTitle = if (state.selectedId != null) {
        "Rename Status"
    } else {
        "Add Status"
    }

    BackHandler(enabled = state.isSelectionMode) {
        viewModel.onEvent(StatusesScreenEvent.ToggleSelectionMode(false))
    }

    TagScaffold(
        navController,
        title = appBarTitle,
        snackbarHost = { SnackbarHost(hostState = viewModel.snackbarHostState) },
        floatingActionButton = {
            AddFloatingButton {
                viewModel.onEvent(StatusesScreenEvent.OpenForm())
            }
        },
        selectionMode = state.isSelectionMode,
        onDeselectAll = {
            viewModel.onEvent(StatusesScreenEvent.DeselectAll)
        },
        onInvertSelected = {
            viewModel.onEvent(StatusesScreenEvent.InvertSelected)
        },
        onSelectAll = {
            viewModel.onEvent(StatusesScreenEvent.SelectAll)
        },
        enableDelete = state.isSelectionMode && selectedStatuses.isNotEmpty(),
        onDelete = {
            viewModel.onEvent(StatusesScreenEvent.OpenDeleteSelectedStatusesDialog)
        },
        onCancelSelection = {
            viewModel.onEvent(StatusesScreenEvent.ToggleSelectionMode(false))
        }
    ) { paddingValues ->
        TagForm(
            show = state.isFormOpen,
            title = formTitle,
            isError = state.error != null,
            errorMessage = state.error,
            value = state.inputName,
            onValueChange = { genre ->
                viewModel.onEvent(StatusesScreenEvent.EnterStatus(genre))
            },
            onCancel = {
                viewModel.onEvent(StatusesScreenEvent.CloseForm)
            }
        ) { input ->
            if (state.selectedId != null) {
                viewModel.onEvent(StatusesScreenEvent.UpdateStatus(state.selectedId, input))
            } else {
                viewModel.onEvent(StatusesScreenEvent.AddStatus(input))
            }

            if (state.error == null) {
                viewModel.onEvent(StatusesScreenEvent.CloseForm)
            }
        }

        ConfirmationDialog(
            state = state.isDeleteStatusDialogOpen,
            title = "Delete Status",
            message = "Do you want to delete status \"${state.inputName}\" ?",
            onCancel = {
                viewModel.onEvent(StatusesScreenEvent.CloseDeleteStatusDialog)
            }) {
            state.selectedId?.let {
                viewModel.onEvent(StatusesScreenEvent.DeleteStatus(it))
            }
            viewModel.onEvent(StatusesScreenEvent.CloseDeleteStatusDialog)
        }

        ConfirmationDialog(
            state = state.isDeleteSelectedStatusesDialogOpen,
            title = "Delete ${selectedStatuses.size} ${if (selectedStatuses.size == 1) "Status" else "Statuses"}",
            message = "Are you sure you want to delete this ${if (selectedStatuses.size == 1) "status" else "statuses"}?",
            onCancel = {
                viewModel.onEvent(StatusesScreenEvent.CloseDeleteSelectedStatusesDialog)
            }) {
            viewModel.onEvent(StatusesScreenEvent.DeleteSelectedStatuses)
            viewModel.onEvent(StatusesScreenEvent.CloseDeleteSelectedStatusesDialog)
            viewModel.onEvent(StatusesScreenEvent.ToggleSelectionMode(false))
        }

        LazyColumn(modifier = Modifier.padding(paddingValues)) {
            item {
                if (state.isLoading) {
                    LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
                }
            }
            items(statuses, key = { it.id }) { status ->
                TagTile(
                    modifier = Modifier.animateItem(),
                    text = status.name,
                    onRename = {
                        viewModel.onEvent(StatusesScreenEvent.OpenForm(status.id, status.name))
                    }, onDelete = {
                        viewModel.onEvent(StatusesScreenEvent.OpenDeleteStatusDialog(status.id, status.name))
                    },
                    selectionMode = state.isSelectionMode,
                    selected = selectedStatuses.contains(status.id),
                    onClick = {
                        if (state.isSelectionMode) {
                            viewModel.onEvent(StatusesScreenEvent.ToggleSelectedStatus(status.id))
                        }
                    },
                    onLongClick = {
                        viewModel.onEvent(StatusesScreenEvent.ToggleSelectionMode(true))
                        viewModel.onEvent(StatusesScreenEvent.ToggleSelectedStatus(status.id))
                    }
                )
            }
            item {
                Spacer(modifier = Modifier.height(150.dp))
            }
        }
    }
}