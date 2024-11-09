package com.kheemwel.mywatchlist.ui.screens.home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.kheemwel.mywatchlist.R
import com.kheemwel.mywatchlist.data.models.FilterSortBy
import com.kheemwel.mywatchlist.data.models.FilterSortDirection
import com.kheemwel.mywatchlist.data.models.FilterWatchList
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FilterSheet(
    sheetState: SheetState,
    filter: FilterWatchList,
    genres: List<String>,
    countries: List<String>,
    statuses: List<String>,
    onDismiss: () -> Unit,
    onFilterChange: (FilterWatchList) -> Unit = {},
) {
    val tabs = listOf("Status", "Genre", "Country", "Sort")
    val scope = rememberCoroutineScope()
    val pagerState = rememberPagerState(0) { tabs.size }

    ModalBottomSheet(onDismissRequest = onDismiss, sheetState = sheetState, dragHandle = null) {
        TabRow(selectedTabIndex = pagerState.currentPage,
            indicator = {
                Row(
                    modifier = Modifier.tabIndicatorOffset(it[pagerState.currentPage]),
                    horizontalArrangement = Arrangement.Center,
                ) {
                    Box(
                        modifier = Modifier
                            .height(3.dp)
                            .width(tabs[pagerState.currentPage].length.dp * 5)
                            .background(MaterialTheme.colorScheme.primary, RoundedCornerShape(8.dp))
                    )
                }
            }
        ) {
            tabs.forEachIndexed { index, title ->
                Tab(
                    text = { Text(title) },
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
                0 -> StatusTab(statuses, filter.statuses) {
                    onFilterChange(filter.copy(statuses = it))
                }

                1 -> GenreTab(genres, filter.genres) {
                    onFilterChange(filter.copy(genres = it))
                }

                2 -> CountryTab(countries, filter.countries) {
                    onFilterChange(filter.copy(countries = it))
                }

                3 -> SortTab(filter.sortBy, filter.sortDirection) { newSortBy, newSortDirection ->
                    onFilterChange(
                        filter.copy(
                            sortBy = newSortBy,
                            sortDirection = newSortDirection
                        )
                    )
                }
            }
        }

    }
}

@Composable
private fun StatusTab(
    statuses: List<String>,
    initialValues: List<String> = emptyList(),
    onItemsSelected: (List<String>) -> Unit
) {
    val selectedItems = remember(initialValues) { mutableStateListOf<String>() }
    LaunchedEffect(initialValues) {
        selectedItems.addAll(initialValues)
    }

    LazyColumn {
        items(statuses) { status ->
            ListItem(
                modifier = Modifier.clickable {
                    if (selectedItems.contains(status)) {
                        selectedItems.remove(status)
                    } else {
                        selectedItems.add(status)
                    }
                    onItemsSelected(selectedItems)
                },
                leadingContent = {
                    Checkbox(
                        checked = selectedItems.contains(status),
                        onCheckedChange = null
                    )
                },
                headlineContent = { Text(status) }
            )
        }
    }
}

@Composable
private fun GenreTab(
    genres: List<String>,
    initialValues: List<String> = emptyList(),
    onItemsSelected: (List<String>) -> Unit
) {
    val selectedItems = remember(initialValues) { mutableStateListOf<String>() }
    LaunchedEffect(initialValues) {
        selectedItems.addAll(initialValues)
    }

    LazyColumn {
        items(genres) { genre ->
            ListItem(
                modifier = Modifier.clickable {
                    if (selectedItems.contains(genre)) {
                        selectedItems.remove(genre)
                    } else {
                        selectedItems.add(genre)
                    }
                    onItemsSelected(selectedItems)
                },
                leadingContent = {
                    Checkbox(
                        checked = selectedItems.contains(genre),
                        onCheckedChange = null
                    )
                },
                headlineContent = { Text(genre) }
            )
        }
    }
}

@Composable
private fun CountryTab(
    countries: List<String>,
    initialValues: List<String> = emptyList(),
    onItemsSelected: (List<String>) -> Unit
) {
    val selectedItems = remember(initialValues) { mutableStateListOf<String>() }
    LaunchedEffect(initialValues) {
        selectedItems.addAll(initialValues)
    }

    LazyColumn {
        items(countries) { country ->
            ListItem(
                modifier = Modifier.clickable {
                    if (selectedItems.contains(country)) {
                        selectedItems.remove(country)
                    } else {
                        selectedItems.add(country)
                    }
                    onItemsSelected(selectedItems)
                },
                leadingContent = {
                    Checkbox(
                        checked = selectedItems.contains(country),
                        onCheckedChange = null
                    )
                },
                headlineContent = { Text(country) }
            )
        }
    }
}

@Composable
private fun SortTab(
    sortBy: FilterSortBy,
    sortDirection: FilterSortDirection,
    onSortChange: (FilterSortBy, FilterSortDirection) -> Unit
) {
    val sortOptions = listOf(
        Pair(FilterSortBy.Title, "Alphabetically"),
        Pair(FilterSortBy.ReleaseDate, "Release Date"),
        Pair(FilterSortBy.LastModified, "Last Modified")
    )

    LazyColumn {
        items(sortOptions) { (currentSortBy, label) ->
            ListItem(
                modifier = Modifier.clickable {
                    val newSortDirection = if (sortBy == currentSortBy) {
                        if (sortDirection == FilterSortDirection.Ascending) {
                            FilterSortDirection.Descending
                        } else {
                            FilterSortDirection.Ascending
                        }
                    } else {
                        sortDirection
                    }
                    onSortChange(currentSortBy, newSortDirection)
                },
                leadingContent = {
                    if (sortBy == currentSortBy) {
                        Icon(
                            painter = painterResource(
                                if (sortDirection == FilterSortDirection.Ascending) {
                                    R.drawable.baseline_arrow_upward_24
                                } else {
                                    R.drawable.baseline_arrow_downward_24
                                }
                            ),
                            contentDescription = if (sortDirection == FilterSortDirection.Ascending) {
                                "Ascending"
                            } else {
                                "Descending"
                            },
                            tint = MaterialTheme.colorScheme.primary
                        )
                    } else {
                        Spacer(modifier = Modifier.size(24.dp))
                    }
                },
                headlineContent = { Text(label) }
            )
        }
    }
}