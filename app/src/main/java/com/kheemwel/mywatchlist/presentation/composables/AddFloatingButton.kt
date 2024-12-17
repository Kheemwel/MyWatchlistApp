package com.kheemwel.mywatchlist.presentation.composables

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

@Composable
fun AddFloatingButton(onClick: () -> Unit) {
    ExtendedFloatingActionButton(
        icon = { Icon(Icons.Filled.Add, "Add") },
        text = { Text(text = "Add") },
        onClick = onClick,
    )
}