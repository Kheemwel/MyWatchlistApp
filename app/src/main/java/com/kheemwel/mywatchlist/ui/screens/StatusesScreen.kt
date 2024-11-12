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
import com.kheemwel.mywatchlist.data.models.StatusModel
import com.kheemwel.mywatchlist.ui.composables.ConfirmationDialog
import com.kheemwel.mywatchlist.ui.composables.TagForm
import com.kheemwel.mywatchlist.ui.composables.TagScaffold
import com.kheemwel.mywatchlist.ui.composables.TagTile

@Composable
fun StatusesScreen(navController: NavController, viewModel: StatusModel) {
    val statuses by viewModel.statuses.collectAsState()
    var openAddEditDialog by remember { mutableStateOf(false) }
    var openDeleteItemDialog by remember { mutableStateOf(false) }
    var isStatusExist by remember { mutableStateOf(false) }
    var isEditMode by remember { mutableStateOf(false) }
    var selectedStatusIndex by remember { mutableIntStateOf(-1) }
    val dialogTitle = if (isEditMode) {
        "Rename Status"
    } else {
        "Add Status"
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
        "Edit Statuses"
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
        onDeselectAll = {
            selectedItems.clear()
        },
        onInvertSelected = {
            val newList = statuses.indices.filterNot { it in selectedItems }
            selectedItems.apply {
                clear()
                addAll(newList)
            }
        },
        onSelectAll = {
            selectedItems.apply {
                clear()
                addAll(statuses.indices)
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
            initialValue = if (isEditMode) statuses[selectedStatusIndex] else "",
            isError = isStatusExist,
            errorMessage = "Status already exists",
            onTextChange = { genre ->
                isStatusExist = viewModel.isStatusExists(genre)
            },
            onCancel = {
                openAddEditDialog = false
                isStatusExist = false
                isEditMode = false
                selectedStatusIndex = -1
            }) { genre ->
            if (isEditMode) {
                viewModel.updateStatus(selectedStatusIndex, genre)
            } else {
                viewModel.addStatus(genre)
            }
            openAddEditDialog = false
            isStatusExist = false
            isEditMode = false
            selectedStatusIndex = -1
        }
        ConfirmationDialog(
            state = openDeleteItemDialog,
            title = "Delete Status",
            message = "Do you want to delete status \"${if (selectedStatusIndex >= 0) statuses[selectedStatusIndex] else ""}\" ?",
            onCancel = {
                openDeleteItemDialog = false
            }) {
            viewModel.deleteStatus(selectedStatusIndex)
            selectedStatusIndex = -1
            openDeleteItemDialog = false
        }
        ConfirmationDialog(
            state = openDeleteSelectedDialog,
            title = "Delete ${selectedItems.size} ${if (selectedItems.size == 1) "Status" else "Statuses"}",
            message = "Are you sure you want to delete this ${if (selectedItems.size == 1) "status" else "statuses"}?",
            onCancel = {
                openDeleteSelectedDialog = false
            }) {
            viewModel.deleteStatuses(selectedItems)
            selectedItems.clear()
            openDeleteSelectedDialog = false
            selectionMode = false
        }
        LazyColumn(modifier = Modifier.padding(paddingValues)) {
            itemsIndexed(statuses, key = { _, item -> item.hashCode() }) { index, item ->
                TagTile(item, onRename = {
                    selectedStatusIndex = index
                    isEditMode = true
                    openAddEditDialog = true
                }, onDelete = {
                    selectedStatusIndex = index
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