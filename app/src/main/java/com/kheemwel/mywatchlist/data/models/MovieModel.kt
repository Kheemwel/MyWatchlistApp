package com.kheemwel.mywatchlist.data.models

import androidx.lifecycle.ViewModel
import com.kheemwel.mywatchlist.utils.getCurrentDateTimeAsString
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.util.UUID

data class Movie(
    val uuid: String = "",
    val title: String,
    val genres: List<String>,
    val country: String,
    val status: String,
    val isFavorite: Boolean,
    val releaseDate: String,
    val lastModified: String = ""
)

class MovieModel : ViewModel() {
    private val _movies = MutableStateFlow(emptyList<Movie>())
    val movies: StateFlow<List<Movie>> = _movies.asStateFlow()

    fun getMovie(uuid: String): Movie? {
        return _movies.value.find { it.uuid == uuid }
    }

    fun addMovie(movie: Movie) {
        _movies.update {
            it + movie.copy(
                uuid = UUID.randomUUID().toString(),
                lastModified = getCurrentDateTimeAsString()
            )
        }
    }

    fun updateMovie(uuid: String, newMovie: Movie) {
        _movies.update { currentList ->
            currentList.map { if (it.uuid == uuid) newMovie.copy(lastModified = getCurrentDateTimeAsString()) else it }
        }
    }

    fun deleteMovie(uuid: String) {
        _movies.update { currentList ->
            currentList.filterNot { it.uuid == uuid }
        }
    }

    fun deleteMovies(uuids: List<String>) {
        _movies.update { currentList ->
            currentList.filterNot { it.uuid in uuids }
        }
    }

    fun isMovieExists(uuid: String): Boolean {
        return _movies.value.any { it.uuid == uuid }
    }

    fun toggleFavorite(uuid: String) {
        _movies.update { currentList ->
            currentList.map {
                if (it.uuid == uuid) it.copy(
                    isFavorite = !it.isFavorite,
                    lastModified = getCurrentDateTimeAsString()
                ) else it
            }
        }
    }
}