package com.kheemwel.mywatchlist.presentation.composables

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.navigation.NavController

/**
 * Button for navigating back
 */
@Composable
fun BackButton(navController: NavController) {
    IconButton(onClick = { if (navController.previousBackStackEntry != null) navController.popBackStack() }) {
        Icon(Icons.Filled.ArrowBack, contentDescription = "Back")
    }
}