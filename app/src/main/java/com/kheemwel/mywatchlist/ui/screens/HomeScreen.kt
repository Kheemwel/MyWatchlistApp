package com.kheemwel.mywatchlist.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.kheemwel.mywatchlist.ui.composables.HomeScaffold

@Composable
fun HomeScreen(navController: NavController) {
    HomeScaffold("Home", actions = {
        IconButton(onClick = { navController.navigate("/settings") }) {
            Icon(Icons.Filled.Settings, contentDescription = "Settings")
        }
    }) { innerPadding ->
        var tabIndex by rememberSaveable { mutableIntStateOf(0) }

        val tabs = listOf("Movies", "Series")

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            TabRow(selectedTabIndex = tabIndex,
                indicator = {
                    Box(
                        modifier = Modifier
                            .tabIndicatorOffset(it[tabIndex])
                            .height(3.dp)
                            .padding(horizontal = 64.dp)
                            .background(MaterialTheme.colorScheme.primary, RoundedCornerShape(8.dp))
                    )
                }
            ) {
                tabs.forEachIndexed { index, title ->
                    Tab(
                        text = { Text(title) },
                        selected = tabIndex == index,
                        onClick = { tabIndex = index },
                        selectedContentColor = MaterialTheme.colorScheme.primary,
                        unselectedContentColor = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
            when (tabIndex) {
                0 -> MovieTab()
                1 -> SeriesTab()
            }
        }
    }
}

@Composable
private fun MovieTab() {
    Box {

    }
}

@Composable
private fun SeriesTab() {
    Box {

    }
}
