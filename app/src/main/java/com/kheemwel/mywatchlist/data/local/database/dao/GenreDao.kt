package com.kheemwel.mywatchlist.data.local.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.kheemwel.mywatchlist.data.local.database.entity.GenreEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface GenreDao {
    @Query("SELECT * FROM genres")
    fun getAllGenres(): Flow<List<GenreEntity>>

    @Query("SELECT * FROM genres WHERE id = :id LIMIT 1")
    suspend fun getGenre(id: Long): GenreEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addGenre(genre: GenreEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addManyGenres(genres: List<GenreEntity>): LongArray

    @Update
    suspend fun updateGenre(genre: GenreEntity)

    @Query("DELETE FROM genres WHERE id = :id")
    suspend fun deleteGenre(id: Long)

    @Query("DELETE FROM genres WHERE id IN (:ids)")
    suspend fun deleteManyGenres(ids: List<Long>)

    @Query("DELETE FROM genres")
    suspend fun deleteAllGenres()
}