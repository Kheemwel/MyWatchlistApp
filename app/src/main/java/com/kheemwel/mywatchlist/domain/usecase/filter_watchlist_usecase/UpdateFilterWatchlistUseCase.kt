package com.kheemwel.mywatchlist.domain.usecase.filter_watchlist_usecase

import com.kheemwel.mywatchlist.domain.model.FilterWatchlist
import com.kheemwel.mywatchlist.domain.repository.FilterWatchlistRepository
import javax.inject.Inject

class UpdateFilterWatchlistUseCase @Inject constructor(
    private val repository: FilterWatchlistRepository
) {
    suspend operator fun invoke(filter: FilterWatchlist) = repository.updateFilterWatchlist(filter)
}