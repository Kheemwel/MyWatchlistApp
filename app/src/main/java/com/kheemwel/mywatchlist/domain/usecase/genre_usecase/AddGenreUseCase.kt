package com.kheemwel.mywatchlist.domain.usecase.genre_usecase

import com.kheemwel.mywatchlist.domain.model.Genre
import com.kheemwel.mywatchlist.domain.repository.GenreRepository
import javax.inject.Inject

class AddGenreUseCase @Inject constructor(
    private val repository: GenreRepository
) {
    @Throws()
    suspend operator fun invoke(genre: Genre) {
        if (genre.name.isBlank()) {
            throw Exception("The genre name can't be empty")
        }
        repository.addGenre(genre)
    }
}