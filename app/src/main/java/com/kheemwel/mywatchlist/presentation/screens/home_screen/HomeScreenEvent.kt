package com.kheemwel.mywatchlist.presentation.screens.home_screen

import com.kheemwel.mywatchlist.domain.model.Country
import com.kheemwel.mywatchlist.domain.model.FilterWatchlist
import com.kheemwel.mywatchlist.domain.model.Genre
import com.kheemwel.mywatchlist.domain.model.Movie
import com.kheemwel.mywatchlist.domain.model.Series
import com.kheemwel.mywatchlist.domain.model.Status

sealed class HomeScreenEvent {
    data object ShowSearch : HomeScreenEvent()
    data object HideSearch : HomeScreenEvent()
    data class EnterSearch(val search: String) : HomeScreenEvent()
    data object ToggleShowFilterSheet : HomeScreenEvent()
    data class UpdateFilter(val filter: FilterWatchlist) : HomeScreenEvent()
    data class ToggleSelectionMode(val value: Boolean) : HomeScreenEvent()
    data class ToggleSelectedMovie(val id: Long) : HomeScreenEvent()
    data class ToggleSelectedSeries(val id: Long) : HomeScreenEvent()
    data object SelectAllMovies : HomeScreenEvent()
    data object InvertSelectedMovies : HomeScreenEvent()
    data object DeselectAllMovies : HomeScreenEvent()
    data object SelectAllSeries : HomeScreenEvent()
    data object InvertSelectedSeries : HomeScreenEvent()
    data object DeselectAllSeries : HomeScreenEvent()
    data object ShowDeleteSelectedDialog : HomeScreenEvent()
    data object HideDeleteSelectedDialog : HomeScreenEvent()
    data object DeleteAllSelected : HomeScreenEvent()
    data object ToggleEditMode : HomeScreenEvent()
    data class ShowMovieModal(
        val editMode: Boolean = true,
        val id: Long? = null,
        val title: String = "",
        val status: Status? = null,
        val country: Country? = null,
        val genres: List<Genre> = emptyList(),
        val isFavorite: Boolean = false,
        val releaseDate: String = ""
    ) : HomeScreenEvent()

    data object HideMovieModal : HomeScreenEvent()
    data class AddMovie(
        val title: String,
        val status: Status? = null,
        val country: Country? = null,
        val genres: List<Genre> = emptyList(),
        val isFavorite: Boolean = false,
        val releaseDate: String = ""
    ) : HomeScreenEvent()

    data class UpdateMovie(
        val id: Long,
        val newTitle: String,
        val newStatus: Status? = null,
        val newCountry: Country? = null,
        val newGenres: List<Genre> = emptyList(),
        val isFavorite: Boolean = false,
        val newReleaseDate: String = ""
    ) : HomeScreenEvent()

    data class DeleteMovie(val id: Long) : HomeScreenEvent()
    data class ShowSeriesModal(
        val editMode: Boolean = true,
        val id: Long? = null,
        val title: String = "",
        val season: Int = 0,
        val episode: Int = 0,
        val status: Status? = null,
        val country: Country? = null,
        val genres: List<Genre> = emptyList(),
        val isFavorite: Boolean = false,
        val releaseDate: String = ""
    ) : HomeScreenEvent()

    data object HideSeriesModal : HomeScreenEvent()
    data class AddSeries(
        val title: String,
        val season: Int = 0,
        val episode: Int = 0,
        val status: Status? = null,
        val country: Country? = null,
        val genres: List<Genre> = emptyList(),
        val isFavorite: Boolean = false,
        val releaseDate: String = ""
    ) : HomeScreenEvent()

    data class UpdateSeries(
        val id: Long,
        val newTitle: String,
        val newSeason: Int = 0,
        val newEpisode: Int = 0,
        val newStatus: Status? = null,
        val newCountry: Country? = null,
        val newGenres: List<Genre> = emptyList(),
        val isFavorite: Boolean = false,
        val newReleaseDate: String = ""
    ) : HomeScreenEvent()

    data class DeleteSeries(val id: Long) : HomeScreenEvent()
    data class ShowDeleteMovieDialog(val id: Long, val title: String) : HomeScreenEvent()
    data object HideDeleteMovieDialog : HomeScreenEvent()
    data class ShowDeleteSeriesDialog(val id: Long, val title: String) : HomeScreenEvent()
    data object HideDeleteSeriesDialog : HomeScreenEvent()
    data class EnterTitle(val title: String) : HomeScreenEvent()
    data class EnterSeason(val season: Int) : HomeScreenEvent()
    data class EnterEpisode(val episode: Int) : HomeScreenEvent()
    data class SelectStatus(val status: Status) : HomeScreenEvent()
    data class SelectCountry(val country: Country) : HomeScreenEvent()
    data class SelectGenre(val genre: Genre) : HomeScreenEvent()
    data class EnterReleaseDate(val date: String) : HomeScreenEvent()
    data object ToggleFavorite : HomeScreenEvent()
    data class ToggleFavoriteMovie(val movie: Movie) : HomeScreenEvent()
    data class ToggleFavoriteSeries(val series: Series) : HomeScreenEvent()
    data object SwitchMovieModalToSeriesModal : HomeScreenEvent()
    data object SwitchSeriesModalToMovieModal : HomeScreenEvent()
    data class TransferToMovie(val id: Long) : HomeScreenEvent()
    data class ShowTransferToMovieDialog(val id: Long, val title: String) : HomeScreenEvent()
    data object HideTransferToMovieDialog : HomeScreenEvent()
    data class TransferToSeries(val id: Long) : HomeScreenEvent()
    data class ShowTransferToSeriesDialog(val id: Long, val title: String) : HomeScreenEvent()
    data object HideTransferToSeriesDialog : HomeScreenEvent()
}