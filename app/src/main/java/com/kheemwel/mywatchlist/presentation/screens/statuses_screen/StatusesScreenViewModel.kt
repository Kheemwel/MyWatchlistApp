package com.kheemwel.mywatchlist.presentation.screens.statuses_screen

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kheemwel.mywatchlist.domain.model.Status
import com.kheemwel.mywatchlist.domain.usecase.status_usecase.StatusUseCases
import com.kheemwel.mywatchlist.presentation.composables.showSnackbar
import com.kheemwel.mywatchlist.utils.update
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class StatusesScreenViewModel @Inject constructor(
    private val useCases: StatusUseCases
) : ViewModel() {
    private val _state = mutableStateOf(StatusesScreenState())
    val state: State<StatusesScreenState> = _state
    private var statusesJob: Job? = null
    val snackbarHostState = SnackbarHostState()

    init {
        getStatuses()
    }

    fun onEvent(event: StatusesScreenEvent) {
        when (event) {
            is StatusesScreenEvent.EnterStatus -> enterStatus(event.name)
            is StatusesScreenEvent.AddStatus -> addStatus(event.name)
            is StatusesScreenEvent.UpdateStatus -> updateStatus(event.id, event.newName)
            is StatusesScreenEvent.DeleteStatus -> deleteStatus(event.id)
            is StatusesScreenEvent.DeleteSelectedStatuses -> deleteSelectedStatuses()
            is StatusesScreenEvent.OpenForm -> openForm(event.id, event.name)
            StatusesScreenEvent.CloseForm -> closeForm()
            is StatusesScreenEvent.OpenDeleteStatusDialog -> openDeleteStatusDialog(event.id, event.name)
            StatusesScreenEvent.CloseDeleteStatusDialog -> closeDeleteStatusDialog()
            StatusesScreenEvent.OpenDeleteSelectedStatusesDialog -> openDeleteSelectedStatusesDialog()
            StatusesScreenEvent.CloseDeleteSelectedStatusesDialog -> closeDeleteSelectedStatusesDialog()
            is StatusesScreenEvent.ToggleSelectionMode -> toggleSelectionMode(event.value)
            is StatusesScreenEvent.ToggleSelectedStatus -> toggleSelectedStatus(event.id)
            StatusesScreenEvent.SelectAll -> selectAll()
            StatusesScreenEvent.InvertSelected -> invertSelected()
            StatusesScreenEvent.DeselectAll -> deselectAll()
        }
    }

    private fun getStatuses() {
        statusesJob?.cancel()
        statusesJob = useCases.getAllStatusesUseCase()
            .onStart {
                _state.update { copy(isLoading = true) }
                delay(3000)
            }
            .onCompletion { error ->
                error?.message?.let { showSnackbar(snackbarHostState, it) }
            }
            .onEach {
                _state.update { copy(statuses = it, isLoading = false) }
            }.launchIn(viewModelScope)
    }

    private fun enterStatus(name: String) {
        _state.update {
            copy(
                error = if (doesStatusExist(name)) "Status already exist" else null,
                inputName = name
            )
        }
    }

    private fun addStatus(name: String) {
        if (doesStatusExist(name)) {
            _state.update {
                copy(
                    error = "Status already exist",
                )
            }
            return
        }

        viewModelScope.launch {
            try {
                useCases.addStatusUseCase(Status(name = name))
            } catch (e: Exception) {
                _state.update { copy(error = e.message) }
            }
        }
    }

    private fun updateStatus(id: Long, newName: String) {
        viewModelScope.launch {
            try {
                useCases.updateStatusUseCase(Status(id, newName))
            } catch (e: Exception) {
                _state.update { copy(error = e.message) }
            }
        }
    }

    private fun deleteStatus(id: Long) {
        viewModelScope.launch {
            try {
                useCases.deleteStatusUseCase(id)
            } catch (e: Exception) {
                e.message?.let { showSnackbar(snackbarHostState, it) }
            }
        }
    }

    private fun deleteSelectedStatuses() {
        viewModelScope.launch {
            try {
                val selected = _state.value.selectedStatuses
                useCases.deleteManyStatusesUseCase(selected)
            } catch (e: Exception) {
                e.message?.let { showSnackbar(snackbarHostState, it) }
            }
        }
    }

    private fun openForm(id: Long?, name: String) {
        _state.update { copy(isFormOpen = true, selectedId = id, inputName = name) }
    }

    private fun closeForm() {
        _state.update { copy(isFormOpen = false, error = null, selectedId = null, inputName = "") }
    }

    private fun openDeleteStatusDialog(id: Long, name: String) {
        _state.update { copy(isDeleteStatusDialogOpen = true, selectedId = id, inputName = name) }
    }

    private fun closeDeleteStatusDialog() {
        _state.update { copy(isDeleteStatusDialogOpen = false, selectedId = null, inputName = "") }
    }

    private fun openDeleteSelectedStatusesDialog() {
        _state.update { copy(isDeleteSelectedStatusesDialogOpen = true) }
    }


    private fun closeDeleteSelectedStatusesDialog() {
        _state.update { copy(isDeleteSelectedStatusesDialogOpen = false) }
    }

    private fun toggleSelectionMode(value: Boolean) {
        _state.update { copy(isSelectionMode = value) }
        if (!value) {
            deselectAll()
        }
    }

    private fun toggleSelectedStatus(id: Long) {
        val statuses = _state.value.selectedStatuses.toMutableList()
        if (statuses.contains(id)) {
            statuses.remove(id)
        } else {
            statuses.add(id)
        }
        _state.update { copy(selectedStatuses = statuses.toList()) }
    }

    private fun selectAll() {
        val statuses = _state.value.statuses.map { it.id }
        _state.update { copy(selectedStatuses = statuses) }
    }


    private fun invertSelected() {
        val statuses = _state.value.statuses.map { it.id }
        val inverted = statuses.filterNot { it in _state.value.selectedStatuses }
        _state.update { copy(selectedStatuses = inverted) }
    }

    private fun deselectAll() {
        _state.update { copy(selectedStatuses = emptyList()) }
    }

    private fun doesStatusExist(name: String): Boolean {
        return _state.value.statuses.any { it.name == name }
    }
}