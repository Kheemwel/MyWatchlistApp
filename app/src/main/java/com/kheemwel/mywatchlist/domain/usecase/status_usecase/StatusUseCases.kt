package com.kheemwel.mywatchlist.domain.usecase.status_usecase

data class StatusUseCases(
    val getAllStatusesUseCase: GetAllStatusesUseCase,
    val getStatusUseCase: GetStatusUseCase,
    val addStatusUseCase: AddStatusUseCase,
    val addManyStatusesUseCase: AddManyStatusesUseCase,
    val updateStatusUseCase: UpdateStatusUseCase,
    val deleteStatusUseCase: DeleteStatusUseCase,
    val deleteManyStatusesUseCase: DeleteManyStatusesUseCase,
    val deleteAllStatusesUseCase: DeleteAllStatusesUseCase
)
