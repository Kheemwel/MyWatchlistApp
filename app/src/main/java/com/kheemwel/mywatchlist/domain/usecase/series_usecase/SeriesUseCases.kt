package com.kheemwel.mywatchlist.domain.usecase.series_usecase

data class SeriesUseCases(
    val getAllSeriesUseCase: GetAllSeriesUseCase,
    val getSeriesUseCase: GetSeriesUseCase,
    val addSeriesUseCase: AddSeriesUseCase,
    val addManySeriesUseCase: AddManySeriesUseCase,
    val updateSeriesUseCase: UpdateSeriesUseCase,
    val deleteSeriesUseCase: DeleteSeriesUseCase,
    val deleteManySeriesUseCase: DeleteManySeriesUseCase,
    val deleteAllSeriesUseCase: DeleteAllSeriesUseCase
)