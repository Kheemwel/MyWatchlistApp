package com.kheemwel.mywatchlist.presentation.screens.settings_screen

import com.kheemwel.mywatchlist.domain.model.AppData

data class SettingsScreenState(
    val exportData: String? = null,
    val importedData: AppData? = null,
    val showCreateBackupProgressDialog: Boolean = false,
    val showReadingBackupProgressDialog: Boolean = false,
    val showRestoreBackupDialog: Boolean = false,
    val showRestoringProgressDialog: Boolean = false,
)
