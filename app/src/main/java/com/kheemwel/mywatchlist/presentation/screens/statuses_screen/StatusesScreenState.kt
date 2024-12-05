package com.kheemwel.mywatchlist.presentation.screens.statuses_screen

import com.kheemwel.mywatchlist.domain.model.Status

data class StatusesScreenState(
    val statuses: List<Status> = emptyList(),
    val selectedStatuses: List<Long> = emptyList(),
    val inputName: String = "",
    val selectedId: Long? = null,
    val isLoading: Boolean = false,
    val error: String? = null,
    val snackbarMessage: String? = null,
    val isSelectionMode: Boolean = false,
    val isFormOpen: Boolean = false,
    val isDeleteStatusDialogOpen: Boolean = false,
    val isDeleteSelectedStatusesDialogOpen: Boolean = false
)
