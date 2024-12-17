package com.kheemwel.mywatchlist.domain.usecase.movie_usecase

data class MovieUseCases(
    val getAllMoviesUseCase: GetAllMoviesUseCase,
    val getMovieUseCase: GetMovieUseCase,
    val addMovieUseCase: AddMovieUseCase,
    val addManyMoviesUseCase: AddManyMoviesUseCase,
    val updateMovieUseCase: UpdateMovieUseCase,
    val deleteMovieUseCase: DeleteMovieUseCase,
    val deleteManyMoviesUseCase: DeleteManyMoviesUseCase,
    val deleteAllMoviesUseCase: DeleteAllMoviesUseCase
)