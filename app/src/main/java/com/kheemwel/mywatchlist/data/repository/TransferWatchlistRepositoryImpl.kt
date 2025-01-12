package com.kheemwel.mywatchlist.data.repository

import androidx.room.Transaction
import com.kheemwel.mywatchlist.data.local.database.dao.MovieDao
import com.kheemwel.mywatchlist.data.local.database.dao.SeriesDao
import com.kheemwel.mywatchlist.domain.model.Movie
import com.kheemwel.mywatchlist.domain.model.Series
import com.kheemwel.mywatchlist.domain.repository.TransferWatchlistRepository
import com.kheemwel.mywatchlist.utils.getCurrentDateTimeAsString
import javax.inject.Inject

class TransferWatchlistRepositoryImpl @Inject constructor(
    private val movieDao: MovieDao,
    private val seriesDao: SeriesDao
) : TransferWatchlistRepository {

    @Transaction
    override suspend fun transferMovieToSeries(id: Long) {
        val movie = movieDao.getMovie(id)?.toModel()
        movie?.let {
            seriesDao.addSeries(Series(
                title = it.title,
                status = it.status,
                country = it.country,
                genres = it.genres,
                isFavorite = it.isFavorite,
                releaseDate = it.releaseDate,
                lastModified = getCurrentDateTimeAsString(),
            ).toEntity().series)

            movieDao.deleteMovie(it.id)
        }
    }

    @Transaction
    override suspend fun transferSeriesToMovie(id: Long) {
        val series = seriesDao.getSeries(id)?.toModel()
        series?.let {
            movieDao.addMovie(Movie(
                title = it.title,
                status = it.status,
                country = it.country,
                genres = it.genres,
                isFavorite = it.isFavorite,
                releaseDate = it.releaseDate,
                lastModified = getCurrentDateTimeAsString(),
            ).toEntity().movie)

            seriesDao.deleteSeries(it.id)
        }
    }

}