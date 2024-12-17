package com.kheemwel.mywatchlist.domain.model

import com.kheemwel.mywatchlist.data.local.database.entity.GenreEntity
import kotlinx.serialization.Serializable

@Serializable
data class Genre(
    val id: Long = 0L,
    val name: String
) {
    fun toEntity(): GenreEntity = GenreEntity(id, name)
}