package com.kheemwel.mywatchlist.presentation.screens.genres_screen

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kheemwel.mywatchlist.domain.model.Genre
import com.kheemwel.mywatchlist.domain.usecase.genre_usecase.GenreUseCases
import com.kheemwel.mywatchlist.presentation.composables.showSnackbar
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
class GenresScreenViewModel @Inject constructor(
    private val useCases: GenreUseCases
) : ViewModel() {
    private val _state = mutableStateOf(GenresScreenState())
    val state: State<GenresScreenState> = _state
    private var genresJob: Job? = null
    val snackbarHostState = SnackbarHostState()

    init {
        getGenres()
    }

    fun onEvent(event: GenresScreenEvent) {
        when (event) {
            is GenresScreenEvent.EnterGenre -> enterGenre(event.name)
            is GenresScreenEvent.AddGenre -> addGenre(event.name)
            is GenresScreenEvent.UpdateGenre -> updateGenre(event.id, event.newName)
            is GenresScreenEvent.DeleteGenre -> deleteGenre(event.id)
            is GenresScreenEvent.DeleteSelectedGenres -> deleteSelectedGenres()
            is GenresScreenEvent.OpenForm -> openForm(event.id, event.name)
            GenresScreenEvent.CloseForm -> closeForm()
            is GenresScreenEvent.OpenDeleteGenreDialog -> openDeleteGenreDialog(event.id, event.name)
            GenresScreenEvent.CloseDeleteGenreDialog -> closeDeleteGenreDialog()
            GenresScreenEvent.OpenDeleteSelectedGenresDialog -> openDeleteSelectedGenresDialog()
            GenresScreenEvent.CloseDeleteSelectedGenresDialog -> closeDeleteSelectedGenresDialog()
            is GenresScreenEvent.ToggleSelectionMode -> toggleSelectionMode(event.value)
            is GenresScreenEvent.ToggleSelectedGenre -> toggleSelectedGenre(event.id)
            GenresScreenEvent.SelectAll -> selectAll()
            GenresScreenEvent.InvertSelected -> invertSelected()
            GenresScreenEvent.DeselectAll -> deselectAll()
        }
    }

    private fun getGenres() {
        genresJob?.cancel()
        genresJob = useCases.getAllGenresUseCase()
            .onStart {
                _state.update { copy(isLoading = true) }
            }
            .onCompletion { error ->
                error?.message?.let { showSnackbar(snackbarHostState, it) }
            }
            .onEach {
                _state.update { copy(genres = it, isLoading = false) }
            }.launchIn(viewModelScope)
    }

    private fun enterGenre(name: String) {
        _state.update {
            copy(
                error = if (doesGenreExist(name)) "Genre already exist" else null,
                inputName = name
            )
        }
    }

    private fun addGenre(name: String) {
        if (doesGenreExist(name)) {
            _state.update {
                copy(
                    error = "Genre already exist",
                )
            }
            return
        }

        viewModelScope.launch {
            try {
                useCases.addGenreUseCase(Genre(name = name))
                _state.update { copy(error = null) }
            } catch (e: Exception) {
                _state.update { copy(error = e.message) }
            }
        }
    }

    private fun updateGenre(id: Long, newName: String) {
        viewModelScope.launch {
            try {
                useCases.updateGenreUseCase(Genre(id, newName))
            } catch (e: Exception) {
                _state.update { copy(error = e.message) }
            }
        }
    }

    private fun deleteGenre(id: Long) {
        viewModelScope.launch {
            try {
                useCases.deleteGenreUseCase(id)
            } catch (e: Exception) {
                e.message?.let { showSnackbar(snackbarHostState, it) }
            }
        }
    }

    private fun deleteSelectedGenres() {
        viewModelScope.launch {
            try {
                val selected = _state.value.selectedGenres
                useCases.deleteManyGenresUseCase(selected)
            } catch (e: Exception) {
                e.message?.let { showSnackbar(snackbarHostState, it) }
            }
        }
    }

    private fun openForm(id: Long?, name: String) {
        _state.update { copy(isFormOpen = true, selectedId = id, inputName = name) }
    }

    private fun closeForm() {
        _state.update { copy(isFormOpen = false, error = null, selectedId = null, inputName = "") }
    }

    private fun openDeleteGenreDialog(id: Long, name: String) {
        _state.update { copy(isDeleteGenreDialogOpen = true, selectedId = id, inputName = name) }
    }

    private fun closeDeleteGenreDialog() {
        _state.update { copy(isDeleteGenreDialogOpen = false, selectedId = null, inputName = "") }
    }

    private fun openDeleteSelectedGenresDialog() {
        _state.update { copy(isDeleteSelectedGenresDialogOpen = true) }
    }


    private fun closeDeleteSelectedGenresDialog() {
        _state.update { copy(isDeleteSelectedGenresDialogOpen = false) }
    }

    private fun toggleSelectionMode(value: Boolean) {
        _state.update { copy(isSelectionMode = value) }
        if (!value) {
            deselectAll()
        }
    }

    private fun toggleSelectedGenre(id: Long) {
        val genres = _state.value.selectedGenres.toMutableList()
        if (genres.contains(id)) {
            genres.remove(id)
        } else {
            genres.add(id)
        }
        _state.update { copy(selectedGenres = genres.toList()) }
    }

    private fun selectAll() {
        val genres = _state.value.genres.map { it.id }
        _state.update { copy(selectedGenres = genres) }
    }


    private fun invertSelected() {
        val genres = _state.value.genres.map { it.id }
        val inverted = genres.filterNot { it in _state.value.selectedGenres }
        _state.update { copy(selectedGenres = inverted) }
    }

    private fun deselectAll() {
        _state.update { copy(selectedGenres = emptyList()) }
    }

    private fun doesGenreExist(name: String): Boolean {
        return _state.value.genres.any { it.name == name }
    }
}