package com.kheemwel.mywatchlist.domain.repository

import com.kheemwel.mywatchlist.domain.model.FilterWatchlist
import kotlinx.coroutines.flow.Flow

interface FilterWatchlistRepository {
    fun getFilterWatchlist(): Flow<FilterWatchlist>
    suspend fun updateFilterWatchlist(filterWatchlist: FilterWatchlist)
}