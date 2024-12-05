package com.kheemwel.mywatchlist.domain.usecase.status_usecase

import com.kheemwel.mywatchlist.domain.model.Status
import com.kheemwel.mywatchlist.domain.repository.StatusRepository
import javax.inject.Inject

class UpdateStatusUseCase @Inject constructor(
    private val repository: StatusRepository
) {
    suspend operator fun invoke(status: Status) = repository.updateStatus(status)
}