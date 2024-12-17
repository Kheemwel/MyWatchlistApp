package com.kheemwel.mywatchlist.domain.usecase.series_usecase

import com.kheemwel.mywatchlist.domain.model.Series
import com.kheemwel.mywatchlist.domain.repository.SeriesRepository
import javax.inject.Inject

class AddSeriesUseCase @Inject constructor(
    private val repository: SeriesRepository
) {
    @Throws()
    suspend operator fun invoke(series: Series) {
        if (series.title.isBlank()) {
            throw Exception("The series name can't be empty")
        }
        repository.addSeries(series)
    }
}