package com.kheemwel.mywatchlist.domain.usecase.genre_usecase

import com.kheemwel.mywatchlist.domain.model.Genre
import com.kheemwel.mywatchlist.domain.repository.GenreRepository
import javax.inject.Inject

class AddManyGenresUseCase @Inject constructor(
    private val repository: GenreRepository
) {
    suspend operator fun invoke(genres: List<Genre>) {
        if (genres.any { it.name.isBlank() }) {
            throw Exception("One of the genre name is empty.")
        }
        repository.addManyGenres(genres)
    }
}