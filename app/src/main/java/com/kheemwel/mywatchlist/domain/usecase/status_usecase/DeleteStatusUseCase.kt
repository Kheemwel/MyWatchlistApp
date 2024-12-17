package com.kheemwel.mywatchlist.domain.usecase.status_usecase

import com.kheemwel.mywatchlist.domain.repository.StatusRepository
import javax.inject.Inject

class DeleteStatusUseCase @Inject constructor(
    private val repository: StatusRepository
) {
    suspend operator fun invoke(id: Long) = repository.deleteStatus(id)
}