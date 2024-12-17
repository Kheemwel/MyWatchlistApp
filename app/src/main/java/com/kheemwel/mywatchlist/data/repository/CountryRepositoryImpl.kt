package com.kheemwel.mywatchlist.data.repository

import com.kheemwel.mywatchlist.data.local.database.dao.CountryDao
import com.kheemwel.mywatchlist.domain.model.Country
import com.kheemwel.mywatchlist.domain.repository.CountryRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class CountryRepositoryImpl @Inject constructor(
    private val dao: CountryDao
) : CountryRepository {
    override fun getAllCountries(): Flow<List<Country>> {
        return dao.getAllCountries().map { countries ->
            countries.map { it.toModel() }
        }
    }

    override suspend fun getCountry(id: Long): Country? {
        return dao.getCountry(id)?.toModel()
    }

    override suspend fun addCountry(country: Country) {
        dao.addCountry(country.toEntity())
    }

    override suspend fun addManyCountries(countries: List<Country>) {
        dao.addManyCountries(countries.map { it.toEntity() })
    }

    override suspend fun updateCountry(country: Country) {
        dao.updateCountry(country.toEntity())
    }

    override suspend fun deleteCountry(id: Long) {
        dao.deleteCountry(id)
    }

    override suspend fun deleteManyCountries(ids: List<Long>) {
        dao.deleteManyCountries(ids)
    }

    override suspend fun deleteAllCountries() {
        dao.deleteAllCountries()
    }
}