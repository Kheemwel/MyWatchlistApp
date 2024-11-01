package com.kheemwel.mywatchlist.ui.screens

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavController
import com.kheemwel.mywatchlist.R
import com.kheemwel.mywatchlist.ui.composables.AppScaffold

@Composable
fun SettingsScreen(navController: NavController) {
    AppScaffold(
        navController,
        title = "Settings",
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier.padding(innerPadding),
        ) {
            item {
                ListItem(
                    leadingContent = { Icon(painterResource(R.drawable.baseline_label_24), contentDescription = "Statuses")},
                    headlineContent = { Text("Statuses") })
            }
            item {
                ListItem(
                    leadingContent = { Icon(painterResource(R.drawable.baseline_label_24), contentDescription = "Genres")},
                    headlineContent = { Text("Genres") })
            }
            item {
                ListItem(
                    leadingContent = { Icon(painterResource(R.drawable.baseline_label_24), contentDescription = "Countries")},
                    headlineContent = { Text("Countries") })
            }
            item {
                ListItem(
                    leadingContent = { Icon(painterResource(R.drawable.baseline_save_24), contentDescription = "Create Backup")},
                    headlineContent = { Text("Create Backup") })
            }
            item {
                ListItem(
                    leadingContent = { Icon(painterResource(R.drawable.baseline_settings_backup_restore_24), contentDescription = "Restore Backup")},
                    headlineContent = { Text("Restore Backup") })
            }
        }
    }
}
