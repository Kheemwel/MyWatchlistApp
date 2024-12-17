package com.kheemwel.mywatchlist.domain.usecase.genre_usecase

import com.kheemwel.mywatchlist.domain.repository.GenreRepository
import javax.inject.Inject

class DeleteGenreUseCase @Inject constructor(
    private val repository: GenreRepository
) {
    suspend operator fun invoke(id: Long) = repository.deleteGenre(id)
}