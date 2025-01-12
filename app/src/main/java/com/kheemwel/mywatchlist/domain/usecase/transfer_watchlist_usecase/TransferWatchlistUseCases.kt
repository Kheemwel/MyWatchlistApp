package com.kheemwel.mywatchlist.domain.usecase.transfer_watchlist_usecase

data class TransferWatchlistUseCases(
    val transferMovieToSeries: TransferMovieToSeries,
    val transferSeriesToMovie: TransferSeriesToMovie
)
