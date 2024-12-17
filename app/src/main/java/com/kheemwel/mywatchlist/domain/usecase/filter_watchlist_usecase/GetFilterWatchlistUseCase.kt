package com.kheemwel.mywatchlist.domain.usecase.filter_watchlist_usecase

import com.kheemwel.mywatchlist.domain.model.FilterWatchlist
import com.kheemwel.mywatchlist.domain.repository.FilterWatchlistRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetFilterWatchlistUseCase @Inject constructor(
    private val repository: FilterWatchlistRepository
) {
    operator fun invoke(): Flow<FilterWatchlist> = repository.getFilterWatchlist()
}