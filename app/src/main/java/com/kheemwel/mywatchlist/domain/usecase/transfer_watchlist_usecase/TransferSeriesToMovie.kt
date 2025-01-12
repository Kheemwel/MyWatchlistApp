package com.kheemwel.mywatchlist.domain.usecase.transfer_watchlist_usecase

import com.kheemwel.mywatchlist.domain.repository.TransferWatchlistRepository
import javax.inject.Inject

class TransferSeriesToMovie @Inject constructor(
    private val repository: TransferWatchlistRepository
) {
    suspend operator fun invoke(id: Long) {
        repository.transferSeriesToMovie(id)
    }
}