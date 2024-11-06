package com.kheemwel.mywatchlist.data.models

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class GenreModel : ViewModel() {
    private val _genres = MutableStateFlow(
        listOf(
            "SciFi",
            "Thriller",
            "Fantasy",
            "Romance",
            "Drama",
            "Action",
            "Adventure",
            "Comedy",
            "Horror",
            "Animation",
            "Anime",
            "Cartoon",
            "Documentary",
            "Family",
            "History",
            "Musical",
            "Mystery",
            "War",
            "Western",
            "Asian",
            "BL",
            "GL",
            "LGBTQIA+",
            "Hollywood",
            "Bollywood",
            "Netflix"
        )
    )
    val genres: StateFlow<List<String>> = _genres.asStateFlow()

    fun addGenre(genre: String) {
        _genres.update { it + genre }
    }

    fun updateGenre(index: Int, newGenre: String) {
        if (index in _genres.value.indices) {
            _genres.update { currentList ->
                currentList.toMutableList().apply { set(index, newGenre) }
            }
        }
    }

    fun deleteGenre(index: Int) {
        if (index in _genres.value.indices) {
            _genres.update { currentList ->
                currentList.toMutableList().apply { removeAt(index) }
            }
        }
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
    }

    fun isGenreExists(genre: String): Boolean {
        return _genres.value.contains(genre)
    }
}