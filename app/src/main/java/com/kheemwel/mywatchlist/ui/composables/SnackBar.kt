package com.kheemwel.mywatchlist.ui.composables

import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult

suspend fun showSnackbar(snackbarHostState: SnackbarHostState, message: String): SnackbarResult {
    return snackbarHostState.showSnackbar(message = message, duration = SnackbarDuration.Short)
}