package com.kheemwel.mywatchlist.data.repository

import com.kheemwel.mywatchlist.data.local.datastore.manager.FilterWatchlistManager
import com.kheemwel.mywatchlist.domain.model.FilterSortBy
import com.kheemwel.mywatchlist.domain.model.FilterSortDirection
import com.kheemwel.mywatchlist.domain.model.FilterWatchlist
import com.kheemwel.mywatchlist.domain.repository.FilterWatchlistRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import com.kheemwel.mywatchlist.FilterSortBy as SortBy
import com.kheemwel.mywatchlist.FilterSortDirection as SortDirection
import com.kheemwel.mywatchlist.FilterWatchlist as Filter

class FilterWatchlistRepositoryImpl @Inject constructor(
    private val manager: FilterWatchlistManager
) : FilterWatchlistRepository {
    override fun getFilterWatchlist(): Flow<FilterWatchlist> {
        return manager.getFilterWatchlist().map {
            FilterWatchlist(
                statuses = it.statusesList,
                genres = it.genresList,
                countries = it.countriesList,
                sortBy = FilterSortBy.entries[it.sortBy.ordinal],
                sortDirection = FilterSortDirection.entries[it.sortDirection.ordinal]
            )
        }
    }

    override suspend fun updateFilterWatchlist(filterWatchlist: FilterWatchlist) {
        manager.updateFilterWatchlist(
            Filter.newBuilder()
                .addAllStatuses(filterWatchlist.statuses)
                .addAllGenres(filterWatchlist.genres)
                .addAllCountries(filterWatchlist.countries)
                .setSortBy(SortBy.forNumber(filterWatchlist.sortBy.ordinal))
                .setSortDirection(SortDirection.forNumber(filterWatchlist.sortDirection.ordinal))
                .build()
        )
    }
}