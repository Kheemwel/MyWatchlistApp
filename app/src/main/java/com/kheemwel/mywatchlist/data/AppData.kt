package com.kheemwel.mywatchlist.data

import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf

object AppData {
    val movieList = mutableStateListOf<Movie>()
    val seriesList = mutableStateListOf<Series>()
    val statusList = mutableStateListOf("Finished Watching", "Pending", "Coming Soon", "Still Watching")
    val countryList = mutableStateListOf("America", "Asia", "Europe", "Africa", "Australia")
    val genreList = mutableStateListOf("Action", "Comedy", "Drama", "Horror", "Fantasy")
}

data class Movie(
    val title: String,
    val duration: String,
    val status: String,
    val genre: String,
    val country: String,
    val favorite: Boolean
)

data class Series(
    val title: String,
    val seasons: Int,
    val episodes: Int,
    val status: String,
    val genre: String,
    val country: String,
    val favorite: Boolean
)
