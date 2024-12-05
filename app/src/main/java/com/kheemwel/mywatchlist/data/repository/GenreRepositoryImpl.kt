package com.kheemwel.mywatchlist.data.repository

import com.kheemwel.mywatchlist.data.local.database.dao.GenreDao
import com.kheemwel.mywatchlist.domain.model.Genre
import com.kheemwel.mywatchlist.domain.repository.GenreRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GenreRepositoryImpl @Inject constructor(
    private val dao: GenreDao
) : GenreRepository {
    override fun getAllGenres(): Flow<List<Genre>> {
        return dao.getAllGenres().map { genres ->
            genres.map { it.toModel() }
        }
    }

    override suspend fun getGenre(id: Long): Genre? {
        return dao.getGenre(id)?.toModel()
    }

    override suspend fun addGenre(genre: Genre) {
        dao.addGenre(genre.toEntity())
    }

    override suspend fun addManyGenres(genres: List<Genre>) {
        dao.addManyGenres(genres.map { it.toEntity() })
    }

    override suspend fun updateGenre(genre: Genre) {
        dao.updateGenre(genre.toEntity())
    }

    override suspend fun deleteGenre(id: Long) {
        dao.deleteGenre(id)
    }

    override suspend fun deleteManyGenres(ids: List<Long>) {
        dao.deleteManyGenres(ids)
    }

    override suspend fun deleteAllGenres() {
        dao.deleteAllGenres()
    }
}