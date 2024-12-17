package com.kheemwel.mywatchlist.domain.usecase.status_usecase

import com.kheemwel.mywatchlist.domain.model.Status
import com.kheemwel.mywatchlist.domain.repository.StatusRepository
import javax.inject.Inject

class AddManyStatusesUseCase @Inject constructor(
    private val repository: StatusRepository
) {
    suspend operator fun invoke(statuses: List<Status>) {
        if (statuses.any { it.name.isBlank() }) {
            throw Exception("One of the status name is empty.")
        }
        repository.addManyStatuses(statuses)
    }
}