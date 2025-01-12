package com.kheemwel.mywatchlist.domain.repository

interface TransferWatchlistRepository {
    suspend fun transferMovieToSeries(id: Long)
    suspend fun transferSeriesToMovie(id: Long)
}