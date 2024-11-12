package com.kheemwel.mywatchlist.data.database

import android.content.Context
import android.content.SharedPreferences
import com.kheemwel.mywatchlist.data.models.FilterWatchList
import com.kheemwel.mywatchlist.data.models.Movie
import com.kheemwel.mywatchlist.data.models.Series
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

object SharedPref {
    private const val PREFS_NAME = "my_watchlist_preferences"
    private lateinit var preferences: SharedPreferences

    private const val KEY_STATUSES = "statuses"
    private const val KEY_GENRES = "genres"
    private const val KEY_COUNTRIES = "countries"
    private const val KEY_MOVIES = "movies"
    private const val KEY_SERIES = "series"
    private const val KEY_FILTER = "filter"

    fun init(context: Context) {
        preferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    }

    fun getStatuses(): List<String> {
        val data = preferences.getString(KEY_STATUSES, "")
        return if (data.isNullOrEmpty()) emptyList() else data.split(", ")
    }

    fun getGenres(): List<String> {
        val data = preferences.getString(KEY_GENRES, "")
        return if (data.isNullOrEmpty()) emptyList() else data.split(", ")
    }

    fun getCountries(): List<String> {
        val data = preferences.getString(KEY_COUNTRIES, "")
        return if (data.isNullOrEmpty()) emptyList() else data.split(", ")
    }

    fun getMovies(): List<Movie> {
        val data = preferences.getString(KEY_MOVIES, "")
        return if (data.isNullOrEmpty()) emptyList() else Json.decodeFromString<List<Movie>>(data)
    }

    fun getSeries(): List<Series> {
        val data = preferences.getString(KEY_SERIES, "")
        return if (data.isNullOrEmpty()) emptyList() else Json.decodeFromString<List<Series>>(data)
    }

    fun getFilter(): FilterWatchList {
        val data = preferences.getString(KEY_FILTER, "")
        return if (data.isNullOrEmpty()) FilterWatchList() else Json.decodeFromString<FilterWatchList>(data)
    }

    fun setStatuses(statuses: List<String>) {
        preferences.edit().putString(KEY_STATUSES, statuses.joinToString(", ")).apply()
    }

    fun setGenres(genres: List<String>) {
        preferences.edit().putString(KEY_GENRES, genres.joinToString(", ")).apply()
    }

    fun setCountries(countries: List<String>) {
        preferences.edit().putString(KEY_COUNTRIES, countries.joinToString(", ")).apply()
    }

    fun setMovies(movies: List<Movie>) {
        preferences.edit().putString(KEY_MOVIES, Json.encodeToString(movies)).apply()
    }

    fun setSeries(series: List<Series>) {
        preferences.edit().putString(KEY_SERIES, Json.encodeToString(series)).apply()
    }

    fun setFilter(filter: FilterWatchList) {
        preferences.edit().putString(KEY_FILTER, Json.encodeToString(filter)).apply()
    }
}