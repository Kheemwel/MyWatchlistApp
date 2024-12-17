package com.kheemwel.mywatchlist.domain.usecase.series_usecase

import com.kheemwel.mywatchlist.domain.model.Series
import com.kheemwel.mywatchlist.domain.repository.SeriesRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetAllSeriesUseCase @Inject constructor(
    private val repository: SeriesRepository
) {
    operator fun invoke(): Flow<List<Series>> = repository.getAllSeries()
}