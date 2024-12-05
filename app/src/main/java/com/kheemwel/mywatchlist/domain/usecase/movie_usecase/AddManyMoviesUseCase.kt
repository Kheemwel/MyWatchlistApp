package com.kheemwel.mywatchlist.domain.usecase.movie_usecase

import com.kheemwel.mywatchlist.domain.model.Movie
import com.kheemwel.mywatchlist.domain.repository.MovieRepository
import javax.inject.Inject

class AddManyMoviesUseCase @Inject constructor(
    private val repository: MovieRepository
) {
    suspend operator fun invoke(movies: List<Movie>) {
        if (movies.any { it.title.isBlank() }) {
            throw Exception("One of the movie name is empty.")
        }
        repository.addManyMovies(movies)
    }
}