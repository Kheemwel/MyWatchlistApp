package com.kheemwel.mywatchlist.domain.usecase.country_usecase

import com.kheemwel.mywatchlist.domain.model.Country
import com.kheemwel.mywatchlist.domain.repository.CountryRepository
import javax.inject.Inject

class UpdateCountryUseCase @Inject constructor(
    private val repository: CountryRepository
) {
    suspend operator fun invoke(country: Country) = repository.updateCountry(country)
}