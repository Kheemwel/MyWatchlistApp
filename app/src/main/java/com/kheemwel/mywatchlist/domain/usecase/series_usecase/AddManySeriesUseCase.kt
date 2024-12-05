package com.kheemwel.mywatchlist.domain.usecase.series_usecase

import com.kheemwel.mywatchlist.domain.model.Series
import com.kheemwel.mywatchlist.domain.repository.SeriesRepository
import javax.inject.Inject

class AddManySeriesUseCase @Inject constructor(
    private val repository: SeriesRepository
) {
    suspend operator fun invoke(series: List<Series>) {
        if (series.any { it.title.isBlank() }) {
            throw Exception("One of the series name is empty.")
        }
        repository.addManySeries(series)
    }
}