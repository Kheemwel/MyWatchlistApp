package com.kheemwel.mywatchlist.data.models

import androidx.lifecycle.ViewModel
import com.kheemwel.mywatchlist.data.database.SharedPref
import com.kheemwel.mywatchlist.utils.getCurrentDateTimeAsString
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.serialization.Serializable
import java.util.UUID

@Serializable
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
    private val _series = MutableStateFlow(SharedPref.getSeries())
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
        save()
    }

    fun updateSeries(uuid: String, newSeries: Series) {
        _series.update { currentList ->
            currentList.map { if (it.uuid == uuid) newSeries.copy(lastModified = getCurrentDateTimeAsString()) else it }
        }
        save()
    }

    fun deleteSeries(uuid: String) {
        _series.update { currentList ->
            currentList.filterNot { it.uuid == uuid }
        }
        save()
    }

    fun deleteSeries(uuids: List<String>) {
        _series.update { currentList ->
            currentList.filterNot { it.uuid in uuids }
        }
        save()
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
        save()
    }

    private fun save() {
        SharedPref.setSeries(_series.value)
    }

    fun saveSeries(series: List<Series>, action: DataAction) {
        val currentSeries = _series.value
        val updatedSeries = when (action) {
            DataAction.Overwrite -> series
            DataAction.Merge -> (currentSeries + series).distinct()
            DataAction.Append -> currentSeries + series
        }
        _series.value = updatedSeries
        save()
    }
}