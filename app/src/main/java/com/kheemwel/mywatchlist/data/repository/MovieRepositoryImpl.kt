package com.kheemwel.mywatchlist.data.repository

import com.kheemwel.mywatchlist.data.local.database.dao.MovieDao
import com.kheemwel.mywatchlist.data.local.database.entity.MovieGenresCrossRef
import com.kheemwel.mywatchlist.domain.model.Movie
import com.kheemwel.mywatchlist.domain.repository.MovieRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class MovieRepositoryImpl @Inject constructor(
    private val dao: MovieDao
) : MovieRepository {
    override fun getAllMovies(): Flow<List<Movie>> {
        return dao.getAllMovies().map { movies ->
            movies.map { it.toModel() }
        }
    }

    override suspend fun getMovie(id: Long): Movie? {
        return dao.getMovie(id)?.toModel()
    }

    override suspend fun addMovie(movie: Movie) {
        val entity = movie.toEntity()
        val movieEntity = entity.movie
        val genreIds = entity.genres.map { it.id }
        val movieId = dao.addMovie(movieEntity)
        val crossRef = genreIds.map { MovieGenresCrossRef(movieId, it) }
        if (crossRef.isNotEmpty()) dao.addManyMovieGenreCrossRefs(crossRef)
    }

    override suspend fun addManyMovies(movies: List<Movie>) {
        val entities = movies.map { it.toEntity() }
        val movieEntities = entities.map { it.movie }
        val movieIds = dao.addManyMovies(movieEntities)

        val crossRefs = entities.flatMapIndexed { index, entity ->
            val movieId = movieIds[index]
            entity.genres.map { genre -> MovieGenresCrossRef(movieId, genre.id) }
        }
        if (crossRefs.isNotEmpty()) dao.addManyMovieGenreCrossRefs(crossRefs.toList())
    }

    override suspend fun updateMovie(movie: Movie) {
        val entity = movie.toEntity()
        val movieEntity = entity.movie
        val newGenreIds = entity.genres.map { it.id }.toSet()
        dao.updateMovie(movieEntity)

        val currentGenreIds = dao.getMovie(movieEntity.id)?.genres?.map { it.id }?.toSet() ?: emptySet()
        val insertGenreIds = newGenreIds - currentGenreIds
        val deleteGenreIds = currentGenreIds - newGenreIds
        val insertCrossRef = insertGenreIds.map { MovieGenresCrossRef(movieEntity.id, it) }
        val deleteCrossRef = deleteGenreIds.map { MovieGenresCrossRef(movieEntity.id, it) }
        if (insertCrossRef.isNotEmpty()) dao.addManyMovieGenreCrossRefs(insertCrossRef)
        if (deleteCrossRef.isNotEmpty()) dao.deleteManyMovieGenreCrossRefs(deleteCrossRef)
    }

    override suspend fun deleteMovie(id: Long) {
        dao.deleteMovieGenreCrossRef(id)
        dao.deleteMovie(id)
    }

    override suspend fun deleteManyMovies(ids: List<Long>) {
        dao.deleteManyMovieGenreCrossRefsByMovieId(ids)
        dao.deleteManyMovies(ids)
    }

    override suspend fun deleteAllMovies() {
        dao.deleteAllMovies()
    }
}