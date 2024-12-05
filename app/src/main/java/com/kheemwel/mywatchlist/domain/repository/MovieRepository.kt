package com.kheemwel.mywatchlist.domain.repository

import com.kheemwel.mywatchlist.domain.model.Movie
import kotlinx.coroutines.flow.Flow

interface MovieRepository {
    fun getAllMovies(): Flow<List<Movie>>
    suspend fun getMovie(id: Long): Movie?
    suspend fun addMovie(movie: Movie)
    suspend fun addManyMovies(movies: List<Movie>)
    suspend fun updateMovie(movie: Movie)
    suspend fun deleteMovie(id: Long)
    suspend fun deleteManyMovies(ids: List<Long>)
    suspend fun deleteAllMovies()
}