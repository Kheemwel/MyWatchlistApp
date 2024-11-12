package com.kheemwel.mywatchlist.data.models

import kotlinx.serialization.Serializable

enum class FilterSortBy {
    Title,
    ReleaseDate,
    LastModified,
    Favorite
}

enum class FilterSortDirection {
    Ascending,
    Descending,
}

@Serializable
data class FilterWatchList(
    val statuses: List<String> = emptyList(),
    val genres: List<String> = emptyList(),
    val countries: List<String> = emptyList(),
    val sortBy: FilterSortBy = FilterSortBy.LastModified,
    val sortDirection: FilterSortDirection = FilterSortDirection.Descending
)
