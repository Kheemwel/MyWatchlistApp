package com.kheemwel.mywatchlist.domain.usecase.movie_usecase

import com.kheemwel.mywatchlist.domain.model.Movie
import com.kheemwel.mywatchlist.domain.repository.MovieRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetAllMoviesUseCase @Inject constructor(
    private val repository: MovieRepository
) {
    operator fun invoke(): Flow<List<Movie>> = repository.getAllMovies()
}