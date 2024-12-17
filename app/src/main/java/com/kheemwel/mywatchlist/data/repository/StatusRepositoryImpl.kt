package com.kheemwel.mywatchlist.data.repository

import com.kheemwel.mywatchlist.data.local.database.dao.StatusDao
import com.kheemwel.mywatchlist.domain.model.Status
import com.kheemwel.mywatchlist.domain.repository.StatusRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class StatusRepositoryImpl @Inject constructor(
    private val dao: StatusDao
) : StatusRepository {
    override fun getAllStatuses(): Flow<List<Status>> {
        return dao.getAllStatuses().map { statuses ->
            statuses.map { it.toModel() }
        }
    }

    override suspend fun getStatus(id: Long): Status? {
        return dao.getStatus(id)?.toModel()
    }

    override suspend fun addStatus(status: Status) {
        dao.addStatus(status.toEntity())
    }

    override suspend fun addManyStatuses(statuses: List<Status>) {
        dao.addManyStatuses(statuses.map { it.toEntity() })
    }

    override suspend fun updateStatus(status: Status) {
        dao.updateStatus(status.toEntity())
    }

    override suspend fun deleteStatus(id: Long) {
        dao.deleteStatus(id)
    }

    override suspend fun deleteManyStatuses(ids: List<Long>) {
        dao.deleteManyStatuses(ids)
    }

    override suspend fun deleteAllStatuses() {
        dao.deleteAllStatuses()
    }
}