package com.kheemwel.mywatchlist.domain.usecase.series_usecase

import com.kheemwel.mywatchlist.domain.model.Series
import com.kheemwel.mywatchlist.domain.repository.SeriesRepository
import javax.inject.Inject

class UpdateSeriesUseCase @Inject constructor(
    private val repository: SeriesRepository
) {
    suspend operator fun invoke(series: Series) = repository.updateSeries(series)
}