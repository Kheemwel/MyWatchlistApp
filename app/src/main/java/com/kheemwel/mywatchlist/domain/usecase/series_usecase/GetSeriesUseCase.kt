package com.kheemwel.mywatchlist.domain.usecase.series_usecase

import com.kheemwel.mywatchlist.domain.model.Series
import com.kheemwel.mywatchlist.domain.repository.SeriesRepository
import javax.inject.Inject

class GetSeriesUseCase @Inject constructor(
    private val repository: SeriesRepository
) {
    suspend operator fun invoke(id: Long): Series? = repository.getSeries(id)
}