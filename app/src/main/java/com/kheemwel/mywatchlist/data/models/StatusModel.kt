package com.kheemwel.mywatchlist.data.models

import androidx.lifecycle.ViewModel
import com.kheemwel.mywatchlist.data.database.SharedPref
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class StatusModel : ViewModel() {
    private val _statuses = MutableStateFlow(SharedPref.getStatuses())
    val statuses: StateFlow<List<String>> = _statuses.asStateFlow()

    fun addStatus(status: String) {
        _statuses.update { it + status }
        save()
    }

    fun updateStatus(index: Int, newStatus: String) {
        if (index in _statuses.value.indices) {
            _statuses.update { currentList ->
                currentList.toMutableList().apply { set(index, newStatus) }
            }
        }
        save()
    }

    fun deleteStatus(index: Int) {
        if (index in _statuses.value.indices) {
            _statuses.update { currentList ->
                currentList.toMutableList().apply { removeAt(index) }
            }
        }
        save()
    }

    fun deleteStatuses(indexes: List<Int>) {
        _statuses.update { currentList ->
            currentList.toMutableList().apply {
                indexes.sortedDescending().forEach { index ->
                    if (index in indices) {
                        removeAt(index)
                    }
                }
            }
        }
        save()
    }

    fun isStatusExists(status: String): Boolean {
        return _statuses.value.contains(status)
    }

    private fun save() {
        SharedPref.setStatuses(_statuses.value)
    }

    fun saveStatuses(statuses: List<String>, action: DataAction) {
        val currentStatuses = _statuses.value
        val updatedStatuses = when (action) {
            DataAction.Overwrite -> statuses
            DataAction.Merge -> (currentStatuses + statuses).distinct()
            DataAction.Append -> (currentStatuses + statuses).distinct()
        }
        _statuses.value = updatedStatuses
        save()
    }
}