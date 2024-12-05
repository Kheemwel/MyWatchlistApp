package com.kheemwel.mywatchlist.domain.usecase.status_usecase

import com.kheemwel.mywatchlist.domain.repository.StatusRepository
import javax.inject.Inject

class DeleteManyStatusesUseCase @Inject constructor(
    private val repository: StatusRepository
) {
    suspend operator fun invoke(ids: List<Long>) = repository.deleteManyStatuses(ids)
}