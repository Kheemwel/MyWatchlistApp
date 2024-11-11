package com.kheemwel.mywatchlist.ui.screens.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.kheemwel.mywatchlist.R
import com.kheemwel.mywatchlist.data.models.CountryModel
import com.kheemwel.mywatchlist.data.models.FilterSortBy
import com.kheemwel.mywatchlist.data.models.FilterSortDirection
import com.kheemwel.mywatchlist.data.models.FilterWatchList
import com.kheemwel.mywatchlist.data.models.GenreModel
import com.kheemwel.mywatchlist.data.models.Movie
import com.kheemwel.mywatchlist.data.models.MovieModel
import com.kheemwel.mywatchlist.data.models.Series
import com.kheemwel.mywatchlist.data.models.SeriesModel
import com.kheemwel.mywatchlist.data.models.StatusModel
import com.kheemwel.mywatchlist.ui.composables.ConfirmationDialog
import com.kheemwel.mywatchlist.ui.composables.HomeScaffold
import com.kheemwel.mywatchlist.ui.composables.WatchTile
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
    var filter by remember { mutableStateOf(FilterWatchList()) }
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
                }
            }
        } else {
            filtered.sortedByDescending {
                when (filter.sortBy) {
                    FilterSortBy.Title -> it.title
                    FilterSortBy.ReleaseDate -> it.releaseDate
                    FilterSortBy.LastModified -> it.lastModified
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
                }
            }
        } else {
            filtered.sortedByDescending {
                when (filter.sortBy) {
                    FilterSortBy.Title -> it.title
                    FilterSortBy.ReleaseDate -> it.releaseDate
                    FilterSortBy.LastModified -> it.lastModified
                }
            }
        }
    }

    HomeScaffold(
        navController,
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

        if (showFilter) {
            FilterSheet(
                filterState,
                filter,
                genres.value,
                countries.value,
                statuses.value,
                onDismiss = { showFilter = false }) {
                filter = it
                println(filter.statuses)
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
                HomeTab(
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

                HomeTab(
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

            HorizontalPager(state = pagerState) { page ->
                when (page) {
                    0 -> MovieTab(
                        movieModel,
                        filteredMovies,
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
                        })

                    1 -> SeriesTab(
                        seriesModel,
                        filteredSeries,
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
                        })
                }
            }
        }
    }
}

@Composable
private fun HomeTab(
    title: String,
    badge: String,
    icon: @Composable (() -> Unit)? = null,
    selected: Boolean,
    onClick: () -> Unit
) {
    Tab(
        text = {
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(title)
                Text(
                    text = badge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier
                        .background(MaterialTheme.colorScheme.surfaceVariant, CircleShape)
                        .padding(horizontal = 6.dp)
                )
            }
        },
        icon = icon,
        selected = selected,
        onClick = onClick,
        selectedContentColor = MaterialTheme.colorScheme.primary,
        unselectedContentColor = MaterialTheme.colorScheme.onSurfaceVariant
    )
}

@Composable
private fun MovieTab(
    movieModel: MovieModel,
    movies: List<Movie>,
    onView: (Movie) -> Unit,
    onEdit: (Movie) -> Unit,
    onDelete: (Movie) -> Unit
) {
    LazyColumn {
        items(movies) { movie ->
            WatchTile(
                text = movie.title,
                status = movie.status,
                isFavorite = movie.isFavorite,
                onFavorite = { movieModel.toggleFavorite(movie.uuid) },
                onEdit = { onEdit(movie) },
                onDelete = { onDelete(movie) },
                onClick = { onView(movie) }
            )
        }
        item {
            Spacer(modifier = Modifier.height(150.dp))
        }
    }
}

@Composable
private fun SeriesTab(
    seriesModel: SeriesModel,
    series: List<Series>,
    onView: (Series) -> Unit,
    onEdit: (Series) -> Unit,
    onDelete: (Series) -> Unit
) {
    LazyColumn {
        items(series) { series ->
            WatchTile(
                text = series.title,
                status = series.status,
                isFavorite = series.isFavorite,
                onFavorite = { seriesModel.toggleFavorite(series.uuid) },
                onEdit = { onEdit(series) },
                onDelete = { onDelete(series) },
                onClick = { onView(series) }
            )
        }
        item {
            Spacer(modifier = Modifier.height(150.dp))
        }
    }
}
