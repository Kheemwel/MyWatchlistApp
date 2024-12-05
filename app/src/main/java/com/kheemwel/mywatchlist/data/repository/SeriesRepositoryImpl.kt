package com.kheemwel.mywatchlist.data.repository

import com.kheemwel.mywatchlist.data.local.database.dao.SeriesDao
import com.kheemwel.mywatchlist.data.local.database.entity.SeriesGenresCrossRef
import com.kheemwel.mywatchlist.domain.model.Series
import com.kheemwel.mywatchlist.domain.repository.SeriesRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class SeriesRepositoryImpl @Inject constructor(
    private val dao: SeriesDao
) : SeriesRepository {
    override fun getAllSeries(): Flow<List<Series>> {
        return dao.getAllSeries().map { series ->
            series.map { it.toModel() }
        }
    }

    override suspend fun getSeries(id: Long): Series? {
        return dao.getSeries(id)?.toModel()
    }

    override suspend fun addSeries(series: Series) {
        val entity = series.toEntity()
        val seriesEntity = entity.series
        val genreIds = entity.genres.map { it.id }
        val seriesId = dao.addSeries(seriesEntity)
        val crossRef = genreIds.map { SeriesGenresCrossRef(seriesId, it) }
        if (crossRef.isNotEmpty()) dao.addManySeriesGenreCrossRefs(crossRef)
    }

    override suspend fun addManySeries(series: List<Series>) {
        val entities = series.map { it.toEntity() }
        val seriesEntities = entities.map { it.series }
        val seriesIds = dao.addManySeries(seriesEntities)

        val crossRefs = entities.flatMapIndexed { index, entity ->
            val seriesId = seriesIds[index]
            entity.genres.map { genre -> SeriesGenresCrossRef(seriesId, genre.id) }
        }
        if (crossRefs.isNotEmpty()) dao.addManySeriesGenreCrossRefs(crossRefs.toList())
    }

    override suspend fun updateSeries(series: Series) {
        val entity = series.toEntity()
        val seriesEntity = entity.series
        val newGenreIds = entity.genres.map { it.id }.toSet()
        dao.updateSeries(seriesEntity)

        val currentGenreIds = dao.getSeries(seriesEntity.id)?.genres?.map { it.id }?.toSet() ?: emptySet()
        val insertGenreIds = newGenreIds - currentGenreIds
        val deleteGenreIds = currentGenreIds - newGenreIds
        val insertCrossRef = insertGenreIds.map { SeriesGenresCrossRef(seriesEntity.id, it) }
        val deleteCrossRef = deleteGenreIds.map { SeriesGenresCrossRef(seriesEntity.id, it) }
        if (insertCrossRef.isNotEmpty()) dao.addManySeriesGenreCrossRefs(insertCrossRef)
        if (deleteCrossRef.isNotEmpty()) dao.deleteManySeriesGenreCrossRefs(deleteCrossRef)
    }

    override suspend fun deleteSeries(id: Long) {
        dao.deleteSeriesGenreCrossRef(id)
        dao.deleteSeries(id)
    }

    override suspend fun deleteManySeries(ids: List<Long>) {
        dao.deleteManySeriesGenreCrossRefsBySeriesId(ids)
        dao.deleteManySeries(ids)
    }

    override suspend fun deleteAllSeries() {
        dao.deleteAllSeries()
    }
}