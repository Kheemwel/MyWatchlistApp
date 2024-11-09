package com.kheemwel.mywatchlist.ui.composables

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable

@Composable
fun ConfirmationDialog(
    state: Boolean,
    title: String,
    message: String,
    onDismiss: () -> Unit = {},
    onCancelText: String = "Cancel",
    onCancel: () -> Unit,
    onConfirmText: String = "OK",
    onConfirm: () -> Unit
) {
    if (state) {
        AlertDialog(
            title = {
                Text(title)
            },
            text = { Text(message) },
            onDismissRequest = onDismiss,
            confirmButton = {
                TextButton(
                    onClick = onConfirm
                ) {
                    Text(onConfirmText)
                }
            },
            dismissButton = {
                TextButton(
                    onClick = onCancel
                ) {
                    Text(onCancelText)
                }
            }
        )
    }
}