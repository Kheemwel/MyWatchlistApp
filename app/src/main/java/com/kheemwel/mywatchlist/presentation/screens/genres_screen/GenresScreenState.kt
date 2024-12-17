package com.kheemwel.mywatchlist.presentation.screens.genres_screen

import com.kheemwel.mywatchlist.domain.model.Genre

data class GenresScreenState(
    val genres: List<Genre> = emptyList(),
    val selectedGenres: List<Long> = emptyList(),
    val inputName: String = "",
    val selectedId: Long? = null,
    val isLoading: Boolean = false,
    val error: String? = null,
    val isSelectionMode: Boolean = false,
    val isFormOpen: Boolean = false,
    val isDeleteGenreDialogOpen: Boolean = false,
    val isDeleteSelectedGenresDialogOpen: Boolean = false
)