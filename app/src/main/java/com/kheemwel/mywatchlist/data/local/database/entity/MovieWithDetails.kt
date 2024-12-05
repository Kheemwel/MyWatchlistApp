package com.kheemwel.mywatchlist.data.local.database.entity

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import com.kheemwel.mywatchlist.domain.model.Movie

data class MovieWithDetails(
    @Embedded
    val movie: MovieEntity,
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
        associateBy = Junction(MovieGenresCrossRef::class, parentColumn = "movieId", entityColumn = "genreId")
    )
    val genres: List<GenreEntity>
) {
    fun toModel(): Movie {
        return Movie(
            id = movie.id,
            title = movie.title,
            status = status?.toModel(),
            country = country?.toModel(),
            genres = genres.map { it.toModel() },
            isFavorite = movie.isFavorite,
            releaseDate = movie.releaseDate,
            lastModified = movie.lastModified
        )
    }
}
