package com.kheemwel.mywatchlist.domain.usecase.genre_usecase

data class GenreUseCases(
    val getAllGenresUseCase: GetAllGenresUseCase,
    val getGenreUseCase: GetGenreUseCase,
    val addGenreUseCase: AddGenreUseCase,
    val addManyGenresUseCase: AddManyGenresUseCase,
    val updateGenreUseCase: UpdateGenreUseCase,
    val deleteGenreUseCase: DeleteGenreUseCase,
    val deleteManyGenresUseCase: DeleteManyGenresUseCase,
    val deleteAllGenresUseCase: DeleteAllGenresUseCase
)