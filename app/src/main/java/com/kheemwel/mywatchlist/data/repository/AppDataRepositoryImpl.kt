package com.kheemwel.mywatchlist.data.repository

import com.kheemwel.mywatchlist.data.local.database.dao.CountryDao
import com.kheemwel.mywatchlist.data.local.database.dao.GenreDao
import com.kheemwel.mywatchlist.data.local.database.dao.MovieDao
import com.kheemwel.mywatchlist.data.local.database.dao.SeriesDao
import com.kheemwel.mywatchlist.data.local.database.dao.StatusDao
import com.kheemwel.mywatchlist.data.local.database.entity.CountryEntity
import com.kheemwel.mywatchlist.data.local.database.entity.GenreEntity
import com.kheemwel.mywatchlist.data.local.database.entity.MovieGenresCrossRef
import com.kheemwel.mywatchlist.data.local.database.entity.SeriesGenresCrossRef
import com.kheemwel.mywatchlist.data.local.database.entity.StatusEntity
import com.kheemwel.mywatchlist.domain.model.AppData
import com.kheemwel.mywatchlist.domain.model.Country
import com.kheemwel.mywatchlist.domain.model.DataAction
import com.kheemwel.mywatchlist.domain.model.Genre
import com.kheemwel.mywatchlist.domain.model.Movie
import com.kheemwel.mywatchlist.domain.model.Series
import com.kheemwel.mywatchlist.domain.model.Status
import com.kheemwel.mywatchlist.domain.repository.AppDataRepository
import kotlinx.coroutines.flow.firstOrNull
import javax.inject.Inject

class AppDataRepositoryImpl @Inject constructor(
    private val statusDao: StatusDao,
    private val countryDao: CountryDao,
    private val genreDao: GenreDao,
    private val movieDao: MovieDao,
    private val seriesDao: SeriesDao
) : AppDataRepository {
    override suspend fun exportData(): AppData {
        val statuses = statusDao.getAllStatuses().firstOrNull()?.map { it.toModel() } ?: emptyList()
        val countries = countryDao.getAllCountries().firstOrNull()?.map { it.toModel() } ?: emptyList()
        val genres = genreDao.getAllGenres().firstOrNull()?.map { it.toModel() } ?: emptyList()
        val movies = movieDao.getAllMovies().firstOrNull()?.map { it.toModel() } ?: emptyList()
        val series = seriesDao.getAllSeries().firstOrNull()?.map { it.toModel() } ?: emptyList()

        return AppData(statuses = statuses, countries = countries, genres = genres, movies = movies, series = series)
    }

    override suspend fun importData(appData: AppData, action: DataAction) {
        if (action == DataAction.Overwrite) {
            statusDao.deleteAllStatuses()
            countryDao.deleteAllCountries()
            genreDao.deleteAllGenres()
            movieDao.deleteAllMovies()
            seriesDao.deleteAllSeries()
        }

        addManyStatuses(appData.statuses)
        addManyCountries(appData.countries)
        addManyGenres(appData.genres)
        val statuses = statusDao.getAllStatuses().firstOrNull() ?: emptyList()
        val countries = countryDao.getAllCountries().firstOrNull() ?: emptyList()
        val genres = genreDao.getAllGenres().firstOrNull() ?: emptyList()
        addManyMovies(appData.movies, statuses, countries, genres)
        addManySeries(appData.series, statuses, countries, genres)
    }

    private suspend fun addManyStatuses(statuses: List<Status>) {
        val currentStatuses = statusDao.getAllStatuses().firstOrNull()?.map { it.name } ?: emptyList()
        val statusList = statuses.filterNot { it.name in currentStatuses }.map { it.toEntity().copy(id = 0) }
        statusDao.addManyStatuses(statusList)
    }

    private suspend fun addManyCountries(countries: List<Country>) {
        val currentCountries = countryDao.getAllCountries().firstOrNull()?.map { it.name } ?: emptyList()
        val countryList = countries.filterNot { it.name in currentCountries }.map { it.toEntity().copy(id = 0) }
        countryDao.addManyCountries(countryList)
    }

    private suspend fun addManyGenres(genres: List<Genre>) {
        val currentGenres = genreDao.getAllGenres().firstOrNull()?.map { it.name } ?: emptyList()
        val genreList = genres.filterNot { it.name in currentGenres }.map { it.toEntity().copy(id = 0) }
        genreDao.addManyGenres(genreList)
    }

    private suspend fun addManyMovies(
        movies: List<Movie>,
        statuses: List<StatusEntity>,
        countries: List<CountryEntity>,
        genres: List<GenreEntity>
    ) {
        val entities = movies.map { movie ->
            val status = statuses.firstOrNull { it.name == movie.status?.name }
            val country = countries.firstOrNull { it.name == movie.country?.name }
            val genreNames = movie.genres.map { it.name }
            val genreList = genres.filter { it.name in genreNames }
            val entity = movie.toEntity()
            entity.copy(
                movie = entity.movie.copy(
                    id = 0,
                    statusId = status?.id,
                    countryId = country?.id
                ),
                status = status,
                country = country,
                genres = genreList
            )
        }
        val movieEntities = entities.map { it.movie }
        val movieIds = movieDao.addManyMovies(movieEntities)

        val crossRefs = entities.flatMapIndexed { index, entity ->
            val movieId = movieIds[index]
            entity.genres.map { genre -> MovieGenresCrossRef(movieId, genre.id) }
        }
        if (crossRefs.isNotEmpty()) movieDao.addManyMovieGenreCrossRefs(crossRefs.toList())
    }

    private suspend fun addManySeries(
        series: List<Series>,
        statuses: List<StatusEntity>,
        countries: List<CountryEntity>,
        genres: List<GenreEntity>
    ) {
        val entities = series.map { serie ->
            val status = statuses.firstOrNull { it.name == serie.status?.name }
            val country = countries.firstOrNull { it.name == serie.country?.name }
            val genreNames = serie.genres.map { it.name }
            val genreList = genres.filter { it.name in genreNames }
            val entity = serie.toEntity()
            entity.copy(
                series = entity.series.copy(
                    id = 0,
                    statusId = status?.id,
                    countryId = country?.id
                ),
                status = status,
                country = country,
                genres = genreList
            )
        }
        val seriesEntities = entities.map { it.series }
        val seriesIds = seriesDao.addManySeries(seriesEntities)

        val crossRefs = entities.flatMapIndexed { index, entity ->
            val seriesId = seriesIds[index]
            entity.genres.map { genre -> SeriesGenresCrossRef(seriesId, genre.id) }
        }
        if (crossRefs.isNotEmpty()) seriesDao.addManySeriesGenreCrossRefs(crossRefs.toList())
    }
}
