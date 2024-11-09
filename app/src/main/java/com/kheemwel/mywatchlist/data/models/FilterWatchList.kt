package com.kheemwel.mywatchlist.data.models


enum class FilterSortBy {
    Title,
    ReleaseDate,
    LastModified,
}

enum class FilterSortDirection {
    Ascending,
    Descending,
}

data class FilterWatchList(
    val statuses: List<String> = emptyList(),
    val genres: List<String> = emptyList(),
    val countries: List<String> = emptyList(),
    val sortBy: FilterSortBy = FilterSortBy.LastModified,
    val sortDirection: FilterSortDirection = FilterSortDirection.Descending
)
