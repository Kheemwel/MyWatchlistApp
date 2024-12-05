package com.kheemwel.mywatchlist.domain.repository

import com.kheemwel.mywatchlist.domain.model.Series
import kotlinx.coroutines.flow.Flow

interface SeriesRepository {
    fun getAllSeries(): Flow<List<Series>>
    suspend fun getSeries(id: Long): Series?
    suspend fun addSeries(series: Series)
    suspend fun addManySeries(series: List<Series>)
    suspend fun updateSeries(series: Series)
    suspend fun deleteSeries(id: Long)
    suspend fun deleteManySeries(ids: List<Long>)
    suspend fun deleteAllSeries()
}