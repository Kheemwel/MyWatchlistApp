package com.kheemwel.mywatchlist.data.models

import androidx.lifecycle.ViewModel
import com.kheemwel.mywatchlist.utils.getCurrentDateTimeAsString
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.util.UUID

data class Series(
    val uuid: String = "",
    val title: String,
    val season: Int,
    val episode: Int,
    val genres: List<String>,
    val country: String,
    val status: String,
    val isFavorite: Boolean,
    val releaseDate: String,
    val lastModified: String = ""
)

class SeriesModel : ViewModel() {
    private val _series = MutableStateFlow(emptyList<Series>())
    val series: StateFlow<List<Series>> = _series.asStateFlow()

    fun getSeries(uuid: String): Series? {
        return _series.value.find { it.uuid == uuid }
    }

    fun addSeries(series: Series) {
        _series.update {
            it + series.copy(
                uuid = UUID.randomUUID().toString(),
                lastModified = getCurrentDateTimeAsString()
            )
        }
    }

    fun updateSeries(uuid: String, newSeries: Series) {
        _series.update { currentList ->
            currentList.map { if (it.uuid == uuid) newSeries.copy(lastModified = getCurrentDateTimeAsString()) else it }
        }
    }

    fun deleteSeries(uuid: String) {
        _series.update { currentList ->
            currentList.filterNot { it.uuid == uuid }
        }
    }

    fun deleteSeries(uuids: List<String>) {
        _series.update { currentList ->
            currentList.filterNot { it.uuid in uuids }
        }
    }

    fun isSeriesExists(uuid: String): Boolean {
        return _series.value.any { it.uuid == uuid }
    }

    fun toggleFavorite(uuid: String) {
        _series.update { currentList ->
            currentList.map {
                if (it.uuid == uuid) it.copy(
                    isFavorite = !it.isFavorite,
                    lastModified = getCurrentDateTimeAsString()
                ) else it
            }
        }
    }
}