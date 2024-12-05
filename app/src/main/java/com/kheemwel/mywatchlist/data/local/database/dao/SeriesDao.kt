package com.kheemwel.mywatchlist.data.local.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.kheemwel.mywatchlist.data.local.database.entity.SeriesEntity
import com.kheemwel.mywatchlist.data.local.database.entity.SeriesGenresCrossRef
import com.kheemwel.mywatchlist.data.local.database.entity.SeriesWithDetails
import kotlinx.coroutines.flow.Flow

@Dao
interface SeriesDao {
    @Transaction
    @Query("SELECT * FROM series")
    fun getAllSeries(): Flow<List<SeriesWithDetails>>

    @Transaction
    @Query("SELECT * FROM series WHERE id = :id LIMIT 1")
    suspend fun getSeries(id: Long): SeriesWithDetails?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addSeries(series: SeriesEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addManySeries(series: List<SeriesEntity>): LongArray

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addSeriesGenreCrossRef(crossRef: SeriesGenresCrossRef)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addManySeriesGenreCrossRefs(crossRefs: List<SeriesGenresCrossRef>)

    @Update
    suspend fun updateSeries(series: SeriesEntity)

    @Query("DELETE FROM series WHERE id = :id")
    suspend fun deleteSeries(id: Long)

    @Query("DELETE FROM series WHERE id IN (:ids)")
    suspend fun deleteManySeries(ids: List<Long>)

    @Query("DELETE FROM series_genres WHERE seriesId = :seriesId")
    suspend fun deleteSeriesGenreCrossRef(seriesId: Long)

    @Query("DELETE FROM series_genres WHERE seriesId IN (:seriesIds)")
    suspend fun deleteManySeriesGenreCrossRefsBySeriesId(seriesIds: List<Long>)

    @Delete
    suspend fun deleteManySeriesGenreCrossRefs(crossRefs: List<SeriesGenresCrossRef>)

    @Query("DELETE FROM series")
    suspend fun deleteAllSeries()
}