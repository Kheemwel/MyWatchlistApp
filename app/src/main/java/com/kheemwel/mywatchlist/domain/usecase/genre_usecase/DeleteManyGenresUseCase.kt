package com.kheemwel.mywatchlist.domain.usecase.genre_usecase

import com.kheemwel.mywatchlist.domain.repository.GenreRepository
import javax.inject.Inject

class DeleteManyGenresUseCase @Inject constructor(
    private val repository: GenreRepository
) {
    suspend operator fun invoke(ids: List<Long>) = repository.deleteManyGenres(ids)
}
