package com.kheemwel.mywatchlist.domain.usecase.movie_usecase

import com.kheemwel.mywatchlist.domain.model.Movie
import com.kheemwel.mywatchlist.domain.repository.MovieRepository
import javax.inject.Inject

class AddMovieUseCase @Inject constructor(
    private val repository: MovieRepository
) {
    @Throws()
    suspend operator fun invoke(movie: Movie) {
        if (movie.title.isBlank()) {
            throw Exception("The movie name can't be empty")
        }
        repository.addMovie(movie)
    }
}