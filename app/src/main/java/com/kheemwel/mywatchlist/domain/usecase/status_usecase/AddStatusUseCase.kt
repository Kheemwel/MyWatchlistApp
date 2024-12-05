package com.kheemwel.mywatchlist.domain.usecase.status_usecase

import com.kheemwel.mywatchlist.domain.model.Status
import com.kheemwel.mywatchlist.domain.repository.StatusRepository
import javax.inject.Inject

class AddStatusUseCase @Inject constructor(
    private val repository: StatusRepository
) {
    @Throws()
    suspend operator fun invoke(status: Status) {
        if (status.name.isBlank()) {
            throw Exception("The status name can't be empty")
        }
        repository.addStatus(status)
    }
}