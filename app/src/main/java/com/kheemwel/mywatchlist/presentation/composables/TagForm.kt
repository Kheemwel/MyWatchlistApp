package com.kheemwel.mywatchlist.presentation.composables

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import kotlinx.coroutines.delay

@Composable
fun TagForm(
    show: Boolean = false,
    title: String,
    isError: Boolean = false,
    errorMessage: String? = null,
    value: String,
    onValueChange: (text: String) -> Unit,
    cancelText: String = "Cancel",
    onCancel: () -> Unit = {},
    confirmText: String = "Save",
    onConfirm: (text: String) -> Unit
) {
    val focusRequester = remember { FocusRequester() }

    if (show) {
        AlertDialog(
            title = {
                Text(title)
            },
            text = {
                OutlinedTextField(
                    modifier = Modifier.focusRequester(focusRequester),
                    isError = isError,
                    value = value,
                    onValueChange = {
                        onValueChange(it)
                    },
                    label = { Text("Name") },
                    supportingText = { if (isError) errorMessage?.let { Text(it) } },
                )

                LaunchedEffect(Unit) {
                    delay(300)
                    focusRequester.requestFocus()
                }
            },
            onDismissRequest = {
                onCancel()
            },
            confirmButton = {
                TextButton(
                    enabled = value.trim().isNotEmpty() && !isError,
                    onClick = {
                        onConfirm(value.trim())
                    }
                ) {
                    Text(confirmText)
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        onCancel()
                    }
                ) {
                    Text(cancelText)
                }
            }
        )
    }
}