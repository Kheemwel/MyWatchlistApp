package com.kheemwel.mywatchlist.domain.usecase.country_usecase

import com.kheemwel.mywatchlist.domain.model.Country
import com.kheemwel.mywatchlist.domain.repository.CountryRepository
import javax.inject.Inject

class AddCountryUseCase @Inject constructor(
    private val repository: CountryRepository
) {
    @Throws()
    suspend operator fun invoke(country: Country) {
        if (country.name.isBlank()) {
            throw Exception("The country name can't be empty")
        }
        repository.addCountry(country)
    }
}