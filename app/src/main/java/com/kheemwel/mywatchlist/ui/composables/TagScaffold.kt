package com.kheemwel.mywatchlist.ui.composables

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
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
    floatingActionButton: @Composable () -> Unit = {},
    selectionMode: Boolean = false,
    onSelectAll: (Boolean) -> Unit = {},
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

                CenterAlignedTopAppBar(
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

                        IconButton(onClick = {
                            selectAll = !selectAll
                            onSelectAll(selectAll)
                        }) {
                            Icon(
                                painterResource(R.drawable.baseline_checklist_rtl_24),
                                contentDescription = "Select All",
                                tint = if (selectAll) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
                            )
                        }
                    }
                )
            } else {
                CenterAlignedTopAppBar(
                    title = { Text(title) },
                    navigationIcon = { BackButton(navController) },
                )
            }
        },
        floatingActionButton = floatingActionButton,
        content = content
    )
}