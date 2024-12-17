package com.kheemwel.mywatchlist.presentation.composables

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavController
import com.kheemwel.mywatchlist.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TagScaffold(
    navController: NavController,
    title: String,
    snackbarHost: @Composable () -> Unit = {},
    floatingActionButton: @Composable () -> Unit = {},
    selectionMode: Boolean = false,
    onDeselectAll: () -> Unit = {},
    onInvertSelected: () -> Unit = {},
    onSelectAll: () -> Unit = {},
    enableDelete: Boolean = false,
    onDelete: () -> Unit = {},
    onCancelSelection: () -> Unit = {},
    content: @Composable (PaddingValues) -> Unit,
) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            if (selectionMode) {
                var selectAll by remember { mutableStateOf(false) }

                TopAppBar(
                    title = { Text(title) },
                    navigationIcon = {
                        IconButton(onClick = {
                            selectAll = false
                            onCancelSelection()
                        }) {
                            Icon(
                                Icons.Filled.Close,
                                contentDescription = "Cancel",
                            )
                        }
                    },
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
                    title = { Text(title) },
                    navigationIcon = { BackButton(navController) },
                )
            }
        },
        snackbarHost = snackbarHost,
        floatingActionButton = floatingActionButton,
        content = content
    )
}