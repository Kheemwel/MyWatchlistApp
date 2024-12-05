package com.kheemwel.mywatchlist.domain.usecase.country_usecase

import com.kheemwel.mywatchlist.domain.repository.CountryRepository
import javax.inject.Inject

class DeleteCountryUseCase @Inject constructor(
    private val repository: CountryRepository
) {
    suspend operator fun invoke(id: Long) = repository.deleteCountry(id)
}