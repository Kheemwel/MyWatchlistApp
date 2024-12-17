package com.kheemwel.mywatchlist.domain.usecase.genre_usecase

import com.kheemwel.mywatchlist.domain.model.Genre
import com.kheemwel.mywatchlist.domain.repository.GenreRepository
import javax.inject.Inject

class UpdateGenreUseCase @Inject constructor(
    private val repository: GenreRepository
) {
    suspend operator fun invoke(genre: Genre) = repository.updateGenre(genre)
}