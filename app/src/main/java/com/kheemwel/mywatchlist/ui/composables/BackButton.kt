package com.kheemwel.mywatchlist.ui.composables

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import com.kheemwel.mywatchlist.core.LocalNavController

/**
 * Button for navigating back
 */
@Composable
fun BackButton() {
    val navController = LocalNavController.current
    IconButton(onClick = { navController.popBackStack() }) {
        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
    }
}