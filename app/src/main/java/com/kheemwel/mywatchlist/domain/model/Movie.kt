package com.kheemwel.mywatchlist.domain.model

import com.kheemwel.mywatchlist.data.local.database.entity.MovieEntity
import com.kheemwel.mywatchlist.data.local.database.entity.MovieWithDetails
import kotlinx.serialization.Serializable

@Serializable
data class Movie(
    val id: Long = 0L,
    val title: String,
    val status: Status? = null,
    val country: Country? = null,
    val genres: List<Genre> = emptyList(),
    val isFavorite: Boolean = false,
    val releaseDate: String = "",
    val lastModified: String = ""
) {
    fun toEntity(): MovieWithDetails {
        return MovieWithDetails(
            movie = MovieEntity(
                id = id,
                title = title,
                statusId = status?.id,
                countryId = country?.id,
                isFavorite = isFavorite,
                releaseDate = releaseDate,
                lastModified = lastModified,
            ),
            status = status?.toEntity(),
            country = country?.toEntity(),
            genres = genres.map { it.toEntity() }
        )
    }
}
