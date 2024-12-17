package com.kheemwel.mywatchlist.presentation.screens.genres_screen

sealed class GenresScreenEvent {
    data class OpenForm(val id: Long? = null, val name: String = "") : GenresScreenEvent()
    data object CloseForm : GenresScreenEvent()
    data class OpenDeleteGenreDialog(val id: Long, val name: String) : GenresScreenEvent()
    data object CloseDeleteGenreDialog : GenresScreenEvent()
    data object OpenDeleteSelectedGenresDialog : GenresScreenEvent()
    data object CloseDeleteSelectedGenresDialog : GenresScreenEvent()
    data class ToggleSelectionMode(val value: Boolean) : GenresScreenEvent()
    data class ToggleSelectedGenre(val id: Long) : GenresScreenEvent()
    data object SelectAll : GenresScreenEvent()
    data object InvertSelected : GenresScreenEvent()
    data object DeselectAll : GenresScreenEvent()
    data class EnterGenre(val name: String) : GenresScreenEvent()
    data class AddGenre(val name: String) : GenresScreenEvent()
    data class UpdateGenre(val id: Long, val newName: String) : GenresScreenEvent()
    data class DeleteGenre(val id: Long) : GenresScreenEvent()
    data object DeleteSelectedGenres : GenresScreenEvent()
}