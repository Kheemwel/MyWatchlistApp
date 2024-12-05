package com.kheemwel.mywatchlist.domain.repository

import com.kheemwel.mywatchlist.domain.model.Genre
import kotlinx.coroutines.flow.Flow

interface GenreRepository {
    fun getAllGenres(): Flow<List<Genre>>
    suspend fun getGenre(id: Long): Genre?
    suspend fun addGenre(genre: Genre)
    suspend fun addManyGenres(genres: List<Genre>)
    suspend fun updateGenre(genre: Genre)
    suspend fun deleteGenre(id: Long)
    suspend fun deleteManyGenres(ids: List<Long>)
    suspend fun deleteAllGenres()
}