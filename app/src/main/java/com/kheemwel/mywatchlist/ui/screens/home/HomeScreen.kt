package com.kheemwel.mywatchlist.ui.screens.home

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.kheemwel.mywatchlist.R
import com.kheemwel.mywatchlist.data.database.SharedPref
import com.kheemwel.mywatchlist.data.models.CountryModel
import com.kheemwel.mywatchlist.data.models.FilterSortBy
import com.kheemwel.mywatchlist.data.models.FilterSortDirection
import com.kheemwel.mywatchlist.data.models.GenreModel
import com.kheemwel.mywatchlist.data.models.Movie
import com.kheemwel.mywatchlist.data.models.MovieModel
import com.kheemwel.mywatchlist.data.models.Series
import com.kheemwel.mywatchlist.data.models.SeriesModel
import com.kheemwel.mywatchlist.data.models.StatusModel
import com.kheemwel.mywatchlist.ui.composables.ConfirmationDialog
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    navController: NavController,
    movieModel: MovieModel,
    seriesModel: SeriesModel,
    statusModel: StatusModel,
    genreModel: GenreModel,
    countryModel: CountryModel
) {
    val statuses = statusModel.statuses.collectAsState()
    val genres = genreModel.genres.collectAsState()
    val countries = countryModel.countries.collectAsState()

    var searchText by remember { mutableStateOf("") }
    var showFilter by remember { mutableStateOf(false) }
    var filter by remember { mutableStateOf(SharedPref.getFilter()) }
    val filterState = rememberModalBottomSheetState()

    val scope = rememberCoroutineScope()
    val pagerState = rememberPagerState(0) { 2 }

    var showAddMovie by remember { mutableStateOf(false) }
    var showViewMovie by remember { mutableStateOf(false) }
    var showAddSeries by remember { mutableStateOf(false) }
    var showViewSeries by remember { mutableStateOf(false) }

    var selectedMovie by remember { mutableStateOf<Movie?>(null) }
    var selectedSeries by remember { mutableStateOf<Series?>(null) }

    val addMovieState =
        rememberModalBottomSheetState(skipPartiallyExpanded = true, confirmValueChange = { false })
    val viewMovieState =
        rememberModalBottomSheetState(skipPartiallyExpanded = true, confirmValueChange = { false })
    val addSeriesState =
        rememberModalBottomSheetState(skipPartiallyExpanded = true, confirmValueChange = { false })
    val viewSeriesState =
        rememberModalBottomSheetState(skipPartiallyExpanded = true, confirmValueChange = { false })

    var showDeleteMovieDialog by remember { mutableStateOf(false) }
    var showDeleteSeriesDialog by remember { mutableStateOf(false) }
    var editMode by remember { mutableStateOf(false) }

    val moviesCollection = movieModel.movies.collectAsState()
    val filteredMovies = moviesCollection.value.filter { movie ->
        // Search Text Filter
        (searchText.isEmpty() ||
                movie.title.contains(searchText, ignoreCase = true) ||
                movie.genres.any { it.contains(searchText, ignoreCase = true) } ||
                movie.status.contains(searchText, ignoreCase = true) ||
                movie.country.contains(searchText, ignoreCase = true)) &&
                // Genres Filter
                (filter.genres.isEmpty() || movie.genres.any { filter.genres.contains(it) }) &&
                // Status Filter
                (filter.statuses.isEmpty() || filter.statuses.contains(movie.status)) &&
                // Countries Filter
                (filter.countries.isEmpty() || filter.countries.contains(movie.country))
    }.let { filtered ->
        // Sorting
        if (filter.sortDirection == FilterSortDirection.Ascending) {
            filtered.sortedBy {
                when (filter.sortBy) {
                    FilterSortBy.Title -> it.title
                    FilterSortBy.ReleaseDate -> it.releaseDate
                    FilterSortBy.LastModified -> it.lastModified
                    FilterSortBy.Favorite -> it.isFavorite.toString()
                }
            }
        } else {
            filtered.sortedByDescending {
                when (filter.sortBy) {
                    FilterSortBy.Title -> it.title
                    FilterSortBy.ReleaseDate -> it.releaseDate
                    FilterSortBy.LastModified -> it.lastModified
                    FilterSortBy.Favorite -> it.isFavorite.toString()
                }
            }
        }
    }

    val seriesCollection = seriesModel.series.collectAsState()
    val filteredSeries = seriesCollection.value.filter { series ->
        // Search Text Filter
        (searchText.isEmpty() ||
                series.title.contains(searchText, ignoreCase = true) ||
                series.genres.any { it.contains(searchText, ignoreCase = true) } ||
                series.status.contains(searchText, ignoreCase = true) ||
                series.country.contains(searchText, ignoreCase = true)) &&
                // Genres Filter
                (filter.genres.isEmpty() || series.genres.any { filter.genres.contains(it) }) &&
                // Status Filter
                (filter.statuses.isEmpty() || filter.statuses.contains(series.status)) &&
                // Countries Filter
                (filter.countries.isEmpty() || filter.countries.contains(series.country))
    }.let { filtered ->
        // Sorting
        if (filter.sortDirection == FilterSortDirection.Ascending) {
            filtered.sortedBy {
                when (filter.sortBy) {
                    FilterSortBy.Title -> it.title
                    FilterSortBy.ReleaseDate -> it.releaseDate
                    FilterSortBy.LastModified -> it.lastModified
                    FilterSortBy.Favorite -> it.isFavorite.toString()
                }
            }
        } else {
            filtered.sortedByDescending {
                when (filter.sortBy) {
                    FilterSortBy.Title -> it.title
                    FilterSortBy.ReleaseDate -> it.releaseDate
                    FilterSortBy.LastModified -> it.lastModified
                    FilterSortBy.Favorite -> it.isFavorite.toString()
                }
            }
        }
    }

    var selectionMode by remember { mutableStateOf(false) }
    val movieUUIDs = filteredMovies.map { it.uuid }
    val selectedMoviesList = remember { mutableStateListOf<String>() }
    val seriesUUIDs = filteredSeries.map { it.uuid }
    val selectedSeriesList = remember { mutableStateListOf<String>() }
    var showDeleteSelectedDialog by remember { mutableStateOf(false) }
    val selectedItems = selectedMoviesList.size + selectedSeriesList.size
    val title = if (selectionMode) {
        "Selected $selectedItems ${if (selectedItems == 1) "item" else "items"}"
    } else {
        "My Watchlist"
    }

    BackHandler(enabled = selectionMode) {
        selectionMode = false
        selectedMoviesList.clear()
        selectedSeriesList.clear()
    }

    HomeScaffold(
        navController = navController,
        title = title,
        selectionMode = selectionMode,
        onDeselectAll = {
            when (pagerState.currentPage) {
                0 -> {
                    selectedMoviesList.clear()
                }

                1 -> {
                    selectedSeriesList.clear()
                }
            }
        },
        onInvertSelected = {
            when (pagerState.currentPage) {
                0 -> {
                    val newList = movieUUIDs.filterNot { it in selectedMoviesList }
                    selectedMoviesList.apply {
                        clear()
                        addAll(newList)
                    }
                }

                1 -> {
                    val newList = seriesUUIDs.filterNot { it in selectedSeriesList }
                    selectedSeriesList.apply {
                        clear()
                        addAll(newList)
                    }
                }
            }
        },
        onSelectAll = {
            when (pagerState.currentPage) {
                0 -> {
                    selectedMoviesList.apply {
                        clear()
                        addAll(movieUUIDs)
                    }
                }

                1 -> {
                    selectedSeriesList.apply {
                        clear()
                        addAll(seriesUUIDs)
                    }
                }
            }
        },
        enableDelete = selectionMode && selectedItems > 0,
        onDelete = {
            showDeleteSelectedDialog = true
        },
        onCancelSelection = {
            selectionMode = false
            selectedMoviesList.clear()
            selectedSeriesList.clear()
        },
        onSearch = { searchText = it },
        isFilterActive = filter.genres.isNotEmpty() || filter.statuses.isNotEmpty() || filter.countries.isNotEmpty(),
        onFilter = { showFilter = !showFilter },
        onAddMovie = { showAddMovie = true },
        onAddSeries = { showAddSeries = true }
    ) { innerPadding ->
        if (showAddMovie) {
            AddMovieSheet(
                addMovieState,
                movieModel,
                genres.value,
                countries.value,
                statuses.value
            ) { showAddMovie = false }
        }

        if (showAddSeries) {
            AddSeriesSheet(
                addSeriesState,
                seriesModel,
                genres.value,
                countries.value,
                statuses.value
            ) { showAddSeries = false }
        }

        if (showViewMovie) {
            ViewEditMovieSheet(
                editMode = editMode,
                movie = selectedMovie,
                sheetState = viewMovieState,
                movieModel = movieModel,
                genres = genres.value,
                countries.value,
                statuses.value
            ) {
                showViewMovie = false
            }
        }

        if (showViewSeries) {
            ViewEditSeriesSheet(
                editMode = editMode,
                series = selectedSeries,
                sheetState = viewSeriesState,
                seriesModel = seriesModel,
                genres = genres.value,
                countries.value,
                statuses.value
            ) {
                showViewSeries = false
            }
        }

        if (showDeleteMovieDialog) {
            ConfirmationDialog(
                state = showDeleteMovieDialog,
                title = "Delete Movie",
                message = "Are you sure you want to delete the movie \"${selectedMovie?.title}\"?",
                onDismiss = { showDeleteMovieDialog = false },
                onCancelText = "Cancel",
                onCancel = { showDeleteMovieDialog = false },
                onConfirmText = "Ok"
            ) {
                selectedMovie?.let { movieModel.deleteMovie(it.uuid) }
                showDeleteMovieDialog = false
            }
        }

        if (showDeleteSeriesDialog) {
            ConfirmationDialog(
                state = showDeleteSeriesDialog,
                title = "Delete Series",
                message = "Are you sure you want to delete the series \"${selectedSeries?.title}\"?",
                onDismiss = { showDeleteSeriesDialog = false },
                onCancelText = "Cancel",
                onCancel = { showDeleteSeriesDialog = false },
                onConfirmText = "Ok"
            ) {
                selectedSeries?.let { seriesModel.deleteSeries(it.uuid) }
                showDeleteSeriesDialog = false
            }
        }

        if (showDeleteSelectedDialog) {
            ConfirmationDialog(
                state = showDeleteSelectedDialog,
                title = "Delete ${selectedItems} ${if (selectedItems == 1) "item" else "items"}",
                message = "Are you sure you want to delete ${if (selectedItems == 1) "this selected item" else "these selected items"}?",
                onDismiss = { showDeleteSelectedDialog = false },
                onCancelText = "Cancel",
                onCancel = { showDeleteSelectedDialog = false },
                onConfirmText = "Ok"
            ) {
                movieModel.deleteMovies(selectedMoviesList)
                seriesModel.deleteSeries(selectedSeriesList)
                selectedMoviesList.clear()
                selectedSeriesList.clear()
                showDeleteSelectedDialog = false
            }
        }

        if (showFilter) {
            FilterSheet(
                filterState,
                filter,
                genres.value,
                countries.value,
                statuses.value,
                onDismiss = { showFilter = false }
            ) {
                filter = it
                SharedPref.setFilter(it)
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
                    badge = if (filteredMovies.size > 999) "999+" else filteredMovies.size.toString(),
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
                    badge = if (filteredSeries.size > 999) "999+" else filteredSeries.size.toString(),
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

            HorizontalPager(
                state = pagerState,
                modifier = Modifier.weight(1f),
                verticalAlignment = Alignment.Top
            ) { page ->
                when (page) {
                    0 -> MovieTab(
                        movieModel = movieModel,
                        movies = filteredMovies,
                        selectionMode = selectionMode,
                        enableSelectionMode = { selectionMode = true },
                        selectedItems = selectedMoviesList,
                        onItemSelected = {
                            if (selectedMoviesList.contains(it)) {
                                selectedMoviesList.remove(it)
                            } else {
                                selectedMoviesList.add(it)
                            }
                        },
                        onView = {
                            selectedMovie = it
                            editMode = false
                            showViewMovie = true
                        },
                        onEdit = {
                            selectedMovie = it
                            editMode = true
                            showViewMovie = true
                        },
                        onDelete = {
                            selectedMovie = it
                            showDeleteMovieDialog = true
                        },
                    )

                    1 -> SeriesTab(
                        seriesModel = seriesModel,
                        series = filteredSeries,
                        selectionMode = selectionMode,
                        enableSelectionMode = { selectionMode = true },
                        selectedItems = selectedSeriesList,
                        onItemSelected = {
                            if (selectedSeriesList.contains(it)) {
                                selectedSeriesList.remove(it)
                            } else {
                                selectedSeriesList.add(it)
                            }
                        },
                        onView = {
                            selectedSeries = it
                            editMode = false
                            showViewSeries = true
                        },
                        onEdit = {
                            selectedSeries = it
                            editMode = true
                            showViewSeries = true
                        },
                        onDelete = {
                            selectedSeries = it
                            showDeleteSeriesDialog = true
                        }
                    )
                }
            }
        }
    }
}