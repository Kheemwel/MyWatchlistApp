package com.kheemwel.mywatchlist.presentation.composables

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Checkbox
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import com.kheemwel.mywatchlist.R

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun TagTile(
    modifier: Modifier = Modifier,
    text: String,
    onRename: () -> Unit,
    onDelete: () -> Unit,
    selectionMode: Boolean = false,
    selected: Boolean = false,
    onClick: () -> Unit = {},
    onLongClick: () -> Unit = {},
) {
    var openMenu by remember { mutableStateOf(false) }

    ListItem(
        modifier = modifier.combinedClickable(onClick = onClick, onLongClick = onLongClick),
        leadingContent = {
            Icon(
                painterResource(R.drawable.outline_label_24),
                contentDescription = "Genre"
            )
        },
        headlineContent = { Text(text) },
        trailingContent = {
            if (selectionMode) {
                Checkbox(
                    checked = selected,
                    onCheckedChange = {
                        onClick()
                    }
                )
            } else {
                IconButton(onClick = { openMenu = true }) {
                    Icon(
                        Icons.Filled.MoreVert,
                        contentDescription = "Localized description"
                    )
                }
                DropdownMenu(
                    expanded = openMenu,
                    onDismissRequest = { openMenu = false }
                ) {
                    DropdownMenuItem(leadingIcon = {
                        Icon(
                            Icons.Filled.Edit,
                            contentDescription = "Edit"
                        )
                    }, text = { Text("Rename") }, onClick = {
                        openMenu = false
                        onRename()
                    })
                    DropdownMenuItem(leadingIcon = {
                        Icon(
                            Icons.Filled.Delete,
                            contentDescription = "Delete"
                        )
                    }, text = { Text("Delete") }, onClick = {
                        openMenu = false
                        onDelete()
                    })
                }
            }
        }
    )
}