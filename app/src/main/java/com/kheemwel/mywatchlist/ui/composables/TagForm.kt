package com.kheemwel.mywatchlist.ui.composables

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue
import kotlinx.coroutines.delay

@Composable
fun TagForm(
    show: Boolean = false,
    title: String,
    initialValue: String = "",
    isError: Boolean = false,
    errorMessage: String,
    onTextChange: (text: String) -> Unit,
    cancelText: String = "Cancel",
    onCancel: () -> Unit = {},
    confirmText: String = "Confirm",
    onConfirm: (text: String) -> Unit
) {
    var text by remember(initialValue) {
        mutableStateOf(
            TextFieldValue(
                initialValue.trim(),
                TextRange(initialValue.trim().length)
            )
        )
    }
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
                    value = text,
                    onValueChange = {
                        text = it
                        onTextChange(it.text.trim())
                    },
                    label = { Text("Name") },
                    supportingText = { if (isError) Text(errorMessage) },
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
                    enabled = text.text.trim()
                        .isNotEmpty() && !isError && (initialValue.trim() != text.text.trim()),
                    onClick = {
                        onConfirm(text.text.trim())
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