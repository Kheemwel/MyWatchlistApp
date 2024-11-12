package com.kheemwel.mywatchlist.ui.screens.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.kheemwel.mywatchlist.data.models.Movie
import com.kheemwel.mywatchlist.data.models.MovieModel
import com.kheemwel.mywatchlist.data.models.Series
import com.kheemwel.mywatchlist.data.models.SeriesModel
import com.kheemwel.mywatchlist.ui.composables.WatchTile


@Composable
fun HomePrimaryTab(
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
fun MovieTab(
    movieModel: MovieModel,
    movies: List<Movie>,
    selectionMode: Boolean = false,
    enableSelectionMode: () -> Unit = {},
    selectedItems: List<String> = emptyList(),
    onItemSelected: (String) -> Unit = {},
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
                selectionMode = selectionMode,
                selected = selectedItems.contains(movie.uuid),
                onClick = {
                    if (selectionMode) {
                        onItemSelected(movie.uuid)
                    } else {
                        onView(movie)
                    }
                },
                onLongClick = {
                    enableSelectionMode()
                    onItemSelected(movie.uuid)
                }
            )
        }
        item {
            Spacer(modifier = Modifier.height(150.dp))
        }
    }
}

@Composable
fun SeriesTab(
    seriesModel: SeriesModel,
    series: List<Series>,
    selectionMode: Boolean = false,
    enableSelectionMode: () -> Unit = {},
    selectedItems: List<String> = emptyList(),
    onItemSelected: (String) -> Unit = {},
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
                selectionMode = selectionMode,
                selected = selectedItems.contains(series.uuid),
                onClick = {
                    if (selectionMode) {
                        onItemSelected(series.uuid)
                    } else {
                        onView(series)
                    }
                },
                onLongClick = {
                    enableSelectionMode()
                    onItemSelected(series.uuid)
                }
            )
        }
        item {
            Spacer(modifier = Modifier.height(150.dp))
        }
    }
}
