package com.kheemwel.mywatchlist.presentation.composables

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Star
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
import androidx.compose.ui.text.style.TextOverflow
import com.kheemwel.mywatchlist.R

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun WatchTile(
    text: String,
    status: String,
    isFavorite: Boolean,
    onFavorite: () -> Unit,
    transferOptionText: String,
    onTransfer: () -> Unit,
    onEdit: () -> Unit,
    onDelete: () -> Unit,
    selectionMode: Boolean = false,
    selected: Boolean = false,
    onClick: () -> Unit = {},
    onLongClick: () -> Unit = {},
) {
    var openMenu by remember { mutableStateOf(false) }

    ListItem(
        modifier = Modifier.combinedClickable(onClick = onClick, onLongClick = onLongClick),
        leadingContent = {
            if (isFavorite) {
                Icon(
                    Icons.Filled.Star,
                    contentDescription = "Favorite",
                )
            } else {
                Icon(
                    painterResource(R.drawable.baseline_star_border_24),
                    contentDescription = "Not Favorite"
                )
            }
        },
        headlineContent = { Text(text, maxLines = 2, overflow = TextOverflow.Ellipsis) },
        supportingContent = { Text(status) },
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
                    DropdownMenuItem(
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Filled.Star,
                                contentDescription = "Favorite"
                            )
                        },
                        text = { Text("Toggle Favorite") },
                        onClick = {
                            openMenu = false
                            onFavorite()
                        })
                    DropdownMenuItem(leadingIcon = {
                        Icon(painterResource(R.drawable.baseline_swap_horiz_24), contentDescription = "Transfer")
                    },
                        text = { Text(transferOptionText) },
                        onClick = {
                            openMenu = false
                            onTransfer()
                        })
                    DropdownMenuItem(leadingIcon = {
                        Icon(
                            Icons.Filled.Edit,
                            contentDescription = "Edit"
                        )
                    }, text = { Text("Edit") }, onClick = {
                        openMenu = false
                        onEdit()
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