package com.kheemwel.mywatchlist.data.local.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.kheemwel.mywatchlist.data.local.database.entity.MovieEntity
import com.kheemwel.mywatchlist.data.local.database.entity.MovieGenresCrossRef
import com.kheemwel.mywatchlist.data.local.database.entity.MovieWithDetails
import kotlinx.coroutines.flow.Flow

@Dao
interface MovieDao {
    @Transaction
    @Query("SELECT * FROM movies")
    fun getAllMovies(): Flow<List<MovieWithDetails>>

    @Transaction
    @Query("SELECT * FROM movies WHERE id = :id LIMIT 1")
    suspend fun getMovie(id: Long): MovieWithDetails?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addMovie(movie: MovieEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addManyMovies(movies: List<MovieEntity>): LongArray

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addMovieGenreCrossRef(crossRef: MovieGenresCrossRef)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addManyMovieGenreCrossRefs(crossRefs: List<MovieGenresCrossRef>)

    @Update
    suspend fun updateMovie(movie: MovieEntity)

    @Query("DELETE FROM movies WHERE id = :id")
    suspend fun deleteMovie(id: Long)

    @Query("DELETE FROM movies WHERE id IN (:ids)")
    suspend fun deleteManyMovies(ids: List<Long>)

    @Query("DELETE FROM movie_genres WHERE movieId = :movieId")
    suspend fun deleteMovieGenreCrossRef(movieId: Long)

    @Query("DELETE FROM movie_genres WHERE movieId IN (:movieIds)")
    suspend fun deleteManyMovieGenreCrossRefsByMovieId(movieIds: List<Long>)

    @Delete
    suspend fun deleteManyMovieGenreCrossRefs(crossRef: List<MovieGenresCrossRef>)

    @Query("DELETE FROM movies")
    suspend fun deleteAllMovies()
}