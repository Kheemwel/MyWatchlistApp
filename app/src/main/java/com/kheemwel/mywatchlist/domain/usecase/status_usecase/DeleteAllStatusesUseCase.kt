package com.kheemwel.mywatchlist.domain.usecase.status_usecase

import com.kheemwel.mywatchlist.domain.repository.StatusRepository
import javax.inject.Inject

class DeleteAllStatusesUseCase @Inject constructor(
    private val repository: StatusRepository
) {
    suspend operator fun invoke() = repository.deleteAllStatuses()
}