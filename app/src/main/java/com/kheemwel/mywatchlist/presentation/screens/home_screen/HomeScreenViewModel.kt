package com.kheemwel.mywatchlist.presentation.screens.home_screen

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kheemwel.mywatchlist.domain.model.Country
import com.kheemwel.mywatchlist.domain.model.FilterSortBy
import com.kheemwel.mywatchlist.domain.model.FilterSortDirection
import com.kheemwel.mywatchlist.domain.model.FilterWatchlist
import com.kheemwel.mywatchlist.domain.model.Genre
import com.kheemwel.mywatchlist.domain.model.Movie
import com.kheemwel.mywatchlist.domain.model.Series
import com.kheemwel.mywatchlist.domain.model.Status
import com.kheemwel.mywatchlist.domain.usecase.country_usecase.GetAllCountriesUseCase
import com.kheemwel.mywatchlist.domain.usecase.filter_watchlist_usecase.FilterWatchlistUseCases
import com.kheemwel.mywatchlist.domain.usecase.genre_usecase.GetAllGenresUseCase
import com.kheemwel.mywatchlist.domain.usecase.movie_usecase.MovieUseCases
import com.kheemwel.mywatchlist.domain.usecase.series_usecase.SeriesUseCases
import com.kheemwel.mywatchlist.domain.usecase.status_usecase.GetAllStatusesUseCase
import com.kheemwel.mywatchlist.presentation.composables.showSnackbar
import com.kheemwel.mywatchlist.utils.getCurrentDateTimeAsString
import com.kheemwel.mywatchlist.utils.update
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeScreenViewModel @Inject constructor(
    private val movieUseCase: MovieUseCases,
    private val seriesUseCase: SeriesUseCases,
    private val getAllStatusesUseCase: GetAllStatusesUseCase,
    private val getAllCountriesUseCase: GetAllCountriesUseCase,
    private val getAllGenresUseCase: GetAllGenresUseCase,
    private val filterUseCase: FilterWatchlistUseCases,
) : ViewModel() {
    private val _state = mutableStateOf(HomeScreenState())
    val state: State<HomeScreenState> = _state

    private var statusesJob: Job? = null
    private var countriesJob: Job? = null
    private var genresJob: Job? = null
    private var moviesJob: Job? = null
    private var movieList = emptyList<Movie>()
    private var seriesJob: Job? = null
    private var seriesList = emptyList<Series>()
    private var filterWatchlistJob: Job? = null

    val snackbarHostState = SnackbarHostState()

    init {
        viewModelScope.launch {
            getStatuses()
            getCountries()
            getGenres()
            getFilterWatchlist()
            getMovies()
            getSeries()
        }
    }

    fun onEvent(event: HomeScreenEvent) {
        when (event) {
            is HomeScreenEvent.AddMovie -> addMovie(
                event.title,
                event.status,
                event.country,
                event.genres,
                event.releaseDate,
                event.isFavorite
            )

            is HomeScreenEvent.AddSeries -> addSeries(
                event.title,
                event.season,
                event.episode,
                event.status,
                event.country,
                event.genres,
                event.releaseDate,
                event.isFavorite
            )

            HomeScreenEvent.DeleteAllSelected -> deleteAllSelected()
            is HomeScreenEvent.DeleteMovie -> deleteMovie(event.id)
            is HomeScreenEvent.DeleteSeries -> deleteSeries(event.id)
            HomeScreenEvent.DeselectAllMovies -> deselectAllMovies()
            HomeScreenEvent.DeselectAllSeries -> deselectSeries()
            is HomeScreenEvent.EnterEpisode -> enterEpisode(event.episode)
            is HomeScreenEvent.EnterReleaseDate -> enterReleaseDate(event.date)
            is HomeScreenEvent.EnterSearch -> enterSearch(event.search)
            is HomeScreenEvent.EnterSeason -> enterSeason(event.season)
            is HomeScreenEvent.EnterTitle -> enterTitle(event.title)
            HomeScreenEvent.HideDeleteMovieDialog -> hideDeleteMovieDialog()
            HomeScreenEvent.HideDeleteSelectedDialog -> hideDeleteSelectedDialog()
            HomeScreenEvent.HideDeleteSeriesDialog -> hideDeleteSeriesDialog()
            HomeScreenEvent.HideMovieModal -> hideMovieModal()
            HomeScreenEvent.HideSearch -> hideSearch()
            HomeScreenEvent.HideSeriesModal -> hideSeriesModal()
            HomeScreenEvent.InvertSelectedMovies -> invertSelectedMovies()
            HomeScreenEvent.InvertSelectedSeries -> invertSelectedSeries()
            HomeScreenEvent.SelectAllMovies -> selectAllMovies()
            HomeScreenEvent.SelectAllSeries -> selectAllSeries()
            is HomeScreenEvent.SelectCountry -> selectCountry(event.country)
            is HomeScreenEvent.SelectGenre -> selectGenre(event.genre)
            is HomeScreenEvent.SelectStatus -> selectStatus(event.status)
            is HomeScreenEvent.ShowDeleteMovieDialog -> showDeleteMovieDialog(event.id, event.title)
            HomeScreenEvent.ShowDeleteSelectedDialog -> showDeleteSelectedDialog()
            is HomeScreenEvent.ShowDeleteSeriesDialog -> showDeleteSeriesDialog(event.id, event.title)
            is HomeScreenEvent.ShowMovieModal -> showMovieModal(
                event.editMode,
                event.id,
                event.title,
                event.status,
                event.country,
                event.genres,
                event.releaseDate,
                event.isFavorite
            )

            HomeScreenEvent.ShowSearch -> showSearch()
            is HomeScreenEvent.ShowSeriesModal -> showSeriesModal(
                event.editMode,
                event.id,
                event.title,
                event.season,
                event.episode,
                event.status,
                event.country,
                event.genres,
                event.releaseDate,
                event.isFavorite
            )

            HomeScreenEvent.ToggleEditMode -> toggleEditMode()
            HomeScreenEvent.ToggleFavorite -> toggleFavorite()
            is HomeScreenEvent.ToggleFavoriteMovie -> toggleFavoriteMovie(event.movie)
            is HomeScreenEvent.ToggleFavoriteSeries -> toggleFavoriteSeries(event.series)
            is HomeScreenEvent.ToggleSelectedMovie -> toggleSelectedMovie(event.id)
            is HomeScreenEvent.ToggleSelectedSeries -> toggleSelectedSeries(event.id)
            is HomeScreenEvent.ToggleSelectionMode -> toggleSelectionMode(event.value)
            HomeScreenEvent.ToggleShowFilterSheet -> toggleFilterSheet()
            is HomeScreenEvent.UpdateFilter -> updateFilterWatchlist(event.filter)
            is HomeScreenEvent.UpdateMovie -> updateMovie(
                event.id,
                event.newTitle,
                event.newStatus,
                event.newCountry,
                event.newGenres,
                event.newReleaseDate,
                event.isFavorite
            )

            is HomeScreenEvent.UpdateSeries -> updateSeries(
                event.id,
                event.newTitle,
                event.newSeason,
                event.newEpisode,
                event.newStatus,
                event.newCountry,
                event.newGenres,
                event.newReleaseDate,
                event.isFavorite
            )
        }
    }

    private fun getStatuses() {
        statusesJob?.cancel()
        statusesJob = getAllStatusesUseCase()
            .onStart {
                _state.update { copy(isLoading = true) }
            }
            .onCompletion { error ->
                error?.message?.let { showSnackbar(snackbarHostState, it) }
            }
            .onEach {
                _state.update { copy(isLoading = false, statuses = it) }
            }.launchIn(viewModelScope)
    }

    private fun getCountries() {
        countriesJob?.cancel()
        countriesJob = getAllCountriesUseCase()
            .onStart {
                _state.update { copy(isLoading = true) }
            }
            .onCompletion { error ->
                error?.message?.let { showSnackbar(snackbarHostState, it) }
            }
            .onEach {
                _state.update { copy(isLoading = false, countries = it) }
            }.launchIn(viewModelScope)
    }

    private fun getGenres() {
        genresJob?.cancel()
        genresJob = getAllGenresUseCase()
            .onStart {
                _state.update { copy(isLoading = true) }
            }
            .onCompletion { error ->
                error?.message?.let { showSnackbar(snackbarHostState, it) }
            }
            .onEach {
                _state.update { copy(isLoading = false, genres = it) }
            }.launchIn(viewModelScope)
    }

    private fun getMovies() {
        moviesJob?.cancel()
        moviesJob = movieUseCase.getAllMoviesUseCase()
            .onStart {
                _state.update { copy(isLoading = true) }
            }
            .onCompletion { error ->
                error?.message?.let { showSnackbar(snackbarHostState, it) }
            }
            .onEach { movies ->
                movieList = movies
                val stateValue = _state.value
                filterMovies(stateValue.searchText, stateValue.filterWatchlist, movies)
                _state.update { copy(isLoading = false) }
            }.launchIn(viewModelScope)
    }

    private fun getSeries() {
        seriesJob?.cancel()
        seriesJob = seriesUseCase.getAllSeriesUseCase()
            .onStart {
                _state.update { copy(isLoading = true) }
            }
            .onCompletion { error ->
                error?.message?.let { showSnackbar(snackbarHostState, it) }
            }
            .onEach { series ->
                seriesList = series
                val stateValue = _state.value
                filterSeries(stateValue.searchText, stateValue.filterWatchlist, series)
                _state.update { copy(isLoading = false) }
            }.launchIn(viewModelScope)
    }

    private fun getFilterWatchlist() {
        filterWatchlistJob?.cancel()
        filterWatchlistJob = filterUseCase.getFilterUseCase()
            .onStart {
                _state.update { copy(isLoading = true) }
            }
            .onCompletion { error ->
                error?.message?.let { showSnackbar(snackbarHostState, it) }
            }
            .onEach {
                _state.update { copy(filterWatchlist = it) }
            }.launchIn(viewModelScope)
    }

    private fun toggleFilterSheet() {
        _state.update { copy(showFilterSheet = !_state.value.showFilterSheet) }
    }

    private fun updateFilterWatchlist(filter: FilterWatchlist) {
        viewModelScope.launch {
            try {
                filterUseCase.updateFilterWatchlistUseCase(filter)
                _state.update { copy(filterWatchlist = filter) }
                val stateValue = _state.value
                filterMovies(stateValue.searchText, filter, movieList)
                filterSeries(stateValue.searchText, filter, seriesList)
            } catch (e: Exception) {
                _state.update { copy(error = e.localizedMessage) }
            }
        }
    }

    private fun showSearch() {
        _state.update { copy(showSearch = true) }
    }

    private fun enterSearch(search: String) {
        _state.update { copy(searchText = search) }
        val stateValue = _state.value
        filterMovies(search, stateValue.filterWatchlist, movieList)
        filterSeries(search, stateValue.filterWatchlist, seriesList)
    }

    private fun hideSearch() {
        _state.update { copy(showSearch = false, searchText = "") }
        val stateValue = _state.value
        filterMovies("", stateValue.filterWatchlist, movieList)
        filterSeries("", stateValue.filterWatchlist, seriesList)
    }

    private fun filterMovies(searchText: String, filterWatchlist: FilterWatchlist, movies: List<Movie>) {
        val filteredMovies = movies.filter { movie ->
            // Search Text Filter
            (searchText.isEmpty() ||
                    movie.title.contains(searchText, ignoreCase = true) ||
                    movie.genres.any { it.name.contains(searchText, ignoreCase = true) } ||
                    movie.status?.name?.contains(searchText, ignoreCase = true) == true ||
                    movie.country?.name?.contains(searchText, ignoreCase = true) == true) &&
                    // Genres Filter
                    (filterWatchlist.genres.isEmpty() || movie.genres.any { filterWatchlist.genres.contains(it.name) }) &&
                    // Status Filter
                    (filterWatchlist.statuses.isEmpty() || filterWatchlist.statuses.contains(movie.status?.name)) &&
                    // Countries Filter
                    (filterWatchlist.countries.isEmpty() || filterWatchlist.countries.contains(movie.country?.name))
        }.let { filtered ->
            // Sorting
            if (filterWatchlist.sortDirection == FilterSortDirection.Ascending) {
                filtered.sortedBy {
                    when (filterWatchlist.sortBy) {
                        FilterSortBy.Title -> it.title
                        FilterSortBy.ReleaseDate -> it.releaseDate
                        FilterSortBy.LastModified -> it.lastModified
                        FilterSortBy.Favorite -> it.isFavorite.toString()
                    }
                }
            } else {
                filtered.sortedByDescending {
                    when (filterWatchlist.sortBy) {
                        FilterSortBy.Title -> it.title
                        FilterSortBy.ReleaseDate -> it.releaseDate
                        FilterSortBy.LastModified -> it.lastModified
                        FilterSortBy.Favorite -> it.isFavorite.toString()
                    }
                }
            }
        }
        _state.update { copy(movies = filteredMovies) }
    }

    private fun filterSeries(searchText: String, filterWatchlist: FilterWatchlist, series: List<Series>) {
        val filteredSeries = series.filter { serie ->
            // Search Text Filter
            (searchText.isEmpty() ||
                    serie.title.contains(searchText, ignoreCase = true) ||
                    serie.genres.any { it.name.contains(searchText, ignoreCase = true) } ||
                    serie.status?.name?.contains(searchText, ignoreCase = true) == true ||
                    serie.country?.name?.contains(searchText, ignoreCase = true) == true) &&
                    // Genres Filter
                    (filterWatchlist.genres.isEmpty() || serie.genres.any { filterWatchlist.genres.contains(it.name) }) &&
                    // Status Filter
                    (filterWatchlist.statuses.isEmpty() || filterWatchlist.statuses.contains(serie.status?.name)) &&
                    // Countries Filter
                    (filterWatchlist.countries.isEmpty() || filterWatchlist.countries.contains(serie.country?.name))
        }.let { filtered ->
            // Sorting
            if (filterWatchlist.sortDirection == FilterSortDirection.Ascending) {
                filtered.sortedBy {
                    when (filterWatchlist.sortBy) {
                        FilterSortBy.Title -> it.title
                        FilterSortBy.ReleaseDate -> it.releaseDate
                        FilterSortBy.LastModified -> it.lastModified
                        FilterSortBy.Favorite -> it.isFavorite.toString()
                    }
                }
            } else {
                filtered.sortedByDescending {
                    when (filterWatchlist.sortBy) {
                        FilterSortBy.Title -> it.title
                        FilterSortBy.ReleaseDate -> it.releaseDate
                        FilterSortBy.LastModified -> it.lastModified
                        FilterSortBy.Favorite -> it.isFavorite.toString()
                    }
                }
            }
        }
        _state.update { copy(series = filteredSeries) }
    }

    private fun showMovieModal(
        editMode: Boolean,
        id: Long?,
        title: String,
        status: Status?,
        country: Country?,
        genres: List<Genre>,
        releaseDate: String,
        isFavorite: Boolean
    ) {
        _state.update {
            copy(
                showMovieModal = true,
                isEditMode = editMode,
                selectedId = id,
                inputTitle = title,
                inputStatus = status,
                inputCountry = country,
                inputGenres = genres,
                inputFavorite = isFavorite,
                inputReleaseDate = releaseDate
            )
        }
    }

    private fun hideMovieModal() {
        _state.update {
            copy(
                showMovieModal = false,
                isEditMode = false,
                inputTitle = "",
                inputStatus = null,
                inputCountry = null,
                inputGenres = emptyList(),
                inputFavorite = false,
                inputReleaseDate = "",
                error = null
            )
        }
    }

    private fun addMovie(
        title: String,
        status: Status?,
        country: Country?,
        genres: List<Genre>,
        releaseDate: String,
        isFavorite: Boolean
    ) {
        viewModelScope.launch {
            try {
                movieUseCase.addMovieUseCase(
                    Movie(
                        title = title,
                        status = status,
                        country = country,
                        genres = genres,
                        isFavorite = isFavorite,
                        releaseDate = releaseDate,
                        lastModified = getCurrentDateTimeAsString()
                    )
                )
                hideMovieModal()
            } catch (e: Exception) {
                _state.update { copy(error = e.localizedMessage) }
            }
        }
    }

    private fun updateMovie(
        id: Long,
        newTitle: String,
        newStatus: Status?,
        newCountry: Country?,
        newGenres: List<Genre>,
        newReleaseDate: String,
        isFavorite: Boolean
    ) {
        viewModelScope.launch {
            try {
                movieUseCase.updateMovieUseCase(
                    Movie(
                        id = id,
                        title = newTitle,
                        status = newStatus,
                        country = newCountry,
                        genres = newGenres,
                        isFavorite = isFavorite,
                        releaseDate = newReleaseDate,
                        lastModified = getCurrentDateTimeAsString()
                    )
                )
                hideMovieModal()
            } catch (e: Exception) {
                _state.update { copy(error = e.localizedMessage) }
            }
        }
    }

    private fun toggleFavoriteMovie(movie: Movie) {
        viewModelScope.launch {
            try {
                val updatedMovie = movie.copy(isFavorite = !movie.isFavorite)
                movieUseCase.updateMovieUseCase(updatedMovie)
            } catch (e: Exception) {
                e.localizedMessage?.let { showSnackbar(snackbarHostState, it) }
            }
        }
    }

    private fun showDeleteMovieDialog(id: Long, title: String) {
        _state.update { copy(showDeleteMovieDialog = true, selectedId = id, inputTitle = title) }
    }

    private fun deleteMovie(id: Long) {
        viewModelScope.launch {
            try {
                movieUseCase.deleteMovieUseCase(id)
            } catch (e: Exception) {
                _state.update { copy(error = e.localizedMessage) }
            }
        }
    }

    private fun hideDeleteMovieDialog() {
        _state.update { copy(showDeleteMovieDialog = false, selectedId = null, inputTitle = "") }
    }

    private fun showSeriesModal(
        editMode: Boolean,
        id: Long?,
        title: String,
        season: Int,
        episode: Int,
        status: Status?,
        country: Country?,
        genres: List<Genre>,
        releaseDate: String,
        isFavorite: Boolean
    ) {
        _state.update {
            copy(
                showSeriesModal = true,
                isEditMode = editMode,
                selectedId = id,
                inputTitle = title,
                inputSeason = season,
                inputEpisode = episode,
                inputStatus = status,
                inputCountry = country,
                inputGenres = genres,
                inputFavorite = isFavorite,
                inputReleaseDate = releaseDate
            )
        }
    }

    private fun hideSeriesModal() {
        _state.update {
            copy(
                showSeriesModal = false,
                isEditMode = false,
                inputTitle = "",
                inputSeason = 0,
                inputEpisode = 0,
                inputStatus = null,
                inputCountry = null,
                inputGenres = emptyList(),
                inputFavorite = false,
                inputReleaseDate = "",
                error = null
            )
        }
    }

    private fun addSeries(
        title: String,
        season: Int,
        episode: Int,
        status: Status?,
        country: Country?,
        genres: List<Genre>,
        releaseDate: String,
        isFavorite: Boolean
    ) {
        viewModelScope.launch {
            try {
                seriesUseCase.addSeriesUseCase(
                    Series(
                        title = title,
                        season = season,
                        episode = episode,
                        status = status,
                        country = country,
                        genres = genres,
                        isFavorite = isFavorite,
                        releaseDate = releaseDate,
                        lastModified = getCurrentDateTimeAsString()
                    )
                )
                hideSeriesModal()
            } catch (e: Exception) {
                _state.update { copy(error = e.localizedMessage) }
            }
        }
    }

    private fun updateSeries(
        id: Long,
        newTitle: String,
        newSeason: Int,
        newEpisode: Int,
        newStatus: Status?,
        newCountry: Country?,
        newGenres: List<Genre>,
        newReleaseDate: String,
        isFavorite: Boolean
    ) {
        viewModelScope.launch {
            try {
                seriesUseCase.updateSeriesUseCase(
                    Series(
                        id = id,
                        title = newTitle,
                        season = newSeason,
                        episode = newEpisode,
                        status = newStatus,
                        country = newCountry,
                        genres = newGenres,
                        isFavorite = isFavorite,
                        releaseDate = newReleaseDate,
                        lastModified = getCurrentDateTimeAsString()
                    )
                )
                hideSeriesModal()
            } catch (e: Exception) {
                _state.update { copy(error = e.localizedMessage) }
            }
        }
    }

    private fun toggleFavoriteSeries(series: Series) {
        viewModelScope.launch {
            try {
                val updatedSeries = series.copy(isFavorite = !series.isFavorite)
                seriesUseCase.updateSeriesUseCase(updatedSeries)
            } catch (e: Exception) {
                e.localizedMessage?.let { showSnackbar(snackbarHostState, it) }
            }
        }
    }

    private fun showDeleteSeriesDialog(id: Long, title: String) {
        _state.update { copy(showDeleteSeriesDialog = true, selectedId = id, inputTitle = title) }
    }

    private fun deleteSeries(id: Long) {
        viewModelScope.launch {
            try {
                seriesUseCase.deleteSeriesUseCase(id)
            } catch (e: Exception) {
                _state.update { copy(error = e.localizedMessage) }
            }
        }
    }

    private fun hideDeleteSeriesDialog() {
        _state.update { copy(showDeleteSeriesDialog = false, selectedId = null, inputTitle = "") }
    }

    private fun enterTitle(title: String) {
        _state.update { copy(inputTitle = title) }
    }

    private fun enterSeason(season: Int) {
        _state.update { copy(inputSeason = season) }
    }

    private fun enterEpisode(episode: Int) {
        _state.update { copy(inputEpisode = episode) }
    }

    private fun selectStatus(status: Status) {
        _state.update { copy(inputStatus = status) }
    }

    private fun selectCountry(country: Country) {
        _state.update { copy(inputCountry = country) }
    }

    private fun selectGenre(genre: Genre) {
        val inputGenres = _state.value.inputGenres.toMutableList()
        if (inputGenres.contains(genre)) {
            inputGenres.remove(genre)
        } else {
            inputGenres.add(genre)
        }
        _state.update { copy(inputGenres = inputGenres.toList()) }
    }

    private fun enterReleaseDate(date: String) {
        _state.update { copy(inputReleaseDate = date) }
    }

    private fun toggleFavorite() {
        _state.update { copy(inputFavorite = !_state.value.inputFavorite) }
    }

    private fun toggleEditMode() {
        _state.update { copy(isEditMode = !_state.value.isEditMode) }
    }

    private fun toggleSelectionMode(value: Boolean) {
        _state.update { copy(isSelectionMode = value) }
    }

    private fun selectAllMovies() {
        _state.update { copy(selectedMovies = movieList.map { it.id }) }
    }

    private fun toggleSelectedMovie(id: Long) {
        val selected = _state.value.selectedMovies.toMutableList()
        if (selected.contains(id)) {
            selected.remove(id)
        } else {
            selected.add(id)
        }
        _state.update { copy(selectedMovies = selected.toList()) }
    }

    private fun invertSelectedMovies() {
        val movieIds = movieList.map { it.id }
        val inverted = movieIds.filterNot { it in _state.value.selectedMovies }
        _state.update { copy(selectedMovies = inverted) }
    }

    private fun deselectAllMovies() {
        _state.update { copy(selectedMovies = emptyList()) }
    }

    private fun selectAllSeries() {
        _state.update { copy(selectedSeries = seriesList.map { it.id }) }
    }

    private fun toggleSelectedSeries(id: Long) {
        val selected = _state.value.selectedSeries.toMutableList()
        if (selected.contains(id)) {
            selected.remove(id)
        } else {
            selected.add(id)
        }
        _state.update { copy(selectedSeries = selected.toList()) }
    }

    private fun invertSelectedSeries() {
        val seriesIds = seriesList.map { it.id }
        val inverted = seriesIds.filterNot { it in _state.value.selectedSeries }
        _state.update { copy(selectedSeries = inverted) }
    }

    private fun deselectSeries() {
        _state.update { copy(selectedSeries = emptyList()) }
    }

    private fun showDeleteSelectedDialog() {
        _state.update { copy(showDeleteSelectedDialog = true) }
    }

    private fun deleteAllSelected() {
        viewModelScope.launch {
            try {
                val selectedMovies = _state.value.selectedMovies
                val selectedSeries = _state.value.selectedSeries
                if (selectedMovies.isNotEmpty()) movieUseCase.deleteManyMoviesUseCase(selectedMovies)
                if (selectedSeries.isNotEmpty()) seriesUseCase.deleteManySeriesUseCase(selectedSeries)
            } catch (e: Exception) {
                _state.update { copy(error = e.localizedMessage) }
            }
        }
    }

    private fun hideDeleteSelectedDialog() {
        _state.update { copy(showDeleteSelectedDialog = false) }
    }
}