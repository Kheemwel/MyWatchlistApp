package com.kheemwel.mywatchlist.domain.model

import com.kheemwel.mywatchlist.data.local.database.entity.StatusEntity
import kotlinx.serialization.Serializable

@Serializable
data class Status(
    val id: Long = 0L,
    val name: String
) {
    fun toEntity(): StatusEntity = StatusEntity(id, name)
}
