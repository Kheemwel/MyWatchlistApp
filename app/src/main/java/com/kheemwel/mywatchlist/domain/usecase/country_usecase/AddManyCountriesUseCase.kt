package com.kheemwel.mywatchlist.domain.usecase.country_usecase

import com.kheemwel.mywatchlist.domain.model.Country
import com.kheemwel.mywatchlist.domain.repository.CountryRepository
import javax.inject.Inject

class AddManyCountriesUseCase @Inject constructor(
    private val repository: CountryRepository
) {
    suspend operator fun invoke(countries: List<Country>) {
        if (countries.any { it.name.isBlank() }) {
            throw Exception("One of the country name is empty.")
        }
        repository.addManyCountries(countries)
    }
}