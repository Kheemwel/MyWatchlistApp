package com.kheemwel.mywatchlist.domain.usecase.series_usecase

import com.kheemwel.mywatchlist.domain.repository.SeriesRepository
import javax.inject.Inject

class DeleteSeriesUseCase @Inject constructor(
    private val repository: SeriesRepository
) {
    suspend operator fun invoke(id: Long) = repository.deleteSeries(id)
}