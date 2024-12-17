package com.kheemwel.mywatchlist.domain.usecase.country_usecase

import com.kheemwel.mywatchlist.domain.model.Country
import com.kheemwel.mywatchlist.domain.repository.CountryRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetAllCountriesUseCase @Inject constructor(
    private val repository: CountryRepository
) {
    operator fun invoke(): Flow<List<Country>> = repository.getAllCountries()
}