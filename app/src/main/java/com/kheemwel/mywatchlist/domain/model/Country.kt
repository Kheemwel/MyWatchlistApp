package com.kheemwel.mywatchlist.domain.model

import com.kheemwel.mywatchlist.data.local.database.entity.CountryEntity
import kotlinx.serialization.Serializable

@Serializable
data class Country(
    val id: Long = 0L,
    val name: String
) {
    fun toEntity(): CountryEntity = CountryEntity(id, name)
}
