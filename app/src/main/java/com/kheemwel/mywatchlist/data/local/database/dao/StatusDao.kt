package com.kheemwel.mywatchlist.data.local.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.kheemwel.mywatchlist.data.local.database.entity.StatusEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface StatusDao {
    @Query("SELECT * FROM statuses")
    fun getAllStatuses(): Flow<List<StatusEntity>>

    @Query("SELECT * FROM statuses WHERE id = :id LIMIT 1")
    suspend fun getStatus(id: Long): StatusEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addStatus(status: StatusEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addManyStatuses(statuses: List<StatusEntity>): LongArray

    @Update
    suspend fun updateStatus(status: StatusEntity)

    @Query("DELETE FROM statuses WHERE id = :id")
    suspend fun deleteStatus(id: Long)

    @Query("DELETE FROM statuses WHERE id IN (:ids)")
    suspend fun deleteManyStatuses(ids: List<Long>)

    @Query("DELETE FROM statuses")
    suspend fun deleteAllStatuses()
}