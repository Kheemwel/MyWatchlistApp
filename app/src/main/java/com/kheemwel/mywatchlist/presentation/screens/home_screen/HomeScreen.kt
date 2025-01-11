package com.kheemwel.mywatchlist.presentation.screens.home_screen

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.kheemwel.mywatchlist.R
import com.kheemwel.mywatchlist.presentation.composables.ConfirmationDialog
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    navController: NavController,
    viewModel: HomeScreenViewModel = hiltViewModel()
) {
    val state = viewModel.state.value
    val movies = state.movies
    val series = state.series
    val statuses = state.statuses
    val countries = state.countries
    val genres = state.genres
    val filterWatchlist = state.filterWatchlist

    val scope = rememberCoroutineScope()
    val pagerState = rememberPagerState(0) { 2 }

    val movieSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true, confirmValueChange = { false })
    val seriesSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true, confirmValueChange = { false })
    val filterSheetState = rememberModalBottomSheetState()

    val selectedItems = state.selectedMovies.size + state.selectedSeries.size
    val title = if (state.isSelectionMode) {
        "Selected $selectedItems ${if (selectedItems == 1) "item" else "items"}"
    } else {
        "My Watchlist"
    }

    DisposableEffect(Unit) {
        onDispose { viewModel.snackbarHostState.currentSnackbarData?.dismiss() }
    }

    BackHandler(enabled = state.isSelectionMode) {
        viewModel.onEvent(HomeScreenEvent.ToggleSelectionMode(false))
        viewModel.onEvent(HomeScreenEvent.DeselectAllMovies)
        viewModel.onEvent(HomeScreenEvent.DeselectAllSeries)
    }

    HomeScaffold(
        navController = navController,
        title = title,
        snackbarHost = { SnackbarHost(hostState = viewModel.snackbarHostState) },
        selectionMode = state.isSelectionMode,
        onDeselectAll = {
            when (pagerState.currentPage) {
                0 -> {
                    viewModel.onEvent(HomeScreenEvent.DeselectAllMovies)
                }

                1 -> {
                    viewModel.onEvent(HomeScreenEvent.DeselectAllSeries)
                }
            }
        },
        onInvertSelected = {
            when (pagerState.currentPage) {
                0 -> {
                    viewModel.onEvent(HomeScreenEvent.InvertSelectedMovies)
                }

                1 -> {
                    viewModel.onEvent(HomeScreenEvent.InvertSelectedSeries)
                }
            }
        },
        onSelectAll = {
            when (pagerState.currentPage) {
                0 -> {
                    viewModel.onEvent(HomeScreenEvent.SelectAllMovies)
                }

                1 -> {
                    viewModel.onEvent(HomeScreenEvent.SelectAllSeries)
                }
            }
        },
        enableDelete = state.isSelectionMode && selectedItems > 0,
        onDelete = { viewModel.onEvent(HomeScreenEvent.ShowDeleteSelectedDialog) },
        onCancelSelection = {
            viewModel.onEvent(HomeScreenEvent.ToggleSelectionMode(false))
            viewModel.onEvent(HomeScreenEvent.DeselectAllMovies)
            viewModel.onEvent(HomeScreenEvent.DeselectAllSeries)
        },
        showSearch = state.showSearch,
        onSearch = { viewModel.onEvent(HomeScreenEvent.EnterSearch(it)) },
        onShowSearch = { viewModel.onEvent(HomeScreenEvent.ShowSearch) },
        onHideSearch = { viewModel.onEvent(HomeScreenEvent.HideSearch) },
        searchText = state.searchText,
        isFilterActive = filterWatchlist.genres.isNotEmpty() || filterWatchlist.statuses.isNotEmpty() || filterWatchlist.countries.isNotEmpty(),
        onFilter = { viewModel.onEvent(HomeScreenEvent.ToggleShowFilterSheet) },
        onAddMovie = { viewModel.onEvent(HomeScreenEvent.ShowMovieModal()) },
        onAddSeries = { viewModel.onEvent(HomeScreenEvent.ShowSeriesModal()) }
    ) { innerPadding ->
        if (state.showMovieModal) {
            MovieSheet(
                sheetState = movieSheetState,
                error = state.error,
                editMode = state.isEditMode,
                onEditMode = { viewModel.onEvent(HomeScreenEvent.ToggleEditMode) },
                genres = genres,
                countries = countries,
                statuses = statuses,
                selectedId = state.selectedId,
                inputTitle = state.inputTitle,
                onEnterTitle = {
                    viewModel.onEvent(HomeScreenEvent.EnterTitle(it))
                },
                inputStatus = state.inputStatus,
                onSelectStatus = {
                    viewModel.onEvent(HomeScreenEvent.SelectStatus(it))
                },
                inputCountry = state.inputCountry,
                onSelectCountry = {
                    viewModel.onEvent(HomeScreenEvent.SelectCountry(it))
                },
                inputGenres = state.inputGenres,
                onSelectGenre = {
                    viewModel.onEvent(HomeScreenEvent.SelectGenre(it))
                },
                inputReleaseDate = state.inputReleaseDate,
                onEnterReleaseDate = {
                    viewModel.onEvent(HomeScreenEvent.EnterReleaseDate(it))
                },
                inputFavorite = state.inputFavorite,
                onToggleFavorite = { viewModel.onEvent(HomeScreenEvent.ToggleFavorite) },
                onSwitchToSeries = { viewModel.onEvent(HomeScreenEvent.SwitchMovieModalToSeriesModal) },
                onSave = {
                    if (state.selectedId != null) {
                        viewModel.onEvent(
                            HomeScreenEvent.UpdateMovie(
                                id = state.selectedId,
                                newTitle = state.inputTitle.trim(),
                                newStatus = state.inputStatus,
                                newCountry = state.inputCountry,
                                newGenres = state.inputGenres,
                                isFavorite = state.inputFavorite,
                                newReleaseDate = state.inputReleaseDate
                            )
                        )
                    } else {
                        viewModel.onEvent(
                            HomeScreenEvent.AddMovie(
                                title = state.inputTitle.trim(),
                                status = state.inputStatus,
                                country = state.inputCountry,
                                genres = state.inputGenres,
                                isFavorite = state.inputFavorite,
                                releaseDate = state.inputReleaseDate
                            )
                        )
                    }
                },
                onDelete = { state.selectedId?.let { viewModel.onEvent(HomeScreenEvent.DeleteMovie(it)) } },
                onDismiss = { viewModel.onEvent(HomeScreenEvent.HideMovieModal) }
            )
        }

        if (state.showSeriesModal) {
            SeriesSheet(
                sheetState = seriesSheetState,
                error = state.error,
                editMode = state.isEditMode,
                onEditMode = { viewModel.onEvent(HomeScreenEvent.ToggleEditMode) },
                genres = genres,
                countries = countries,
                statuses = statuses,
                selectedId = state.selectedId,
                inputTitle = state.inputTitle,
                onEnterTitle = {
                    viewModel.onEvent(HomeScreenEvent.EnterTitle(it))
                },
                inputSeason = state.inputSeason,
                onEnterSeason = {
                    viewModel.onEvent(HomeScreenEvent.EnterSeason(it))
                },
                inputEpisode = state.inputEpisode,
                onEnterEpisode = {
                    viewModel.onEvent(HomeScreenEvent.EnterEpisode(it))
                },
                inputStatus = state.inputStatus,
                onSelectStatus = {
                    viewModel.onEvent(HomeScreenEvent.SelectStatus(it))
                },
                inputCountry = state.inputCountry,
                onSelectCountry = {
                    viewModel.onEvent(HomeScreenEvent.SelectCountry(it))
                },
                inputGenres = state.inputGenres,
                onSelectGenre = {
                    viewModel.onEvent(HomeScreenEvent.SelectGenre(it))
                },
                inputReleaseDate = state.inputReleaseDate,
                onEnterReleaseDate = {
                    viewModel.onEvent(HomeScreenEvent.EnterReleaseDate(it))
                },
                inputFavorite = state.inputFavorite,
                onToggleFavorite = { viewModel.onEvent(HomeScreenEvent.ToggleFavorite) },
                onSwitchToMovie = { viewModel.onEvent(HomeScreenEvent.SwitchSeriesModalToMovieModal) },
                onSave = {
                    if (state.selectedId != null) {
                        viewModel.onEvent(
                            HomeScreenEvent.UpdateSeries(
                                id = state.selectedId,
                                newTitle = state.inputTitle.trim(),
                                newSeason = state.inputSeason,
                                newEpisode = state.inputEpisode,
                                newStatus = state.inputStatus,
                                newCountry = state.inputCountry,
                                newGenres = state.inputGenres,
                                isFavorite = state.inputFavorite,
                                newReleaseDate = state.inputReleaseDate
                            )
                        )
                    } else {
                        viewModel.onEvent(
                            HomeScreenEvent.AddSeries(
                                title = state.inputTitle.trim(),
                                season = state.inputSeason,
                                episode = state.inputEpisode,
                                status = state.inputStatus,
                                country = state.inputCountry,
                                genres = state.inputGenres,
                                isFavorite = state.inputFavorite,
                                releaseDate = state.inputReleaseDate
                            )
                        )
                    }

                    if (state.error == null) {
                        viewModel.onEvent(HomeScreenEvent.HideSeriesModal)
                    }
                },
                onDelete = { state.selectedId?.let { viewModel.onEvent(HomeScreenEvent.DeleteSeries(it)) } },
                onDismiss = { viewModel.onEvent(HomeScreenEvent.HideSeriesModal) }
            )
        }

        ConfirmationDialog(
            state = state.showDeleteMovieDialog,
            title = "Delete Movie",
            message = "Are you sure you want to delete the movie \"${state.inputTitle}\"?",
            onDismiss = { viewModel.onEvent(HomeScreenEvent.HideDeleteMovieDialog) },
            onCancelText = "Cancel",
            onCancel = { viewModel.onEvent(HomeScreenEvent.HideDeleteMovieDialog) },
            onConfirmText = "Ok"
        ) {
            state.selectedId?.let { viewModel.onEvent(HomeScreenEvent.DeleteMovie(it)) }
            viewModel.onEvent(HomeScreenEvent.HideDeleteMovieDialog)
        }

        ConfirmationDialog(
            state = state.showDeleteSeriesDialog,
            title = "Delete Series",
            message = "Are you sure you want to delete the series \"${state.inputTitle}\"?",
            onDismiss = { viewModel.onEvent(HomeScreenEvent.HideDeleteSeriesDialog) },
            onCancelText = "Cancel",
            onCancel = { viewModel.onEvent(HomeScreenEvent.HideDeleteSeriesDialog) },
            onConfirmText = "Ok"
        ) {
            state.selectedId?.let { viewModel.onEvent(HomeScreenEvent.DeleteSeries(it)) }
            viewModel.onEvent(HomeScreenEvent.HideDeleteSeriesDialog)
        }

        ConfirmationDialog(
            state = state.showDeleteSelectedDialog,
            title = "Delete $selectedItems ${if (selectedItems == 1) "item" else "items"}",
            message = "Are you sure you want to delete ${if (selectedItems == 1) "this selected item" else "these selected items"}?",
            onDismiss = { viewModel.onEvent(HomeScreenEvent.HideDeleteSelectedDialog) },
            onCancelText = "Cancel",
            onCancel = { viewModel.onEvent(HomeScreenEvent.HideDeleteSelectedDialog) },
            onConfirmText = "Ok"
        ) {
            viewModel.onEvent(HomeScreenEvent.DeleteAllSelected)
            viewModel.onEvent(HomeScreenEvent.DeselectAllMovies)
            viewModel.onEvent(HomeScreenEvent.DeselectAllSeries)
            viewModel.onEvent(HomeScreenEvent.HideDeleteSelectedDialog)
        }

        if (state.showFilterSheet) {
            FilterSheet(
                filterSheetState,
                filterWatchlist,
                genres.map { it.name },
                countries.map { it.name },
                statuses.map { it.name },
                onDismiss = { viewModel.onEvent(HomeScreenEvent.ToggleShowFilterSheet) }
            ) {
                viewModel.onEvent(HomeScreenEvent.UpdateFilter(it))
            }
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            TabRow(selectedTabIndex = pagerState.currentPage,
                indicator = {
                    Row(
                        modifier = Modifier.tabIndicatorOffset(it[pagerState.currentPage]),
                        horizontalArrangement = Arrangement.Center,
                    ) {
                        Box(
                            modifier = Modifier
                                .height(3.dp)
                                .width(80.dp)
                                .background(
                                    MaterialTheme.colorScheme.primary,
                                    RoundedCornerShape(8.dp)
                                )
                        )
                    }
                }
            ) {
                HomePrimaryTab(
                    title = "Movie",
                    badge = if (movies.size > 999) "999+" else movies.size.toString(),
                    icon = {
                        Icon(
                            painterResource(R.drawable.baseline_movie_24),
                            contentDescription = "Movie"
                        )
                    },
                    selected = pagerState.currentPage == 0
                ) {
                    scope.launch {
                        pagerState.animateScrollToPage(0)
                    }
                }

                HomePrimaryTab(
                    title = "Series",
                    badge = if (series.size > 999) "999+" else series.size.toString(),
                    icon = {
                        Icon(
                            painterResource(R.drawable.baseline_live_tv_24),
                            contentDescription = "Series"
                        )
                    },
                    selected = pagerState.currentPage == 1
                ) {
                    scope.launch {
                        pagerState.animateScrollToPage(1)
                    }
                }
            }

            if (state.isLoading) {
                LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
            }

            HorizontalPager(
                state = pagerState,
                modifier = Modifier.weight(1f),
                verticalAlignment = Alignment.Top
            ) { page ->
                when (page) {
                    0 -> MovieTab(
                        movies = movies,
                        selectionMode = state.isSelectionMode,
                        enableSelectionMode = { viewModel.onEvent(HomeScreenEvent.ToggleSelectionMode(true)) },
                        selectedItems = state.selectedMovies,
                        onItemSelected = {
                            viewModel.onEvent(HomeScreenEvent.ToggleSelectedMovie(it))
                        },
                        onToggleFavorite = {
                            viewModel.onEvent(HomeScreenEvent.ToggleFavoriteMovie(it))
                        },
                        onView = {
                            viewModel.onEvent(
                                HomeScreenEvent.ShowMovieModal(
                                    editMode = false,
                                    id = it.id,
                                    title = it.title,
                                    status = it.status,
                                    country = it.country,
                                    genres = it.genres,
                                    isFavorite = it.isFavorite,
                                    releaseDate = it.releaseDate
                                )
                            )
                        },
                        onEdit = {
                            viewModel.onEvent(
                                HomeScreenEvent.ShowMovieModal(
                                    editMode = true,
                                    id = it.id,
                                    title = it.title,
                                    status = it.status,
                                    country = it.country,
                                    genres = it.genres,
                                    isFavorite = it.isFavorite,
                                    releaseDate = it.releaseDate
                                )
                            )
                        },
                        onDelete = {
                            viewModel.onEvent(HomeScreenEvent.ShowDeleteMovieDialog(it.id, it.title))
                        },
                    )

                    1 -> SeriesTab(
                        series = series,
                        selectionMode = state.isSelectionMode,
                        enableSelectionMode = { viewModel.onEvent(HomeScreenEvent.ToggleSelectionMode(true)) },
                        selectedItems = state.selectedSeries,
                        onItemSelected = {
                            viewModel.onEvent(HomeScreenEvent.ToggleSelectedSeries(it))
                        },
                        onToggleFavorite = {
                            viewModel.onEvent(HomeScreenEvent.ToggleFavoriteSeries(it))
                        },
                        onView = {
                            viewModel.onEvent(
                                HomeScreenEvent.ShowSeriesModal(
                                    editMode = false,
                                    id = it.id,
                                    title = it.title,
                                    season = it.season,
                                    episode = it.episode,
                                    status = it.status,
                                    country = it.country,
                                    genres = it.genres,
                                    isFavorite = it.isFavorite,
                                    releaseDate = it.releaseDate
                                )
                            )
                        },
                        onEdit = {
                            viewModel.onEvent(
                                HomeScreenEvent.ShowSeriesModal(
                                    editMode = true,
                                    id = it.id,
                                    title = it.title,
                                    season = it.season,
                                    episode = it.episode,
                                    status = it.status,
                                    country = it.country,
                                    genres = it.genres,
                                    isFavorite = it.isFavorite,
                                    releaseDate = it.releaseDate
                                )
                            )
                        },
                        onDelete = {
                            viewModel.onEvent(HomeScreenEvent.ShowDeleteSeriesDialog(it.id, it.title))
                        }
                    )
                }
            }
        }
    }
}