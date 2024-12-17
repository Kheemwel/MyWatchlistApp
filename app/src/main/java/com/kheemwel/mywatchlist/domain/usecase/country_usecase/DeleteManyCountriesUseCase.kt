package com.kheemwel.mywatchlist.domain.usecase.country_usecase

import com.kheemwel.mywatchlist.domain.repository.CountryRepository
import javax.inject.Inject

class DeleteManyCountriesUseCase @Inject constructor(
    private val repository: CountryRepository
) {
    suspend operator fun invoke(ids: List<Long>) = repository.deleteManyCountries(ids)
}