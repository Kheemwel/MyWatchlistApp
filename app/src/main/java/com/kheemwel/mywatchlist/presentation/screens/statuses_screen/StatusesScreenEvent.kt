package com.kheemwel.mywatchlist.presentation.screens.statuses_screen

sealed class StatusesScreenEvent {
    data class OpenForm(val id: Long? = null, val name: String = "") : StatusesScreenEvent()
    data object CloseForm : StatusesScreenEvent()
    data class OpenDeleteStatusDialog(val id: Long, val name: String) : StatusesScreenEvent()
    data object CloseDeleteStatusDialog : StatusesScreenEvent()
    data object OpenDeleteSelectedStatusesDialog : StatusesScreenEvent()
    data object CloseDeleteSelectedStatusesDialog : StatusesScreenEvent()
    data class ToggleSelectionMode(val value: Boolean) : StatusesScreenEvent()
    data class ToggleSelectedStatus(val id: Long) : StatusesScreenEvent()
    data object SelectAll : StatusesScreenEvent()
    data object InvertSelected : StatusesScreenEvent()
    data object DeselectAll : StatusesScreenEvent()
    data class EnterStatus(val name: String) : StatusesScreenEvent()
    data class AddStatus(val name: String) : StatusesScreenEvent()
    data class UpdateStatus(val id: Long, val newName: String) : StatusesScreenEvent()
    data class DeleteStatus(val id: Long) : StatusesScreenEvent()
    data object DeleteSelectedStatuses : StatusesScreenEvent()
}