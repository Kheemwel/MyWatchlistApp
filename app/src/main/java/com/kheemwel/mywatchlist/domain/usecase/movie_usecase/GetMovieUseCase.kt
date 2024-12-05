package com.kheemwel.mywatchlist.domain.usecase.movie_usecase

import com.kheemwel.mywatchlist.domain.model.Movie
import com.kheemwel.mywatchlist.domain.repository.MovieRepository
import javax.inject.Inject

class GetMovieUseCase @Inject constructor(
    private val repository: MovieRepository
) {
    suspend operator fun invoke(id: Long): Movie? = repository.getMovie(id)
}