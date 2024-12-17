package com.kheemwel.mywatchlist.domain.usecase.status_usecase

import com.kheemwel.mywatchlist.domain.model.Status
import com.kheemwel.mywatchlist.domain.repository.StatusRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetAllStatusesUseCase @Inject constructor(
    private val repository: StatusRepository
) {
    operator fun invoke(): Flow<List<Status>> = repository.getAllStatuses()
}