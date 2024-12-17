package com.kheemwel.mywatchlist.domain.repository

import com.kheemwel.mywatchlist.domain.model.Status
import kotlinx.coroutines.flow.Flow

interface StatusRepository {
    fun getAllStatuses(): Flow<List<Status>>
    suspend fun getStatus(id: Long): Status?
    suspend fun addStatus(status: Status)
    suspend fun addManyStatuses(statuses: List<Status>)
    suspend fun updateStatus(status: Status)
    suspend fun deleteStatus(id: Long)
    suspend fun deleteManyStatuses(ids: List<Long>)
    suspend fun deleteAllStatuses()
}