package com.kheemwel.mywatchlist.domain.usecase.genre_usecase

import com.kheemwel.mywatchlist.domain.repository.GenreRepository
import javax.inject.Inject

class DeleteAllGenresUseCase @Inject constructor(
    private val repository: GenreRepository
) {
    suspend operator fun invoke() = repository.deleteAllGenres()
}