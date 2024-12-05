package com.kheemwel.mywatchlist.domain.usecase.movie_usecase

import com.kheemwel.mywatchlist.domain.repository.MovieRepository
import javax.inject.Inject

class DeleteMovieUseCase @Inject constructor(
    private val repository: MovieRepository
) {
    suspend operator fun invoke(id: Long) = repository.deleteMovie(id)
}