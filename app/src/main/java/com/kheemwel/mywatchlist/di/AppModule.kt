package com.kheemwel.mywatchlist.di

import android.app.Application
import android.content.Context
import androidx.room.Room
import com.kheemwel.mywatchlist.data.local.database.MyWatchlistDatabase
import com.kheemwel.mywatchlist.data.local.database.dao.CountryDao
import com.kheemwel.mywatchlist.data.local.database.dao.GenreDao
import com.kheemwel.mywatchlist.data.local.database.dao.MovieDao
import com.kheemwel.mywatchlist.data.local.database.dao.SeriesDao
import com.kheemwel.mywatchlist.data.local.database.dao.StatusDao
import com.kheemwel.mywatchlist.data.local.datastore.manager.FilterWatchlistManager
import com.kheemwel.mywatchlist.data.repository.AppDataRepositoryImpl
import com.kheemwel.mywatchlist.data.repository.CountryRepositoryImpl
import com.kheemwel.mywatchlist.data.repository.FilterWatchlistRepositoryImpl
import com.kheemwel.mywatchlist.data.repository.GenreRepositoryImpl
import com.kheemwel.mywatchlist.data.repository.MovieRepositoryImpl
import com.kheemwel.mywatchlist.data.repository.SeriesRepositoryImpl
import com.kheemwel.mywatchlist.data.repository.StatusRepositoryImpl
import com.kheemwel.mywatchlist.data.util.DATABASE_NAME
import com.kheemwel.mywatchlist.domain.repository.AppDataRepository
import com.kheemwel.mywatchlist.domain.repository.CountryRepository
import com.kheemwel.mywatchlist.domain.repository.FilterWatchlistRepository
import com.kheemwel.mywatchlist.domain.repository.GenreRepository
import com.kheemwel.mywatchlist.domain.repository.MovieRepository
import com.kheemwel.mywatchlist.domain.repository.SeriesRepository
import com.kheemwel.mywatchlist.domain.repository.StatusRepository
import com.kheemwel.mywatchlist.domain.usecase.appdata_usecase.AppDataUseCases
import com.kheemwel.mywatchlist.domain.usecase.appdata_usecase.ExportAppDataUseCase
import com.kheemwel.mywatchlist.domain.usecase.appdata_usecase.ImportAppDataUseCase
import com.kheemwel.mywatchlist.domain.usecase.country_usecase.AddCountryUseCase
import com.kheemwel.mywatchlist.domain.usecase.country_usecase.AddManyCountriesUseCase
import com.kheemwel.mywatchlist.domain.usecase.country_usecase.CountryUseCases
import com.kheemwel.mywatchlist.domain.usecase.country_usecase.DeleteAllCountriesUseCase
import com.kheemwel.mywatchlist.domain.usecase.country_usecase.DeleteCountryUseCase
import com.kheemwel.mywatchlist.domain.usecase.country_usecase.DeleteManyCountriesUseCase
import com.kheemwel.mywatchlist.domain.usecase.country_usecase.GetAllCountriesUseCase
import com.kheemwel.mywatchlist.domain.usecase.country_usecase.GetCountryUseCase
import com.kheemwel.mywatchlist.domain.usecase.country_usecase.UpdateCountryUseCase
import com.kheemwel.mywatchlist.domain.usecase.filter_watchlist_usecase.FilterWatchlistUseCases
import com.kheemwel.mywatchlist.domain.usecase.filter_watchlist_usecase.GetFilterWatchlistUseCase
import com.kheemwel.mywatchlist.domain.usecase.filter_watchlist_usecase.UpdateFilterWatchlistUseCase
import com.kheemwel.mywatchlist.domain.usecase.genre_usecase.AddGenreUseCase
import com.kheemwel.mywatchlist.domain.usecase.genre_usecase.AddManyGenresUseCase
import com.kheemwel.mywatchlist.domain.usecase.genre_usecase.DeleteAllGenresUseCase
import com.kheemwel.mywatchlist.domain.usecase.genre_usecase.DeleteGenreUseCase
import com.kheemwel.mywatchlist.domain.usecase.genre_usecase.DeleteManyGenresUseCase
import com.kheemwel.mywatchlist.domain.usecase.genre_usecase.GenreUseCases
import com.kheemwel.mywatchlist.domain.usecase.genre_usecase.GetAllGenresUseCase
import com.kheemwel.mywatchlist.domain.usecase.genre_usecase.GetGenreUseCase
import com.kheemwel.mywatchlist.domain.usecase.genre_usecase.UpdateGenreUseCase
import com.kheemwel.mywatchlist.domain.usecase.movie_usecase.AddManyMoviesUseCase
import com.kheemwel.mywatchlist.domain.usecase.movie_usecase.AddMovieUseCase
import com.kheemwel.mywatchlist.domain.usecase.movie_usecase.DeleteAllMoviesUseCase
import com.kheemwel.mywatchlist.domain.usecase.movie_usecase.DeleteManyMoviesUseCase
import com.kheemwel.mywatchlist.domain.usecase.movie_usecase.DeleteMovieUseCase
import com.kheemwel.mywatchlist.domain.usecase.movie_usecase.GetAllMoviesUseCase
import com.kheemwel.mywatchlist.domain.usecase.movie_usecase.GetMovieUseCase
import com.kheemwel.mywatchlist.domain.usecase.movie_usecase.MovieUseCases
import com.kheemwel.mywatchlist.domain.usecase.movie_usecase.UpdateMovieUseCase
import com.kheemwel.mywatchlist.domain.usecase.series_usecase.AddManySeriesUseCase
import com.kheemwel.mywatchlist.domain.usecase.series_usecase.AddSeriesUseCase
import com.kheemwel.mywatchlist.domain.usecase.series_usecase.DeleteAllSeriesUseCase
import com.kheemwel.mywatchlist.domain.usecase.series_usecase.DeleteManySeriesUseCase
import com.kheemwel.mywatchlist.domain.usecase.series_usecase.DeleteSeriesUseCase
import com.kheemwel.mywatchlist.domain.usecase.series_usecase.GetAllSeriesUseCase
import com.kheemwel.mywatchlist.domain.usecase.series_usecase.GetSeriesUseCase
import com.kheemwel.mywatchlist.domain.usecase.series_usecase.SeriesUseCases
import com.kheemwel.mywatchlist.domain.usecase.series_usecase.UpdateSeriesUseCase
import com.kheemwel.mywatchlist.domain.usecase.status_usecase.AddManyStatusesUseCase
import com.kheemwel.mywatchlist.domain.usecase.status_usecase.AddStatusUseCase
import com.kheemwel.mywatchlist.domain.usecase.status_usecase.DeleteAllStatusesUseCase
import com.kheemwel.mywatchlist.domain.usecase.status_usecase.DeleteManyStatusesUseCase
import com.kheemwel.mywatchlist.domain.usecase.status_usecase.DeleteStatusUseCase
import com.kheemwel.mywatchlist.domain.usecase.status_usecase.GetAllStatusesUseCase
import com.kheemwel.mywatchlist.domain.usecase.status_usecase.GetStatusUseCase
import com.kheemwel.mywatchlist.domain.usecase.status_usecase.StatusUseCases
import com.kheemwel.mywatchlist.domain.usecase.status_usecase.UpdateStatusUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    @Singleton
    fun provideDatabase(app: Application): MyWatchlistDatabase {
        return Room.databaseBuilder(
            app,
            MyWatchlistDatabase::class.java,
            DATABASE_NAME
        ).build()
    }

    @Provides
    @Singleton
    fun provideStatusDao(db: MyWatchlistDatabase): StatusDao {
        return db.statusDao()
    }

    @Provides
    @Singleton
    fun provideStatusRepository(dao: StatusDao): StatusRepository {
        return StatusRepositoryImpl(dao)
    }

    @Provides
    @Singleton
    fun provideStatusUseCases(repository: StatusRepository): StatusUseCases {
        return StatusUseCases(
            getAllStatusesUseCase = GetAllStatusesUseCase(repository),
            getStatusUseCase = GetStatusUseCase(repository),
            addStatusUseCase = AddStatusUseCase(repository),
            addManyStatusesUseCase = AddManyStatusesUseCase(repository),
            updateStatusUseCase = UpdateStatusUseCase(repository),
            deleteStatusUseCase = DeleteStatusUseCase(repository),
            deleteManyStatusesUseCase = DeleteManyStatusesUseCase(repository),
            deleteAllStatusesUseCase = DeleteAllStatusesUseCase(repository)
        )
    }

    @Provides
    @Singleton
    fun provideGenreDao(db: MyWatchlistDatabase): GenreDao {
        return db.genreDao()
    }

    @Provides
    @Singleton
    fun provideGenreRepository(dao: GenreDao): GenreRepository {
        return GenreRepositoryImpl(dao)
    }

    @Provides
    @Singleton
    fun provideGenreUseCases(repository: GenreRepository): GenreUseCases {
        return GenreUseCases(
            getAllGenresUseCase = GetAllGenresUseCase(repository),
            getGenreUseCase = GetGenreUseCase(repository),
            addGenreUseCase = AddGenreUseCase(repository),
            addManyGenresUseCase = AddManyGenresUseCase(repository),
            updateGenreUseCase = UpdateGenreUseCase(repository),
            deleteGenreUseCase = DeleteGenreUseCase(repository),
            deleteManyGenresUseCase = DeleteManyGenresUseCase(repository),
            deleteAllGenresUseCase = DeleteAllGenresUseCase(repository)
        )
    }

    @Provides
    @Singleton
    fun provideCountryDao(db: MyWatchlistDatabase): CountryDao {
        return db.countryDao()
    }

    @Provides
    @Singleton
    fun provideCountryRepository(dao: CountryDao): CountryRepository {
        return CountryRepositoryImpl(dao)
    }

    @Provides
    @Singleton
    fun provideCountryUseCases(repository: CountryRepository): CountryUseCases {
        return CountryUseCases(
            getAllCountriesUseCase = GetAllCountriesUseCase(repository),
            getCountryUseCase = GetCountryUseCase(repository),
            addCountryUseCase = AddCountryUseCase(repository),
            addManyCountriesUseCase = AddManyCountriesUseCase(repository),
            updateCountryUseCase = UpdateCountryUseCase(repository),
            deleteCountryUseCase = DeleteCountryUseCase(repository),
            deleteManyCountriesUseCase = DeleteManyCountriesUseCase(repository),
            deleteAllCountriesUseCase = DeleteAllCountriesUseCase(repository)
        )
    }

    @Provides
    @Singleton
    fun provideMovieDao(db: MyWatchlistDatabase): MovieDao {
        return db.movieDao()
    }

    @Provides
    @Singleton
    fun provideMovieRepository(dao: MovieDao): MovieRepository {
        return MovieRepositoryImpl(dao)
    }

    @Provides
    @Singleton
    fun provideMovieUseCases(repository: MovieRepository): MovieUseCases {
        return MovieUseCases(
            getAllMoviesUseCase = GetAllMoviesUseCase(repository),
            getMovieUseCase = GetMovieUseCase(repository),
            addMovieUseCase = AddMovieUseCase(repository),
            addManyMoviesUseCase = AddManyMoviesUseCase(repository),
            updateMovieUseCase = UpdateMovieUseCase(repository),
            deleteMovieUseCase = DeleteMovieUseCase(repository),
            deleteManyMoviesUseCase = DeleteManyMoviesUseCase(repository),
            deleteAllMoviesUseCase = DeleteAllMoviesUseCase(repository)
        )
    }

    @Provides
    @Singleton
    fun provideSeriesDao(db: MyWatchlistDatabase): SeriesDao {
        return db.seriesDao()
    }

    @Provides
    @Singleton
    fun provideSeriesRepository(dao: SeriesDao): SeriesRepository {
        return SeriesRepositoryImpl(dao)
    }

    @Provides
    @Singleton
    fun provideSeriesUseCases(repository: SeriesRepository): SeriesUseCases {
        return SeriesUseCases(
            getAllSeriesUseCase = GetAllSeriesUseCase(repository),
            getSeriesUseCase = GetSeriesUseCase(repository),
            addSeriesUseCase = AddSeriesUseCase(repository),
            addManySeriesUseCase = AddManySeriesUseCase(repository),
            updateSeriesUseCase = UpdateSeriesUseCase(repository),
            deleteSeriesUseCase = DeleteSeriesUseCase(repository),
            deleteManySeriesUseCase = DeleteManySeriesUseCase(repository),
            deleteAllSeriesUseCase = DeleteAllSeriesUseCase(repository)
        )
    }

    @Provides
    @Singleton
    fun provideFilterWatchlistManager(@ApplicationContext context: Context): FilterWatchlistManager {
        return FilterWatchlistManager(context)
    }

    @Provides
    @Singleton
    fun provideFilterWatchlistRepository(manager: FilterWatchlistManager): FilterWatchlistRepository {
        return FilterWatchlistRepositoryImpl(manager)
    }

    @Provides
    @Singleton
    fun provideFilterWatchlistUseCases(repository: FilterWatchlistRepository): FilterWatchlistUseCases {
        return FilterWatchlistUseCases(
            getFilterUseCase = GetFilterWatchlistUseCase(repository),
            updateFilterWatchlistUseCase = UpdateFilterWatchlistUseCase(repository)
        )
    }

    @Provides
    @Singleton
    fun provideAppDataRepository(
        statusDao: StatusDao,
        countryDao: CountryDao,
        genreDao: GenreDao,
        movieDao: MovieDao,
        seriesDao: SeriesDao
    ): AppDataRepository {
        return AppDataRepositoryImpl(statusDao, countryDao, genreDao, movieDao, seriesDao)
    }

    @Provides
    @Singleton
    fun provideAppDataUseCases(repository: AppDataRepository): AppDataUseCases {
        return AppDataUseCases(
            exportAppDataUseCase = ExportAppDataUseCase(repository),
            importAppDataUseCase = ImportAppDataUseCase(repository)
        )
    }
}