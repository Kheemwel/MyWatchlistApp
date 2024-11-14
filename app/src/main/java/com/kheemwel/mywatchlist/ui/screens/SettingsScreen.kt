package com.kheemwel.mywatchlist.ui.screens

import android.content.Context
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavController
import com.kheemwel.mywatchlist.R
import com.kheemwel.mywatchlist.data.models.AppData
import com.kheemwel.mywatchlist.data.models.AppDataModel
import com.kheemwel.mywatchlist.data.models.DataAction
import com.kheemwel.mywatchlist.ui.composables.NavigationScaffold
import com.kheemwel.mywatchlist.ui.composables.showSnackbar
import com.kheemwel.mywatchlist.utils.getCurrentDateTimeAsString
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json

@Composable
fun SettingsScreen(navController: NavController, appDataModel: AppDataModel) {
    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current
    val mimeType = "application/json"
    var showRestoreBackupDialog by remember { mutableStateOf(false) }
    var showBackupProgressDialog by remember { mutableStateOf(false) }
    var showImportProgressDialog by remember { mutableStateOf(false) }
    var progress by remember { mutableFloatStateOf(0f) }
    var importedAppData: AppData? = null
    var exportAppData = ""

    // Launcher for picking a file to import
    val pickFileLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
        onResult = { uri: Uri? ->
            uri?.let {
                showImportProgressDialog = true
                coroutineScope.launch {
                    val data = readTextFromUri(context, it)

                    try {
                        importedAppData = Json.decodeFromString<AppData>(data)
                        showImportProgressDialog = false
                    } catch (e: Exception) {
                        e.printStackTrace()
                        showImportProgressDialog = false
                        showSnackbar(snackbarHostState, "Invalid data format")
                    }

                    if (importedAppData != null) {
                        showRestoreBackupDialog = true
                    }
                }
            }
        }
    )

    // Launcher for saving a file
    val createFileLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.CreateDocument(mimeType),
        onResult = { uri: Uri? ->
            uri?.let {
                coroutineScope.launch {
                    // val exportText = appDataModel.getAppDataJson()
                    val success = saveTextToUri(context, it, exportAppData)
                    if (success) {
                        showSnackbar(snackbarHostState, "Backup created successfully")
                    } else {
                        showSnackbar(snackbarHostState, "Backup creation failed")
                    }
                }
            }
        }
    )

    NavigationScaffold(
        navController,
        title = "Settings",
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        },
    ) { innerPadding ->
        RestoreBackupDialog(
            state = showRestoreBackupDialog,
            onDismiss = { showRestoreBackupDialog = false },
            onCancel = { showRestoreBackupDialog = false },
            onConfirm = { action ->
                showRestoreBackupDialog = false
                coroutineScope.launch {
                    importedAppData?.let { data ->
                        appDataModel.saveAppData(data, action)
                    }
                    showSnackbar(snackbarHostState, "Backup restored successfully")
                }

            }
        )

        ProgressDialog(
            state = showBackupProgressDialog,
            title = "Creating backup",
            progress = progress
        )

        ProgressDialog(
            state = showImportProgressDialog,
            title = "Importing Backup",
        )

        LazyColumn(
            modifier = Modifier.padding(innerPadding),
        ) {
            item {
                ListItem(
                    modifier = Modifier.clickable { navController.navigate("/settings/statuses") },
                    leadingContent = {
                        Icon(
                            painterResource(R.drawable.baseline_label_24),
                            contentDescription = "Statuses"
                        )
                    },
                    headlineContent = { Text("Statuses") })
            }
            item {
                ListItem(
                    modifier = Modifier.clickable { navController.navigate("/settings/genres") },
                    leadingContent = {
                        Icon(
                            painterResource(R.drawable.baseline_label_24),
                            contentDescription = "Genres"
                        )
                    },
                    headlineContent = { Text("Genres") })
            }
            item {
                ListItem(
                    modifier = Modifier.clickable { navController.navigate("/settings/countries") },
                    leadingContent = {
                        Icon(
                            painterResource(R.drawable.baseline_label_24),
                            contentDescription = "Countries"
                        )
                    },
                    headlineContent = { Text("Countries") })
            }
            item {
                ListItem(
                    modifier = Modifier.clickable {
                        showBackupProgressDialog = true
                        coroutineScope.launch {
                            val fileName = "my_watchlist_backup_${getCurrentDateTimeAsString()}.json"
                            exportAppData = appDataModel.encodeAppDataToJsonWithProgress {
                                progress = it
                            }
                            showBackupProgressDialog = false
                            createFileLauncher.launch(fileName)
                        }
                    },
                    leadingContent = {
                        Icon(
                            painterResource(R.drawable.baseline_save_24),
                            contentDescription = "Create Backup"
                        )
                    },
                    headlineContent = { Text("Create Backup") })
            }
            item {
                ListItem(
                    modifier = Modifier.clickable {
                        pickFileLauncher.launch(mimeType)
                    },
                    leadingContent = {
                        Icon(
                            painterResource(R.drawable.baseline_settings_backup_restore_24),
                            contentDescription = "Restore Backup"
                        )
                    },
                    headlineContent = { Text("Restore Backup") })
            }
        }
    }
}

private suspend fun saveTextToUri(context: Context, uri: Uri, text: String): Boolean {
    return withContext(Dispatchers.IO) {
        try {
            context.contentResolver.openOutputStream(uri)?.use { outputStream ->
                outputStream.write(text.toByteArray())
            }
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }
}

private suspend fun readTextFromUri(context: Context, uri: Uri): String {
    return withContext(Dispatchers.IO) {
        try {
            context.contentResolver.openInputStream(uri)?.use { inputStream ->
                inputStream.bufferedReader().use { it.readText() }
            } ?: ""
        } catch (e: Exception) {
            e.printStackTrace()
            ""
        }
    }
}

@Composable
private fun RestoreBackupDialog(
    state: Boolean,
    onDismiss: () -> Unit = {},
    onCancel: () -> Unit = {},
    onConfirm: (DataAction) -> Unit,
) {
    var selectedAction by remember { mutableStateOf(DataAction.Merge) }
    val actions = listOf(
        Triple(DataAction.Overwrite, "Overwrite", "Replace existing data with the backup data. All current data will be lost."),
        Triple(
            DataAction.Merge,
            "Merge",
            "Combine backup data with existing data. Duplicate entries will be handled automatically."
        ),
        Triple(DataAction.Append, "Append", "Add backup data to existing data without modifying existing entries.")
    )
    if (state) {
        AlertDialog(
            icon = { Icon(painterResource(R.drawable.baseline_settings_backup_restore_24), "Restore Backup") },
            title = { Text("Restore Backup") },
            text = {
                Column {
                    Text("Choose how you want to restore the backup:")
                    Divider(
                        modifier = Modifier
                            .padding(vertical = 8.dp)
                            .height(1.dp)
                    )
                    Column {
                        actions.forEach { (action, title, description) ->
                            ListItem(
                                modifier = Modifier.clickable {
                                    selectedAction = action
                                },
                                leadingContent = {
                                    RadioButton(selected = selectedAction == action, onClick = null)
                                },
                                headlineContent = { Text(title) },
                                supportingContent = { Text(description) }
                            )
                        }
                    }
                }
            },
            onDismissRequest = onDismiss,
            confirmButton = {
                TextButton(onClick = { onConfirm(selectedAction) }) {
                    Text("Proceed")
                }
            },
            dismissButton = {
                TextButton(onClick = onCancel) {
                    Text("Cancel")
                }
            }
        )
    }
}

@Composable
private fun ProgressDialog(
    state: Boolean,
    title: String,
    progress: Float? = null,
) {
    if (state) {
        Dialog(onDismissRequest = {}) {
            Card(
                modifier = Modifier
                    .wrapContentSize()
                    .fillMaxWidth(),
                shape = RoundedCornerShape(28.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceColorAtElevation(6.dp),
                    contentColor = MaterialTheme.colorScheme.onSurface
                )
            ) {
                Column(modifier = Modifier.padding(24.dp)) {
                    Row(verticalAlignment = Alignment.Bottom) {
                        Text(
                            text = title,
                            modifier = Modifier.weight(1f),
                            style = MaterialTheme.typography.headlineSmall,
                            textAlign = if (progress == null) TextAlign.Center else null
                        )
                        if (progress != null) {
                            Text("%.2f%%".format(progress), style = MaterialTheme.typography.bodyMedium)
                        }
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    if (progress != null) {
                        LinearProgressIndicator(progress = progress / 100, modifier = Modifier.fillMaxWidth())
                    } else {
                        LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
                    }
                }
            }
        }
    }
}