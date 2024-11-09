package com.kheemwel.mywatchlist.ui.screens.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
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
import com.kheemwel.mywatchlist.data.models.MovieModel
import com.kheemwel.mywatchlist.data.models.SeriesModel
import com.kheemwel.mywatchlist.data.models.StatusModel
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

    var showAddMovie by remember { mutableStateOf(false) }
    val addMovieState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    var showAddSeries by remember { mutableStateOf(false) }
    val addSeriesState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    var showFilter by remember { mutableStateOf(false) }
    val filterState = rememberModalBottomSheetState()

    var searchText by remember { mutableStateOf("") }
    var filter by remember { mutableStateOf(FilterWatchList()) }

    HomeScaffold(
        navController,
        onSearch = { searchText = it },
        isFilterActive = filter.genres.isNotEmpty() || filter.statuses.isNotEmpty() || filter.countries.isNotEmpty(),
        onFilter = { showFilter = !showFilter },
        onAddMovie = { showAddMovie = true },
        onAddSeries = { showAddSeries = true }
    ) { innerPadding ->

        val tabs = listOf("Movies", "Series")
        val icons = listOf(
            painterResource(R.drawable.baseline_movie_24),
            painterResource(R.drawable.baseline_live_tv_24)
        )
        val scope = rememberCoroutineScope()
        val pagerState = rememberPagerState(0) { tabs.size }

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
                    Box(
                        modifier = Modifier
                            .tabIndicatorOffset(it[pagerState.currentPage])
                            .height(3.dp)
                            .padding(horizontal = 64.dp)
                            .background(MaterialTheme.colorScheme.primary, RoundedCornerShape(8.dp))
                    )
                }
            ) {
                tabs.forEachIndexed { index, title ->
                    Tab(
                        text = { Text(title) },
                        icon = { Icon(icons[index], contentDescription = title) },
                        selected = pagerState.currentPage == index,
                        onClick = {
                            scope.launch {
                                pagerState.animateScrollToPage(index)
                            }
                        },
                        selectedContentColor = MaterialTheme.colorScheme.primary,
                        unselectedContentColor = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            HorizontalPager(state = pagerState) { page ->
                when (page) {
                    0 -> MovieTab(movieModel, searchText, filter)
                    1 -> SeriesTab(seriesModel, searchText, filter)
                }
            }
        }
    }
}

@Composable
private fun MovieTab(movieModel: MovieModel, searchText: String, filter: FilterWatchList) {
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
    }
        .let { filtered ->
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

    LazyColumn {
        items(filteredMovies) { movie ->
            WatchTile(
                text = movie.title,
                status = movie.status,
                isFavorite = movie.isFavorite,
                onFavorite = { movieModel.toggleFavorite(movie.uuid) },
                onEdit = { /*TODO*/ },
                onDelete = { /*TODO*/ })
        }
        item {
            Spacer(modifier = Modifier.height(150.dp))
        }
    }
}

@Composable
private fun SeriesTab(seriesModel: SeriesModel, searchText: String, filter: FilterWatchList) {
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
    }
        .let { filtered ->
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

    LazyColumn {
        items(filteredSeries) { series ->
            WatchTile(
                text = series.title,
                status = series.status,
                isFavorite = series.isFavorite,
                onFavorite = { seriesModel.toggleFavorite(series.uuid) },
                onEdit = { /*TODO*/ },
                onDelete = { /*TODO*/ })
        }
        item {
            Spacer(modifier = Modifier.height(150.dp))
        }
    }
}
