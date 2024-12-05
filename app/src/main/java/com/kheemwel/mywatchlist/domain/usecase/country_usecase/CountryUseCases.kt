package com.kheemwel.mywatchlist.domain.usecase.country_usecase

data class CountryUseCases(
    val getAllCountriesUseCase: GetAllCountriesUseCase,
    val getCountryUseCase: GetCountryUseCase,
    val addCountryUseCase: AddCountryUseCase,
    val addManyCountriesUseCase: AddManyCountriesUseCase,
    val updateCountryUseCase: UpdateCountryUseCase,
    val deleteCountryUseCase: DeleteCountryUseCase,
    val deleteManyCountriesUseCase: DeleteManyCountriesUseCase,
    val deleteAllCountriesUseCase: DeleteAllCountriesUseCase
)
