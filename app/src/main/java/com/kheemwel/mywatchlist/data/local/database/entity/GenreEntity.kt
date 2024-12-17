package com.kheemwel.mywatchlist.data.local.database.entity

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.kheemwel.mywatchlist.domain.model.Genre

@Entity(tableName = "genres", indices = [Index("name", unique = true)])
data class GenreEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,
    val name: String
) {
    fun toModel(): Genre = Genre(id, name)
}
