package com.kheemwel.mywatchlist.data.models

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class StatusModel : ViewModel() {
    private val _statuses = MutableStateFlow(
        listOf(
            "Pending",
            "Still Watching",
            "Finished",
            "Waiting"
        )
    )
    val statuses: StateFlow<List<String>> = _statuses.asStateFlow()

    fun addStatus(status: String) {
        _statuses.update { it + status }
    }

    fun updateStatus(index: Int, newStatus: String) {
        if (index in _statuses.value.indices) {
            _statuses.update { currentList ->
                currentList.toMutableList().apply { set(index, newStatus) }
            }
        }
    }

    fun deleteStatus(index: Int) {
        if (index in _statuses.value.indices) {
            _statuses.update { currentList ->
                currentList.toMutableList().apply { removeAt(index) }
            }
        }
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
    }

    fun isStatusExists(status: String): Boolean {
        return _statuses.value.contains(status)
    }
}