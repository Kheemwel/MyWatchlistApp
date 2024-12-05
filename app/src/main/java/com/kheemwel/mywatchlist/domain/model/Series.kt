package com.kheemwel.mywatchlist.domain.model

import com.kheemwel.mywatchlist.data.local.database.entity.SeriesEntity
import com.kheemwel.mywatchlist.data.local.database.entity.SeriesWithDetails
import kotlinx.serialization.Serializable

@Serializable
data class Series(
    val id: Long = 0L,
    val title: String,
    val season: Int = 0,
    val episode: Int = 0,
    val status: Status? = null,
    val country: Country? = null,
    val genres: List<Genre> = emptyList(),
    val isFavorite: Boolean = false,
    val releaseDate: String = "",
    val lastModified: String = ""
) {
    fun toEntity(): SeriesWithDetails {
        return SeriesWithDetails(
            series = SeriesEntity(
                id = id,
                title = title,
                season = season,
                episode = episode,
                statusId = status?.id,
                countryId = country?.id,
                isFavorite = isFavorite,
                releaseDate = releaseDate,
                lastModified = lastModified
            ),
            status = status?.toEntity(),
            country = country?.toEntity(),
            genres = genres.map { it.toEntity() }
        )
    }
}
