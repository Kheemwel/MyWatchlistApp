package com.kheemwel.mywatchlist.domain.usecase.genre_usecase

import com.kheemwel.mywatchlist.domain.model.Genre
import com.kheemwel.mywatchlist.domain.repository.GenreRepository
import javax.inject.Inject

class GetGenreUseCase @Inject constructor(
    private val repository: GenreRepository
) {
    suspend operator fun invoke(id: Long): Genre? = repository.getGenre(id)
}