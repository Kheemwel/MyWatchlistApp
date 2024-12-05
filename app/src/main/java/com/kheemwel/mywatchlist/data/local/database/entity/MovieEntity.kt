package com.kheemwel.mywatchlist.data.local.database.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "movies",
    foreignKeys = [
        ForeignKey(
            entity = StatusEntity::class,
            parentColumns = ["id"],
            childColumns = ["statusId"],
            onDelete = ForeignKey.SET_NULL
        ),
        ForeignKey(
            entity = CountryEntity::class,
            parentColumns = ["id"],
            childColumns = ["countryId"],
            onDelete = ForeignKey.SET_NULL
        )
    ],
    indices = [Index(value = ["statusId"]), Index(value = ["countryId"]), Index("title", unique = true)]
)

data class MovieEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,
    val title: String,
    val statusId: Long?,
    val countryId: Long?,
    val isFavorite: Boolean = false,
    val releaseDate: String = "",
    val lastModified: String = ""
)