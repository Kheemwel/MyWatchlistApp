package com.kheemwel.mywatchlist.data.local.database.entity

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import com.kheemwel.mywatchlist.domain.model.Series

data class SeriesWithDetails(
    @Embedded
    val series: SeriesEntity,
    @Relation(
        parentColumn = "statusId",
        entityColumn = "id"
    )
    val status: StatusEntity?,
    @Relation(
        parentColumn = "countryId",
        entityColumn = "id"
    )
    val country: CountryEntity?,
    @Relation(
        parentColumn = "id",
        entityColumn = "id",
        associateBy = Junction(SeriesGenresCrossRef::class, parentColumn = "seriesId", entityColumn = "genreId")
    )
    val genres: List<GenreEntity>
) {
    fun toModel(): Series {
        return Series(
            id = series.id,
            title = series.title,
            season = series.season,
            episode = series.episode,
            status = status?.toModel(),
            country = country?.toModel(),
            genres = genres.map { it.toModel() },
            isFavorite = series.isFavorite,
            releaseDate = series.releaseDate,
            lastModified = series.lastModified
        )
    }
}