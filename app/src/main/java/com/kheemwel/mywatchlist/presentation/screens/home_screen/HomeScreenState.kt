package com.kheemwel.mywatchlist.presentation.screens.home_screen

import com.kheemwel.mywatchlist.domain.model.Country
import com.kheemwel.mywatchlist.domain.model.FilterWatchlist
import com.kheemwel.mywatchlist.domain.model.Genre
import com.kheemwel.mywatchlist.domain.model.Movie
import com.kheemwel.mywatchlist.domain.model.Series
import com.kheemwel.mywatchlist.domain.model.Status

data class HomeScreenState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val movies: List<Movie> = emptyList(),
    val series: List<Series> = emptyList(),
    val statuses: List<Status> = emptyList(),
    val countries: List<Country> = emptyList(),
    val genres: List<Genre> = emptyList(),
    val filterWatchlist: FilterWatchlist = FilterWatchlist(),
    val searchText: String = "",
    val showSearch: Boolean = false,
    val showFilterSheet: Boolean = false,
    val isSelectionMode: Boolean = false,
    val selectedMovies: List<Long> = emptyList(),
    val selectedSeries: List<Long> = emptyList(),
    val isEditMode: Boolean = true,
    val showMovieModal: Boolean = false,
    val showDeleteMovieDialog: Boolean = false,
    val showSeriesModal: Boolean = false,
    val showDeleteSeriesDialog: Boolean = false,
    val showDeleteSelectedDialog: Boolean = false,
    val selectedId: Long? = null,
    val inputTitle: String = "",
    val inputSeason: Int = 0,
    val inputEpisode: Int = 0,
    val inputStatus: Status? = null,
    val inputCountry: Country? = null,
    val inputGenres: List<Genre> = emptyList(),
    val inputFavorite: Boolean = false,
    val inputReleaseDate: String = "",
)