package com.kheemwel.mywatchlist.ui.composables

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.kheemwel.mywatchlist.core.LocalNavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyWatchlistScaffold(
    title: String,
    snackbarHost: @Composable () -> Unit = {},
    actions: @Composable (RowScope.() -> Unit) = {},
    content: @Composable (PaddingValues) -> Unit
) {
    Scaffold(
        modifier = Modifier.fillMaxSize(), topBar = {
            CenterAlignedTopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                ),
                title = { Text(title) },
                navigationIcon = {
                    with(LocalNavController.current) {
                        if (this.previousBackStackEntry != null) {
                            BackButton()
                        }
                    }
                },
                actions = actions
            )
        },
        snackbarHost = snackbarHost,
        content = content
    )
}