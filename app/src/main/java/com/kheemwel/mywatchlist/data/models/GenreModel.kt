package com.kheemwel.mywatchlist.data.models

import androidx.lifecycle.ViewModel
import com.kheemwel.mywatchlist.data.database.SharedPref
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class GenreModel : ViewModel() {
    private val _genres = MutableStateFlow(SharedPref.getGenres())
    val genres: StateFlow<List<String>> = _genres.asStateFlow()

    fun addGenre(genre: String) {
        _genres.update { it + genre }
        save()
    }

    fun updateGenre(index: Int, newGenre: String) {
        if (index in _genres.value.indices) {
            _genres.update { currentList ->
                currentList.toMutableList().apply { set(index, newGenre) }
            }
        }
        save()
    }

    fun deleteGenre(index: Int) {
        if (index in _genres.value.indices) {
            _genres.update { currentList ->
                currentList.toMutableList().apply { removeAt(index) }
            }
        }
        save()
    }

    fun deleteGenres(indexes: List<Int>) {
        _genres.update { currentList ->
            currentList.toMutableList().apply {
                indexes.sortedDescending().forEach { index ->
                    if (index in indices) {
                        removeAt(index)
                    }
                }
            }
        }
        save()
    }

    fun isGenreExists(genre: String): Boolean {
        return _genres.value.contains(genre)
    }

    private fun save() {
        SharedPref.setGenres(_genres.value)
    }

    fun saveGenres(genres: List<String>, action: DataAction) {
        val currentGenres = _genres.value
        val updatedGenres = when (action) {
            DataAction.Overwrite -> genres
            DataAction.Merge -> (currentGenres + genres).distinct()
            DataAction.Append -> (currentGenres + genres).distinct()
        }
        _genres.value = updatedGenres
        save()
    }
}