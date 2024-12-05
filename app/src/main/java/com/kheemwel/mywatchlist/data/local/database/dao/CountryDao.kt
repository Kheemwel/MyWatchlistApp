package com.kheemwel.mywatchlist.data.local.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.kheemwel.mywatchlist.data.local.database.entity.CountryEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CountryDao {
    @Query("SELECT * FROM countries")
    fun getAllCountries(): Flow<List<CountryEntity>>

    @Query("SELECT * FROM countries WHERE id = :id LIMIT 1")
    suspend fun getCountry(id: Long): CountryEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addCountry(country: CountryEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addManyCountries(countries: List<CountryEntity>): LongArray

    @Update
    suspend fun updateCountry(country: CountryEntity)

    @Query("DELETE FROM countries WHERE id = :id")
    suspend fun deleteCountry(id: Long)

    @Query("DELETE FROM countries WHERE id IN (:ids)")
    suspend fun deleteManyCountries(ids: List<Long>)

    @Query("DELETE FROM countries")
    suspend fun deleteAllCountries()
}