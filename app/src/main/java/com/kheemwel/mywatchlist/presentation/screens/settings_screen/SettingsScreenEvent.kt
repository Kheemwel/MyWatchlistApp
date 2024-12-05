package com.kheemwel.mywatchlist.presentation.screens.settings_screen

import android.content.Context
import android.net.Uri
import com.kheemwel.mywatchlist.domain.model.DataAction

sealed class SettingsScreenEvent {
    data class CreateBackup(val context: Context, val uri: Uri) : SettingsScreenEvent()
    data class RestoreBackup(val context: Context, val uri: Uri) : SettingsScreenEvent()
    data object HideRestoreBackupDialog : SettingsScreenEvent()
    data class SaveImportedBackup(val action: DataAction) : SettingsScreenEvent()
}