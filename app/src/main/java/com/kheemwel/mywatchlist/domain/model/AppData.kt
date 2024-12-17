package com.kheemwel.mywatchlist.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class AppData(
    val backupVersion: Long = 1,
    val statuses: List<Status> = emptyList(),
    val countries: List<Country> = emptyList(),
    val genres: List<Genre> = emptyList(),
    val movies: List<Movie> = emptyList(),
    val series: List<Series> = emptyList()
)
