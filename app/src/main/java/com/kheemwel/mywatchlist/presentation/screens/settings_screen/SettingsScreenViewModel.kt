package com.kheemwel.mywatchlist.presentation.screens.settings_screen

import android.content.Context
import android.net.Uri
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kheemwel.mywatchlist.domain.model.AppData
import com.kheemwel.mywatchlist.domain.model.DataAction
import com.kheemwel.mywatchlist.domain.usecase.appdata_usecase.AppDataUseCases
import com.kheemwel.mywatchlist.presentation.composables.showSnackbar
import com.kheemwel.mywatchlist.utils.update
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import javax.inject.Inject

@HiltViewModel
class SettingsScreenViewModel @Inject constructor(
    private val appDataUseCases: AppDataUseCases
) : ViewModel() {
    private val _state = mutableStateOf(SettingsScreenState())
    val state: State<SettingsScreenState> = _state

    val snackbarHostState = SnackbarHostState()

    fun onEvent(event: SettingsScreenEvent) {
        when (event) {
            is SettingsScreenEvent.CreateBackup -> createBackup(event.context, event.uri)
            SettingsScreenEvent.HideRestoreBackupDialog -> hideRestoreBackupDialog()
            is SettingsScreenEvent.RestoreBackup -> restoreBackup(event.context, event.uri)
            is SettingsScreenEvent.SaveImportedBackup -> saveImportedBackup(event.action)
        }
    }

    private fun createBackup(context: Context, uri: Uri) {
        viewModelScope.launch {
            _state.update { copy(showCreateBackupProgressDialog = true) }

            val data: String = if (_state.value.exportData != null) {
                _state.value.exportData!!
            } else {
                val appData = Json.encodeToString(appDataUseCases.exportAppDataUseCase())
                _state.update { copy(exportData = appData) }
                appData
            }

            val success = saveTextToUri(context, uri, data)

            _state.update { copy(showCreateBackupProgressDialog = false) }

            val message = if (success) {
                "Backup created successfully"
            } else {
                "Backup creation failed"
            }
            showSnackbar(snackbarHostState, message)
        }
    }

    private fun hideRestoreBackupDialog() {
        _state.update { copy(showRestoreBackupDialog = false) }
    }

    private fun restoreBackup(context: Context, uri: Uri) {
        viewModelScope.launch {
            _state.update { copy(showReadingBackupProgressDialog = true) }

            val result = kotlin.runCatching {
                withContext(Dispatchers.IO) {
                    val data = readTextFromUri(context, uri)
                    Json.decodeFromString<AppData>(data)
                }
            }

            result.fold(
                onSuccess = { importedData ->
                    _state.update {
                        copy(importedData = importedData, showReadingBackupProgressDialog = false, showRestoreBackupDialog = true)
                    }
                },
                onFailure = {
                    _state.update { copy(showReadingBackupProgressDialog = false) }
                    showSnackbar(snackbarHostState, "Invalid data format")
                }
            )
        }
    }


    private fun saveImportedBackup(action: DataAction) {
        viewModelScope.launch {
            _state.update { copy(showRestoringProgressDialog = true) }
            var message = ""

            try {
                _state.value.importedData?.let {
                    appDataUseCases.importAppDataUseCase(it, action)
                }
                message = "Backup restored successfully"
            } catch (e: Exception) {
                message = "Backup restoration failed: ${e.localizedMessage}"
            } finally {
                _state.update { copy(showRestoringProgressDialog = false, exportData = null, importedData = null) }
            }

            showSnackbar(snackbarHostState, message)
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
                ""
            }
        }
    }
}