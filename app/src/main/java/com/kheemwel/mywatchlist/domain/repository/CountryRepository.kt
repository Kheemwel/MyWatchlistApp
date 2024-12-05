package com.kheemwel.mywatchlist.domain.repository

import com.kheemwel.mywatchlist.domain.model.Country
import kotlinx.coroutines.flow.Flow

interface CountryRepository {
    fun getAllCountries(): Flow<List<Country>>
    suspend fun getCountry(id: Long): Country?
    suspend fun addCountry(country: Country)
    suspend fun addManyCountries(countries: List<Country>)
    suspend fun updateCountry(country: Country)
    suspend fun deleteCountry(id: Long)
    suspend fun deleteManyCountries(ids: List<Long>)
    suspend fun deleteAllCountries()
}